import java.util.Objects;

public class NameProducerKey {
    private final String name;
    private final String producer;

    public NameProducerKey(Product product) {
        this(product.getName(), product.getProducer());
    }

    public NameProducerKey(String name, String producer) {
        this.name = name;
        this.producer = producer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NameProducerKey)) return false;
        NameProducerKey that = (NameProducerKey) o;
        return name.equals(that.name) && producer.equals(that.producer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, producer);
    }
}
