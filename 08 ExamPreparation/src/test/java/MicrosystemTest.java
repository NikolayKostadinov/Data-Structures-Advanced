import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MicrosystemTest {
    private Microsystem microsystem;
    private Microsystem microsystemSlow;

    private Computer[] computers = {
            new Computer(1, Brand.ACER, 10, 15.6, "grey"),
            new Computer(2, Brand.DELL, 20, 15.6, "grey"),
            new Computer(3, Brand.HP, 30, 13.6, "red"),
            new Computer(4, Brand.ACER, 40, 15.6, "grey"),
            new Computer(5, Brand.DELL, 50, 15.6, "grey"),
            new Computer(6, Brand.HP, 60, 13.6, "red"),
            new Computer(7, Brand.ACER, 70, 15.6, "grey"),
            new Computer(8, Brand.DELL, 80, 15.6, "grey"),
            new Computer(9, Brand.HP, 90, 13.6, "red"),
    };

    @Before
    public void setUp() {
        this.microsystem = new MicrosystemImpl();
        this.microsystemSlow = new MicrosystemSlowImpl();
    }

    @Test
    public void count_should_work_correctly() {

        Computer computer1 = new Computer(2, Brand.ACER, 1120, 15.6, "grey");
        Computer computer = new Computer(1, Brand.DELL, 2300, 15.6, "grey");
        Computer computer2 = new Computer(5, Brand.HP, 2400, 13.6, "red");


        this.microsystem.createComputer(computer);
        this.microsystem.createComputer(computer1);
        this.microsystem.createComputer(computer2);

        final int expectedCount = 3;
        final int actualCount = this.microsystem.count();

        assertEquals("Incorrect count", expectedCount, actualCount);
    }

    @Test
    public void createComputer_should_return_true_with_valid_number() {

        Computer computer1 = new Computer(2, Brand.ACER, 1120, 15.6, "grey");
        Computer computer = new Computer(1, Brand.DELL, 2300, 15.6, "grey");


        this.microsystem.createComputer(computer);
        this.microsystem.createComputer(computer1);

        assertTrue("Incorrect return value", this.microsystem.contains(1));
        assertTrue("Incorrect return value", this.microsystem.contains(2));
    }

    @Test
    public void createComputer_should_increase_count() {
        Computer computer1 = new Computer(1, Brand.ACER, 1120, 15.6, "grey");
        Computer computer2 = new Computer(2, Brand.ASUS, 2000, 15.6, "red");

        this.microsystem.createComputer(computer1);
        this.microsystem.createComputer(computer2);
        final int expectedCount = 2;
        final int actualCount = this.microsystem.count();

        assertEquals("Incorrect count", expectedCount, actualCount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createComputer_should_throw_exception() {

        Computer computer = new Computer(1, Brand.ASUS, 10D, 13.3, "red");
        Computer computer1 = new Computer(1, Brand.DELL, 11D, 14.3, "black");

        this.microsystem.createComputer(computer);
        this.microsystem.createComputer(computer1);
    }

    @Test
    public void removeComputer_should_work_correctly() {

        Computer computer1 = new Computer(2, Brand.ACER, 1120, 15.6, "grey");
        Computer computer = new Computer(1, Brand.DELL, 2300, 15.6, "grey");
        Computer computer2 = new Computer(5, Brand.HP, 2400, 13.6, "red");

        this.microsystem.createComputer(computer);
        this.microsystem.createComputer(computer1);
        this.microsystem.createComputer(computer2);

        this.microsystem.remove(1);
        assertEquals(2,this.microsystem.count());
        assertTrue(microsystem.contains(2));
        assertFalse(microsystem.contains(1));
        assertTrue(microsystem.contains(5));
    }

    @Test
    public void removeComputer_should_work_correctly_both() {

        for (Computer computer : computers) {
            this.microsystem.createComputer(computer);
            this.microsystemSlow.createComputer(computer);
        }

        this.microsystem.remove(5);
        this.microsystemSlow.remove(5);

        List<Computer> computers = toList(this.microsystem.getInRangePrice(0, 1000));
        List<Computer> computersSlow = toList(this.microsystemSlow.getInRangePrice(0, 1000));
        for (int i = 0; i < computers.size(); i++) {
            assertEquals(computers.get(i), computersSlow.get(i));
        }

        this.microsystem.createComputer(this.computers[4]);
        this.microsystemSlow.createComputer(this.computers[4]);
    }

    @Test
    public void removeWithBrand_should_work_correctly() {

        Computer computer1 = new Computer(2, Brand.ACER, 1120, 15.6, "grey");
        Computer computer3 = new Computer(6, Brand.ACER, 1520, 15.6, "grey");
        Computer computer = new Computer(1, Brand.DELL, 2300, 15.6, "grey");
        Computer computer2 = new Computer(5, Brand.HP, 2400, 13.6, "red");

        this.microsystem.createComputer(computer);
        this.microsystem.createComputer(computer1);
        this.microsystem.createComputer(computer2);
        this.microsystem.createComputer(computer3);

        this.microsystem.removeWithBrand(Brand.ACER);
        assertEquals(2,this.microsystem.count());
        assertTrue(microsystem.contains(1));
        assertTrue(microsystem.contains(5));
        assertFalse(microsystem.contains(2));
        assertFalse(microsystem.contains(6));
    }

    @Test
    public void removeWithBrand_should_work_correctly_both() {

        for (Computer computer : computers) {
            this.microsystem.createComputer(computer);
            this.microsystemSlow.createComputer(computer);
        }

        this.microsystem.removeWithBrand(Brand.HP);
        this.microsystemSlow.removeWithBrand(Brand.HP);

        List<Computer> computers = toList(this.microsystem.getInRangePrice(0, 1000));
        List<Computer> computersSlow = toList(this.microsystemSlow.getInRangePrice(0, 1000));
        for (int i = 0; i < computers.size(); i++) {
            assertEquals(computers.get(i), computersSlow.get(i));
        }

        this.microsystem.createComputer(this.computers[5]);
        this.microsystemSlow.createComputer(this.computers[5]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeWithBrand_should_throw_exception() {

        Computer computer1 = new Computer(2, Brand.ACER, 1120, 15.6, "grey");
        Computer computer3 = new Computer(6, Brand.ACER, 1520, 15.6, "grey");
        Computer computer = new Computer(1, Brand.DELL, 2300, 15.6, "grey");
        Computer computer2 = new Computer(5, Brand.DELL, 2400, 13.6, "red");

        this.microsystem.createComputer(computer);
        this.microsystem.createComputer(computer1);
        this.microsystem.createComputer(computer2);
        this.microsystem.createComputer(computer3);

        this.microsystem.removeWithBrand(Brand.HP);
        assertEquals(2,this.microsystem.count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeComputer_should_throw_exception() {

        Computer computer1 = new Computer(2, Brand.ACER, 1120, 15.6, "grey");
        Computer computer = new Computer(1, Brand.DELL, 2300, 15.6, "grey");
        Computer computer2 = new Computer(5, Brand.HP, 2400, 13.6, "red");

        this.microsystem.createComputer(computer);
        this.microsystem.createComputer(computer1);
        this.microsystem.createComputer(computer2);

        this.microsystem.remove(4);
    }

    @Test
    public void getAllWithScreenSize_should_work_correctly() {

        List<Computer> result = toList(this.microsystem.getAllWithScreenSize(15.6));
        assertEquals(0, result.size());

        Computer computer1 = new Computer(2, Brand.ACER, 1120, 15.6, "grey");
        Computer computer = new Computer(1, Brand.DELL, 2300, 15.6, "grey");
        Computer computer2 = new Computer(5, Brand.HP, 2400, 15.6, "red");


        this.microsystem.createComputer(computer);
        this.microsystem.createComputer(computer1);
        this.microsystem.createComputer(computer2);

        result = toList(this.microsystem.getAllWithScreenSize(15.6));

        assertEquals(5, result.get(0).getNumber());
        assertEquals(2, result.get(1).getNumber());
        assertEquals(1, result.get(2).getNumber());
    }

    @Test
    public void getAllWithScreenSize_should_work_correctly_both() {

        for (Computer computer : computers) {
            this.microsystem.createComputer(computer);
            this.microsystemSlow.createComputer(computer);
        }

        this.microsystem.getAllWithScreenSize(15.6);
        this.microsystemSlow.getAllWithScreenSize(15.6);

        List<Computer> computers = toList(this.microsystem.getInRangePrice(0, 1000));
        List<Computer> computersSlow = toList(this.microsystemSlow.getInRangePrice(0, 1000));
        for (int i = 0; i < computers.size(); i++) {
            assertEquals(computers.get(i), computersSlow.get(i));
        }
    }

    @Test
    public void getAllFromBrand_should_work_correctly() {

        List<Computer> result = toList(this.microsystem.getAllFromBrand(Brand.ACER));
        assertEquals(0, result.size());

        Computer computer1 = new Computer(2, Brand.ACER, 1120, 15.6, "grey");
        Computer computer = new Computer(1, Brand.DELL, 2300, 15.6, "grey");
        Computer computer2 = new Computer(5, Brand.ACER, 2400, 15.6, "red");


        this.microsystem.createComputer(computer);
        this.microsystem.createComputer(computer1);
        this.microsystem.createComputer(computer2);

        result = toList(this.microsystem.getAllFromBrand(Brand.ACER));

        assertEquals(2, result.size());
        assertEquals(5, result.get(0).getNumber());
        assertEquals(2, result.get(1).getNumber());

        result = toList(this.microsystem.getAllFromBrand(Brand.HP));

        assertEquals(0, result.size());
    }

    @Test
    public void updateRam_should_work_correctly() {

        List<Computer> result = toList(this.microsystem.getAllWithScreenSize(15.6));
        assertEquals(0, result.size());

        Computer computer1 = new Computer(2, Brand.ACER, 1120, 15.6, "grey");
        Computer computer = new Computer(1, Brand.DELL, 2300, 15.6, "grey");
        Computer computer2 = new Computer(5, Brand.HP, 2400, 15.6, "red");


        this.microsystem.createComputer(computer);
        this.microsystem.createComputer(computer1);
        this.microsystem.createComputer(computer2);

        this.microsystem.upgradeRam(32, 1);
    }

    @Test
    public void getAllWithColor_should_work_correctly() {

        List<Computer> result = toList(this.microsystem.getAllWithColor("grey"));
        assertEquals(0, result.size());

        Computer computer1 = new Computer(2, Brand.ACER, 1120, 15.6, "grey");
        Computer computer = new Computer(1, Brand.DELL, 2300, 15.6, "grey");
        Computer computer2 = new Computer(5, Brand.HP, 2400, 15.6, "red");


        this.microsystem.createComputer(computer1);
        this.microsystem.createComputer(computer);
        this.microsystem.createComputer(computer2);

        result = toList(this.microsystem.getAllWithColor("grey"));

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getNumber());
        assertEquals(2, result.get(1).getNumber());
    }

    @Test
    public void getInRangePrice_should_work_correctly() {

        List<Computer> result = toList(this.microsystem.getInRangePrice(1000, 3000));
        assertEquals(0, result.size());

        Computer computer1 = new Computer(2, Brand.ACER, 1120, 15.6, "grey");
        Computer computer = new Computer(1, Brand.DELL, 2300, 15.6, "grey");
        Computer computer2 = new Computer(5, Brand.HP, 2400, 15.6, "red");


        this.microsystem.createComputer(computer1);
        this.microsystem.createComputer(computer);
        this.microsystem.createComputer(computer2);

        result = toList(this.microsystem.getInRangePrice(1000, 3000));

        assertEquals(3, result.size());
        assertEquals(5, result.get(0).getNumber());
        assertEquals(1, result.get(1).getNumber());
        assertEquals(2, result.get(2).getNumber());
    }

    private List<Computer> toList(Iterable<Computer> iterable) {
        List<Computer> result = new ArrayList<>();
        iterable.forEach(result::add);
        return result;
    }
}
