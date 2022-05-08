public class Computer implements Comparable {

    private static final int DEFAULT_RAM_VALUE = 8;

    private int number;
    private Brand brand;
    private double price;
    private double screenSize;
    private String color;
    private int RAM;

    public Computer(int number, Brand brand, double price, double screenSize, String color)
    {
        this.number = number;
        this.RAM = DEFAULT_RAM_VALUE;
        this.brand = brand;
        this.price = price;
        this.screenSize = screenSize;
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(double screenSize) {
        this.screenSize = screenSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getRAM() {
        return RAM;
    }

    public void setRAM(int RAM) {
        this.RAM = RAM;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Computer)) return false;
        Computer computer = (Computer) o;
        return getNumber() == computer.getNumber();
    }

    @Override
    public int hashCode() {
        return getNumber();
    }


    @Override
    public int compareTo(Object o) {
        Computer other = (Computer) o;
        return Double.compare(other.getPrice(), this.getPrice());
    }
}
