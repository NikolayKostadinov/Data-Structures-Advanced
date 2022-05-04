import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ShoppingCentre {
    private Map<Product, List<Product>> products;
    private Map<String, Set<Product>> productsByName;
    private TreeMap<Double, Set<Product>> productsByPrice;
    private Map<String, Set<Product>> productsByProducer;
    private Map<NameProducerKey, Set<Product>> productsByNameProducer;

    public ShoppingCentre() {
        this.products = new HashMap<>();
        this.productsByName = new HashMap<>();
        this.productsByPrice = new TreeMap<>();
        this.productsByProducer = new HashMap<>();
        this.productsByNameProducer = new HashMap<>();
    }

    public String addProduct(String name, double price, String producer) {
        Product product = new Product(name, price, producer);
        if (!this.products.containsKey(product)) {
            this.products.put(product, new ArrayList<>());
            addToIndices(product);
        }
        this.products.get(product).add(product);
        return "Product added" + System.lineSeparator();
    }

    private void addToIndices(Product product) {
        addToProductsByNameIndex(product);
        addToProductsByPriceIndex(product);
        addToProductsByProducerIndex(product);
        addToProductsByNameProducerIndex(product);
    }

    private void addToProductsByPriceIndex(Product product) {
        this.productsByPrice.putIfAbsent(product.getPrice(), new TreeSet<>());
        this.productsByPrice.get(product.getPrice()).add(product);
    }

    private void addToProductsByNameIndex(Product product) {
        this.productsByName.putIfAbsent(product.getName(), new TreeSet<>());
        this.productsByName.get(product.getName()).add(product);
    }

    private void addToProductsByProducerIndex(Product product) {
        this.productsByProducer.putIfAbsent(product.getProducer(), new TreeSet<>());
        this.productsByProducer.get(product.getProducer()).add(product);
    }

    private void addToProductsByNameProducerIndex(Product product) {
        NameProducerKey key = new NameProducerKey(product);
        this.productsByNameProducer.putIfAbsent(key, new TreeSet<>());
        this.productsByNameProducer.get(key).add(product);
    }

    public String delete(String name, String producer) {
        NameProducerKey key = new NameProducerKey(name, producer);
        int removedRecordsCount = 0;
        if (this.productsByNameProducer.containsKey(key)) {
            Set<Product> removedProducts = this.productsByNameProducer.remove(key);
            removedRecordsCount = removeRecords(removedProducts);
        }

        return (removedRecordsCount == 0 ? "No products found"
                : String.format("%d products deleted", removedRecordsCount)) + System.lineSeparator();
    }

    public String delete(String producer) {
        int removedRecordsCount = 0;
        if (this.productsByProducer.containsKey(producer)) {
            Set<Product> removedProducts = this.productsByProducer.remove(producer);
            removedRecordsCount = removeRecords(removedProducts);
        }
        return (removedRecordsCount == 0 ? "No products found"
                : String.format("%d products deleted", removedRecordsCount)) + System.lineSeparator();
    }

    public String findProductsByName(String name) {
        return getString(this.productsByName.get(name));
    }


    public String findProductsByProducer(String producer) {
        return getString(this.productsByProducer.get(producer));
    }

    public String findProductsByPriceRange(double priceFrom, double priceTo) {
        List<Product> products = new ArrayList<>();
        this.productsByPrice.subMap(priceFrom, true, priceTo, true)
                .entrySet()
                .forEach(ps -> products.addAll(ps.getValue()));
        return getString(products);
    }

    private String getString(Collection<Product> products) {
        return (products == null || products.isEmpty() ? "No products found"
                : products
                .stream()
                .flatMap(p -> this.products.get(p).stream())
                .sorted(Product::compareTo)
                .map(Product::toString)
                .collect(Collectors.joining(System.lineSeparator()))) + System.lineSeparator();
    }

    private int removeRecords(Set<Product> removedProducts) {
        AtomicInteger removedProductsCount = new AtomicInteger();
        if (removedProducts != null) {
            removedProducts.forEach(
                    product -> {
                        List<Product> removed = this.products.remove(product);
                        if (removed != null) {
                            removedProductsCount.getAndAdd(removed.size());
                            removeIndices(product);
                        }
                    }
            );
        }
        return removedProductsCount.get();
    }

    private void removeIndices(Product product) {
        removeFromProductsByNameIndex(product);
        removeFromProductsByPriceIndex(product);
        removeFromProductsByProducerIndex(product);
        removeFromProductsByNameProducerIndex(product);
    }

    private void removeFromProductsByNameIndex(Product product) {
        Set<Product> products = this.productsByName.get(product.getName());
        if (products != null) {
            products.remove(product);
        }
    }

    private void removeFromProductsByPriceIndex(Product product) {
        Set<Product> products = this.productsByPrice.get(product.getPrice());
        if (products != null) {
            products.remove(product);
        }
    }

    private void removeFromProductsByNameProducerIndex(Product product) {
        Set<Product> products = this.productsByNameProducer.get(new NameProducerKey(product));
        if (products != null) {
            products.remove(product);
        }
    }

    private void removeFromProductsByProducerIndex(Product product) {
        Set<Product> products = this.productsByProducer.get(product.getProducer());
        if (products != null) {
            products.remove(product);
        }
    }
}
