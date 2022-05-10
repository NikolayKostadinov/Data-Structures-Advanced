package core;

import models.Vehicle;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class VehicleRepositoryTests {
    private VehicleRepository vehicleRepository;

    @Before
    public void setup() {
        this.vehicleRepository = new VehicleRepositoryImpl();
    }

    @Test
    public void testAddVehicle_WithCorrectData_ShouldCorrectlyAddVehicle() {
        Vehicle vehicle = new Vehicle(1 + "", "BMW", "X5", "Sofia", "Blue", 400, 50000, true);
        this.vehicleRepository.addVehicleForSale(vehicle, "George");

        assertEquals(1, this.vehicleRepository.size());
        assertTrue(this.vehicleRepository.contains(vehicle));
    }

    @Test
    public void testAddManyVehicle_WithCorrectData_ShouldCorrectlyAddVehicle() {
        Vehicle vehicle = new Vehicle(1 + "", "BMW", "X5", "Sofia", "Blue", 400, 50000, true);
        Vehicle vehicle2 = new Vehicle(2 + "", "BMW", "X52", "Sofia2", "Blue2", 500, 60000, false);
        Vehicle vehicle3 = new Vehicle(3 + "", "Audi", "A3", "Sofia3", "Blue3", 300, 70000, false);

        this.vehicleRepository.addVehicleForSale(vehicle3, "George");
        this.vehicleRepository.addVehicleForSale(vehicle2, "George");
        this.vehicleRepository.addVehicleForSale(vehicle, "George");

        assertEquals(3, this.vehicleRepository.size());
    }

    @Test
    public void testContains_WithNonexistentVehicle_ShouldReturnFalse() {
        Vehicle vehicle = new Vehicle(1 + "", "BMW", "X5", "Sofia", "Blue", 400, 50000, true);
        Vehicle vehicle2 = new Vehicle(2 + "", "BMW2", "X52", "Sofia2", "Blue2", 500, 60000, false);

        this.vehicleRepository.addVehicleForSale(vehicle, "George");

        assertFalse(this.vehicleRepository.contains(vehicle2));
    }

    @Test
    public void testRemoveVehicle_WithNonexistentVehicle_ShouldThrowException() {
        Vehicle vehicle = new Vehicle(1 + "", "BMW", "X5", "Sofia", "Blue", 400, 50000, true);
        Vehicle vehicle2 = new Vehicle(2 + "", "BMW", "X52", "Sofia2", "Blue2", 500, 60000, false);
        Vehicle vehicle3 = new Vehicle(3 + "", "Audi", "A3", "Sofia3", "Blue3", 300, 70000, false);

        this.vehicleRepository.addVehicleForSale(vehicle3, "George");
        this.vehicleRepository.addVehicleForSale(vehicle2, "George");
        this.vehicleRepository.addVehicleForSale(vehicle, "George");

        assertThrows(IllegalArgumentException.class, () -> this.vehicleRepository.removeVehicle("non-existent"));
    }

    @Test
    public void testGetAllVehiclesOrderedByHorsepowerDescendingThenByPriceThenBySellerName_WithExistentVehicles_ShouldCorrectlyOrderedVehicles() {
        Vehicle vehicle = new Vehicle(1 + "", "BMW", "X5", "Sofia", "Blue", 400, 50000, true);
        Vehicle vehicle2 = new Vehicle(2 + "", "BMW", "X52", "Sofia2", "Blue2", 500, 61000, false);
        Vehicle vehicle3 = new Vehicle(3 + "", "Audi", "A3", "Sofia3", "Blue3", 300, 70000, false);
        Vehicle vehicle4 = new Vehicle(4 + "", "Audi", "A3", "Sofia3", "Blue3", 500, 88000, false);
        Vehicle vehicle5 = new Vehicle(5 + "", "Audi", "A3", "Sofia3", "Blue3", 500, 61000, false);

        this.vehicleRepository.addVehicleForSale(vehicle, "George");
        this.vehicleRepository.addVehicleForSale(vehicle2, "Jack");
        this.vehicleRepository.addVehicleForSale(vehicle3, "Phill");
        this.vehicleRepository.addVehicleForSale(vehicle4, "Isacc");
        this.vehicleRepository.addVehicleForSale(vehicle5, "Igor");

        List<Vehicle> orderedVehicles = StreamSupport.stream(this.vehicleRepository.getAllVehiclesOrderedByHorsepowerDescendingThenByPriceThenBySellerName().spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(5, orderedVehicles.size());

        assertEquals(vehicle5, orderedVehicles.get(0));
        assertEquals(vehicle2, orderedVehicles.get(1));
        assertEquals(vehicle4, orderedVehicles.get(2));
        assertEquals(vehicle, orderedVehicles.get(3));
        assertEquals(vehicle3, orderedVehicles.get(4));
    }

    @Test
    public void testGetVehicles_WithExistentVehicles_ShouldCorrectlyOrderedVehicles() {
        List<Vehicle> vehicles =
                StreamSupport.stream(this.vehicleRepository.getVehicles(List.of("BMW", "A3", "Sofia3", "Blue4")).spliterator(),
                                false)
                        .collect(Collectors.toList());
        assertTrue(vehicles.isEmpty());
        Vehicle vehicle = new Vehicle(1 + "", "BMW", "X5", "Sofia", "Blue", 400, 50000, true);
        Vehicle vehicle2 = new Vehicle(2 + "", "BMW", "X52", "Sofia2", "Blue2", 500, 61000, false);
        Vehicle vehicle3 = new Vehicle(3 + "", "Audi", "A3", "Sofia3", "Blue3", 300, 61000, false);
        Vehicle vehicle4 = new Vehicle(4 + "", "Audi", "A2", "Sofia3", "Blue3", 500, 88000, false);
        Vehicle vehicle5 = new Vehicle(5 + "", "Audi", "A1", "Sofia4", "Blue4", 500, 21000, true);

        this.vehicleRepository.addVehicleForSale(vehicle, "George");
        this.vehicleRepository.addVehicleForSale(vehicle2, "Jack");
        this.vehicleRepository.addVehicleForSale(vehicle3, "Phill");
        this.vehicleRepository.addVehicleForSale(vehicle4, "Isacc");
        this.vehicleRepository.addVehicleForSale(vehicle5, "Igor");

        vehicles =
                StreamSupport.stream(this.vehicleRepository.getVehicles(List.of("BMW", "A3", "Sofia3", "Blue4")).spliterator(),
                                false)
                        .collect(Collectors.toList());

        assertEquals(5, vehicles.size());

        assertEquals(vehicle5, vehicles.get(0));
        assertEquals(vehicle, vehicles.get(1));
        assertEquals(vehicle2, vehicles.get(2));
        assertEquals(vehicle3, vehicles.get(3));
        assertEquals(vehicle4, vehicles.get(4));
    }

    @Test
    public void testGetVehiclesBySeller_WithExistentVehicles_ShouldCorrectlyOrderedVehicles() {
        assertThrows(IllegalArgumentException.class, () -> this.vehicleRepository.getVehiclesBySeller("bbb"));
        List<Vehicle> vehicles =
                StreamSupport.stream(this.vehicleRepository.getVehicles(List.of("BMW", "A3", "Sofia3", "Blue4")).spliterator(),
                                false)
                        .collect(Collectors.toList());
        assertTrue(vehicles.isEmpty());
        Vehicle vehicle = new Vehicle(1 + "", "BMW", "X5", "Sofia", "Blue", 400, 50000, true);
        Vehicle vehicle2 = new Vehicle(2 + "", "BMW", "X52", "Sofia2", "Blue2", 500, 61000, false);
        Vehicle vehicle3 = new Vehicle(3 + "", "Audi", "A3", "Sofia3", "Blue3", 300, 70000, false);
        Vehicle vehicle4 = new Vehicle(4 + "", "Audi", "A2", "Sofia3", "Blue3", 500, 88000, false);
        Vehicle vehicle5 = new Vehicle(5 + "", "Audi", "A1", "Sofia4", "Blue4", 500, 21000, true);

        this.vehicleRepository.addVehicleForSale(vehicle, "George");
        this.vehicleRepository.addVehicleForSale(vehicle3, "aaa");
        this.vehicleRepository.addVehicleForSale(vehicle2, "aaa");
        this.vehicleRepository.addVehicleForSale(vehicle4, "aaa");
        this.vehicleRepository.addVehicleForSale(vehicle5, "Igor");

        vehicles =
                StreamSupport.stream(this.vehicleRepository.getVehiclesBySeller("aaa").spliterator(),
                                false)
                        .collect(Collectors.toList());

        assertEquals(3, vehicles.size());

        assertEquals(vehicle3, vehicles.get(0));
        assertEquals(vehicle2, vehicles.get(1));
        assertEquals(vehicle4, vehicles.get(2));
    }

    @Test
    public void testGetVehicles_ShouldCorrectlyOrderedVehicles() {
        Vehicle vehicle = new Vehicle(1 + "", "BMW", "X5", "Sofia", "Blue", 400, 50000, true);
        Vehicle vehicle2 = new Vehicle(2 + "", "BMW", "X52", "Sofia2", "Blue2", 500, 61000, false);
        Vehicle vehicle3 = new Vehicle(3 + "", "Audi", "A3", "Sofia3", "Blue3", 300, 70000, false);
        Vehicle vehicle4 = new Vehicle(4 + "", "Audi", "A3", "Sofia3", "Blue3", 500, 88000, false);
        Vehicle vehicle5 = new Vehicle(5 + "", "Audi", "A3", "Sofia3", "Blue3", 500, 61000, false);

        this.vehicleRepository.addVehicleForSale(vehicle, "George");
        this.vehicleRepository.addVehicleForSale(vehicle2, "Jack");
        this.vehicleRepository.addVehicleForSale(vehicle3, "Phill");
        this.vehicleRepository.addVehicleForSale(vehicle4, "Isacc");
        this.vehicleRepository.addVehicleForSale(vehicle5, "Igor");

        List<Vehicle> orderedVehicles = StreamSupport.stream(this.vehicleRepository.getAllVehiclesOrderedByHorsepowerDescendingThenByPriceThenBySellerName().spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(5, orderedVehicles.size());

        assertEquals(vehicle5, orderedVehicles.get(0));
        assertEquals(vehicle2, orderedVehicles.get(1));
        assertEquals(vehicle4, orderedVehicles.get(2));
        assertEquals(vehicle, orderedVehicles.get(3));
        assertEquals(vehicle3, orderedVehicles.get(4));
    }

    @Test
    public void testGetAllVehiclesGroupedByBrand_ShouldCorrectlyOrderedVehicles() {
        assertThrows(IllegalArgumentException.class, () -> this.vehicleRepository.getAllVehiclesGroupedByBrand());
        Vehicle vehicle = new Vehicle(1 + "", "BMW", "X5", "Sofia", "Blue", 400, 50000, true);
        Vehicle vehicle2 = new Vehicle(2 + "", "BMW", "X52", "Sofia2", "Blue2", 500, 61000, false);
        Vehicle vehicle3 = new Vehicle(3 + "", "Audi", "A3", "Sofia3", "Blue3", 300, 70000, false);
        Vehicle vehicle4 = new Vehicle(4 + "", "Audi", "A3", "Sofia3", "Blue3", 500, 88000, false);
        Vehicle vehicle5 = new Vehicle(5 + "", "Audi", "A3", "Sofia3", "Blue3", 500, 61000, false);

        this.vehicleRepository.addVehicleForSale(vehicle, "George");
        this.vehicleRepository.addVehicleForSale(vehicle2, "Jack");
        this.vehicleRepository.addVehicleForSale(vehicle3, "Phill");
        this.vehicleRepository.addVehicleForSale(vehicle4, "Isacc");
        this.vehicleRepository.addVehicleForSale(vehicle5, "Igor");

        Map<String, List<Vehicle>> brands = this.vehicleRepository.getAllVehiclesGroupedByBrand();


        assertEquals(2, brands.size());

        assertEquals(vehicle, brands.get("BMW").get(0));
        assertEquals(vehicle2, brands.get("BMW").get(1));
        assertEquals(vehicle5, brands.get("Audi").get(0));
        assertEquals(vehicle3, brands.get("Audi").get(1));
        assertEquals(vehicle4, brands.get("Audi").get(2));

    }

    @Test
    public void testGetVehiclesInPriceRange_ShouldCorrectlyOrderedVehicles() {
        List<Vehicle> vehicles = StreamSupport.stream(this.vehicleRepository.getVehiclesInPriceRange(61000, 88000).spliterator(), false)
                .collect(Collectors.toList());

        assertTrue(vehicles.isEmpty());

        assertThrows(IllegalArgumentException.class, () -> this.vehicleRepository.getAllVehiclesGroupedByBrand());
        Vehicle vehicle = new Vehicle(1 + "", "BMW", "X5", "Sofia", "Blue", 400, 50000, true);
        Vehicle vehicle2 = new Vehicle(2 + "", "BMW", "X52", "Sofia2", "Blue2", 500, 61000, false);
        Vehicle vehicle3 = new Vehicle(3 + "", "Audi", "A3", "Sofia3", "Blue3", 300, 70000, false);
        Vehicle vehicle4 = new Vehicle(4 + "", "Audi", "A3", "Sofia3", "Blue3", 400, 88000, false);
        Vehicle vehicle5 = new Vehicle(5 + "", "Audi", "A3", "Sofia3", "Blue3", 500, 61000, false);

        this.vehicleRepository.addVehicleForSale(vehicle, "George");
        this.vehicleRepository.addVehicleForSale(vehicle2, "Jack");
        this.vehicleRepository.addVehicleForSale(vehicle3, "Phill");
        this.vehicleRepository.addVehicleForSale(vehicle4, "Isacc");
        this.vehicleRepository.addVehicleForSale(vehicle5, "Igor");

        vehicles = StreamSupport.stream(this.vehicleRepository.getVehiclesInPriceRange(61000, 88000).spliterator(), false)
                .collect(Collectors.toList());


        assertEquals(4, vehicles.size());

        assertEquals(vehicle2, vehicles.get(0));
        assertEquals(vehicle5, vehicles.get(1));
        assertEquals(vehicle4, vehicles.get(2));
        assertEquals(vehicle3, vehicles.get(3));


    }

    @Test
    public void testBuyCheapest_With1000000Vehicles_ShouldPassQuickly() {
        int count = 1000000;

        for (int i = count, j = 0; i >= 0 && j <= count; i--, j++) {
            String sellerName = "George";

            Vehicle vehicle = new Vehicle(i + "", "BMW", "X5", "Sofia", "Blue", i * 10, i, true);

            this.vehicleRepository.addVehicleForSale(vehicle, sellerName);
        }

        long start = System.currentTimeMillis();

        this.vehicleRepository.buyCheapestFromSeller("George");

        long stop = System.currentTimeMillis();

        long elapsedTime = stop - start;

        assertTrue(elapsedTime <= 50);
    }

    @Test
    public void Test() {
        List<Boolean> bools = List.of(true, false, true, false);
        bools = bools.stream()
                .sorted((v1, v2) -> Boolean.compare(v2, v1))
                .collect(Collectors.toList());
    }
}
