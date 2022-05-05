import java.util.*;

public class MicrosystemImpl implements Microsystem {

    private final Map<Integer, Computer> computers;
    private final Map<Brand, TreeSet<Computer>> computersByBrand;
    private final Map<String, TreeSet<Computer>> computersByColor;
    private final TreeMap<Double, Set<Computer>> computersByPrice;

    public MicrosystemImpl() {
        this.computers = new HashMap<>();
        this.computersByBrand = new HashMap<>();
        this.computersByColor = new HashMap<>();
        this.computersByPrice = new TreeMap<>(Collections.reverseOrder());
    }

    @Override
    public void createComputer(Computer computer) {
        if (this.computers.containsKey(computer.getNumber())) {
            throw new IllegalArgumentException();
        }
        this.computers.put(computer.getNumber(), computer);
        addToIndices(computer);
    }

    private void addToIndices(Computer computer) {
        addToComputersByBrandIndex(computer);
        addToComputersByColorIndex(computer);
        addToComputersByPriceIndex(computer);
    }

    private void addToComputersByPriceIndex(Computer computer) {
        this.computersByPrice.putIfAbsent(computer.getPrice(), new HashSet<>());
        this.computersByPrice.get(computer.getPrice()).add(computer);
    }

    private void addToComputersByColorIndex(Computer computer) {
        this.computersByColor.putIfAbsent(computer.getColor(), new TreeSet<>());
        this.computersByColor.get(computer.getColor()).add(computer);
    }

    private void addToComputersByBrandIndex(Computer computer) {
        this.computersByBrand.putIfAbsent(computer.getBrand(), new TreeSet<>());
        this.computersByBrand.get(computer.getBrand()).add(computer);
    }

    @Override
    public boolean contains(int number) {
        return this.computers.containsKey(number);
    }

    @Override
    public int count() {
        return this.computers.size();
    }

    @Override
    public Computer getComputer(int number) {
        Computer computer = this.computers.get(number);
        if (computer == null) throw new IllegalArgumentException();
        return computer;
    }

    @Override
    public void remove(int number) {
        Computer removedComputer = this.computers.remove(number);
        if (removedComputer == null) throw new IllegalArgumentException();
        removeFromIndices(removedComputer);
    }

    private void removeFromIndices(Computer computer) {
        removeFromComputersByBrandIndex(computer);
        removeFromComputersByColorIndex(computer);
        removeFromComputersByPriceIndex(computer);
    }

    private void removeFromComputersByPriceIndex(Computer computer) {
        Set<Computer> computers = this.computersByPrice.get(computer.getPrice());
        if (computers != null) computers.remove(computer);
    }

    private void removeFromComputersByColorIndex(Computer computer) {
        TreeSet<Computer> computers = this.computersByColor.get(computer.getColor());
        if (computers != null) computers.remove(computer);
    }

    private void removeFromComputersByBrandIndex(Computer computer) {
        TreeSet<Computer> computers = this.computersByBrand.get(computer.getBrand());
        if (computers != null) computers.remove(computer);
    }

    @Override
    public void removeWithBrand(Brand brand) {
        TreeSet<Computer> computers = this.computersByBrand.remove(brand);
        if (computers == null || computers.isEmpty()) throw new IllegalArgumentException();
        computers.forEach(computer -> {
            this.computers.remove(computer.getNumber());
            removeFromIndices(computer);
        });
    }

    @Override
    public void upgradeRam(int ram, int number) {
        Computer computer = this.getComputer(number);
        if (computer.getRAM() < ram) {
            computer.setRAM(ram);
        }
    }

    @Override
    public Iterable<Computer> getAllFromBrand(Brand brand) {
        TreeSet<Computer> computers = this.computersByBrand.get(brand);
        return computers == null ? new ArrayList<>() : computers;
    }

    @Override
    public Iterable<Computer> getAllWithScreenSize(double screenSize) {
        List<Computer> computers = new ArrayList<>();
        this.computers.entrySet()
                .stream()
                .filter(computer -> computer.getValue().getScreenSize() == screenSize)
                .sorted((c1, c2) -> Integer.compare(c2.getKey(), c1.getKey()))
                .forEach(computer -> computers.add(computer.getValue()));
        return computers;
    }

    @Override
    public Iterable<Computer> getAllWithColor(String color) {
        TreeSet<Computer> computers = this.computersByColor.get(color);
        return computers == null ? new ArrayList<>() : computers;
    }

    @Override
    public Iterable<Computer> getInRangePrice(double minPrice, double maxPrice) {
        List<Computer> result = new ArrayList<>();

        this.computersByPrice
                .subMap(maxPrice, true, minPrice, true)
                .forEach((key, value) -> result.addAll(value));

        return result;
    }
}
