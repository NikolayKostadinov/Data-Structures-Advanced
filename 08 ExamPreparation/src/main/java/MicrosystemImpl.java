import java.util.*;
import java.util.stream.Collectors;

public class MicrosystemImpl implements Microsystem {

    private Map<Integer, Computer> computers;
   // private TreeSet<Computer> computersByPrice;

    public MicrosystemImpl() {
        this.computers = new HashMap<>();
//        this.computersByPrice = new TreeSet<>();
    }

    @Override
    public void createComputer(Computer computer) {
        if (this.computers.containsKey(computer.getNumber())) {
            throw new IllegalArgumentException();
        }
        this.computers.put(computer.getNumber(), computer);
   //     this.computersByPrice.add(computer);
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
       // this.computersByPrice.remove(removedComputer);
    }

    @Override
    public void removeWithBrand(Brand brand) {
        int preCount = this.count();
        this.computers = this.computers
                .entrySet()
                .stream()
                .filter(x -> !x.getValue().getBrand().equals(brand))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

      //  this.computersByPrice.removeIf(x -> x.getBrand().equals(brand));

        if (preCount == this.count()) throw new IllegalArgumentException();

    }

    @Override
    public void upgradeRam(int ram, int number) {
        Computer computer = this.getComputer(number);
        if (ram > computer.getRAM()) {
            computer.setRAM(ram);
            this.computers.put(number, computer);
          //  this.computersByPrice.add(computer);
        }
    }

    @Override
    public Iterable<Computer> getAllFromBrand(Brand brand) {
        return this.computers
                .values()
                .stream()
                .filter(c -> c.getBrand().equals(brand))
                .sorted(Computer::compareTo)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getAllWithScreenSize(double screenSize) {
        return this.computers
                .values()
                .stream()
                .filter(computer -> Double.compare(computer.getScreenSize(), screenSize) == 0)
                .sorted((c1, c2) -> Integer.compare(c2.getNumber(), c1.getNumber()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getAllWithColor(String color) {
        return this.computers
                .values()
                .stream()
                .filter(c -> c.getColor().equals(color))
                .sorted(Computer::compareTo)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getInRangePrice(double minPrice, double maxPrice) {
        return this.computers
                .values()
                .stream()
                .filter(c -> minPrice <= c.getPrice() && c.getPrice() <= maxPrice)
                .sorted(Computer::compareTo)
                .collect(Collectors.toList());
    }
}
