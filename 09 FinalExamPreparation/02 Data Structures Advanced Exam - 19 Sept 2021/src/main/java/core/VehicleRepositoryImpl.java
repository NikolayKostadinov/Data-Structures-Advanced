package core;

import models.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

public class VehicleRepositoryImpl implements VehicleRepository {

    private final Map<String, Vehicle> vehicles;

    private final Map<Vehicle, String> sellerByVehicle;

    //               Seller  Vehicles
    private final Map<String, Map<String,Vehicle>> vehiclesBySeller;

    //              Brand   Vehicles
    private final Map<String, Map<String, Vehicle>> vehiclesByBrand;

    public VehicleRepositoryImpl() {
        this.vehicles = new LinkedHashMap<>();
        this.sellerByVehicle = new LinkedHashMap<>();
        this.vehiclesBySeller = new LinkedHashMap<>();
        this.vehiclesByBrand = new LinkedHashMap<>();
    }

    @Override
    public void addVehicleForSale(Vehicle vehicle, String sellerName) {
        this.vehicles.put(vehicle.getId(), vehicle);
        this.sellerByVehicle.put(vehicle, sellerName);
        this.vehiclesBySeller
                .computeIfAbsent(sellerName, k -> new LinkedHashMap<>())
                .put(vehicle.getId(), vehicle);
        this.vehiclesByBrand
                .computeIfAbsent(vehicle.getBrand(), k -> new LinkedHashMap<>())
                .put(vehicle.getId(), vehicle);
    }

    @Override
    public void removeVehicle(String vehicleId) {
        Vehicle vehicleToRemove = this.vehicles.remove(vehicleId);
        if (vehicleToRemove == null) throw new IllegalArgumentException();
        String seller = this.sellerByVehicle.remove(vehicleToRemove);
        this.vehiclesBySeller.get(seller).remove(vehicleToRemove.getId());
        this.vehiclesByBrand.get(vehicleToRemove.getBrand()).remove(vehicleToRemove.getId());
    }

    @Override
    public int size() {
        return this.vehicles.size();
    }

    @Override
    public boolean contains(Vehicle vehicle) {
        return this.vehicles.containsKey(vehicle.getId());
    }

    @Override
    public Iterable<Vehicle> getVehicles(List<String> keywords) {
        return this.vehicles
                .values()
                .stream()
                .filter(v ->
                        keywords.contains(v.getBrand())
                                || keywords.contains(v.getModel())
                                || keywords.contains(v.getLocation())
                                || keywords.contains(v.getColor()))
                .sorted((v1, v2) -> {
                    int compareByVIP = Boolean.compare(v2.getIsVIP(), v1.getIsVIP());
                    if (compareByVIP == 0) {
                        return Double.compare(v1.getPrice(), v2.getPrice());
                    }
                    return compareByVIP;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Vehicle> getVehiclesBySeller(String sellerName) {
        Map<String, Vehicle> vehicles = this.vehiclesBySeller.get(sellerName);
        if (vehicles == null || vehicles.isEmpty()) throw new IllegalArgumentException();
        return vehicles.values();
    }

    @Override
    public Iterable<Vehicle> getVehiclesInPriceRange(double lowerBound, double upperBound) {
        return this.vehicles
                .values()
                .stream()
                .filter(v -> lowerBound <= v.getPrice() && v.getPrice() <= upperBound)
                .sorted(Comparator.comparing(Vehicle::getHorsepower).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Vehicle>> getAllVehiclesGroupedByBrand() {
        if (vehiclesByBrand.isEmpty()) throw new IllegalArgumentException();

        return vehiclesByBrand
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        (entry) -> entry.getValue()
                                .values()
                                .stream()
                                .sorted(Comparator.comparing(Vehicle::getPrice))
                                .collect(Collectors.toList())));
    }

    @Override
    public Iterable<Vehicle> getAllVehiclesOrderedByHorsepowerDescendingThenByPriceThenBySellerName() {
        return this.vehicles
                .values()
                .stream()
                .sorted((v1, v2) -> {
                    int compareResult = Integer.compare(v2.getHorsepower(), v1.getHorsepower());
                    compareResult = compareResult == 0 ? Double.compare(v1.getPrice(), v2.getPrice()) : compareResult;
                    compareResult = compareResult == 0 ? this.sellerByVehicle.get(v1).compareTo(this.sellerByVehicle.get(v2)) :
                            compareResult;
                    return compareResult;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Vehicle buyCheapestFromSeller(String sellerName) {
        Map<String, Vehicle> vehicles = this.vehiclesBySeller.get(sellerName);
        if (vehicles == null || vehicles.isEmpty()) throw new IllegalArgumentException();
        Vehicle vehicle = vehicles
                .values()
                .stream()
                .min(Comparator.comparing(Vehicle::getPrice))
                .orElseThrow(IllegalArgumentException::new);
        this.removeVehicle(vehicle.getId());
        return vehicle;
    }
}
