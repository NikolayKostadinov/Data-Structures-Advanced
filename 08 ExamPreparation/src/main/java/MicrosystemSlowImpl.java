import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MicrosystemSlowImpl implements Microsystem {
    private List<Computer> computers;

    public MicrosystemSlowImpl() {
        this.computers = new ArrayList<>();
    }

    @Override
    public void createComputer(Computer computer) {
        if (computers.contains(computer)) {
            throw new IllegalArgumentException();
        }

        this.computers.add(computer);
    }

    @Override
    public boolean contains(int number) {
        return this.computers
                .stream()
                .anyMatch(x -> x.getNumber() == number);
    }

    @Override
    public int count() {
        return this.computers.size();
    }

    @Override
    public Computer getComputer(int number) {
        return this.computers
                .stream()
                .filter(c -> c.getNumber() == number)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public void remove(int number) {
        Computer computer = getComputer(number);
        this.computers.remove(computer);
    }

    @Override
    public void removeWithBrand(Brand brand) {
        List<Computer> forDelete = this.computers
                .stream()
                .filter(c -> c.getBrand().equals(brand))
                .collect(Collectors.toList());
        if (forDelete.isEmpty()) {
            throw new IllegalArgumentException();
        }
        forDelete.forEach(c -> this.computers.remove(c));
    }

    @Override
    public void upgradeRam(int ram, int number) {
        Computer computer = getComputer(number);
        if (computer.getRAM() < ram) {
            computer.setRAM(ram);
        }
    }

    @Override
    public Iterable<Computer> getAllFromBrand(Brand brand) {
        return this.computers
                .stream()
                .filter(c -> c.getBrand().equals(brand))
                .sorted((c1, c2) -> Double.compare(c2.getPrice(), c1.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getAllWithScreenSize(double screenSize) {
        return this.computers
                .stream()
                .filter(c -> c.getScreenSize() == screenSize)
                .sorted((c1, c2) -> Integer.compare(c2.getNumber(), c1.getNumber()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getAllWithColor(String color) {
        return this.computers
                .stream()
                .filter(c -> c.getColor().equals(color))
                .sorted((c1, c2) -> Double.compare(c2.getPrice(), c1.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getInRangePrice(double minPrice, double maxPrice) {
        return this.computers
                .stream()
                .filter(c -> minPrice <= c.getPrice() && c.getPrice() <= maxPrice)
                .sorted((c1, c2) -> Double.compare(c2.getPrice(), c1.getPrice()))
                .collect(Collectors.toList());
    }
}
