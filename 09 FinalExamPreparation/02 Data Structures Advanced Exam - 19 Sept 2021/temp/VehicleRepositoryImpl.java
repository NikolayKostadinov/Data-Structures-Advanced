package core;

import models.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

public class VehicleRepositoryImpl implements VehicleRepository {
    private Map<String, Vehicle> vehiclesById;
    private Map<Vehicle, String> sellersByVehicle;
    private Map<String, Map<String, Vehicle>> vehiclesBySeller;
    private Map<String, Map<String, Vehicle>> vehiclesByBrand;

    public VehicleRepositoryImpl() {
        this.vehiclesById = new LinkedHashMap<>();
        this.sellersByVehicle = new LinkedHashMap<>();
        this.vehiclesBySeller = new LinkedHashMap<>();
        this.vehiclesByBrand = new LinkedHashMap<>();
    }

    @Override
    public void addVehicleForSale(Vehicle vehicle, String sellerName) {
        String vehicleId = vehicle.getId();

        this.vehiclesById.put(vehicleId, vehicle);
        this.sellersByVehicle.put(vehicle, sellerName);
        this.vehiclesBySeller.computeIfAbsent(sellerName, k -> new LinkedHashMap<>()).put(vehicleId, vehicle);
        this.vehiclesByBrand.computeIfAbsent(vehicle.getBrand(), k -> new LinkedHashMap<>()).put(vehicleId, vehicle);
    }

    @Override
    public void removeVehicle(String vehicleId) {
        Vehicle vehicleToRemove = this.vehiclesById.remove(vehicleId);

        if (vehicleToRemove == null) {
            throw new IllegalArgumentException();
        }

        String sellerName = this.sellersByVehicle.remove(vehicleToRemove);
        this.removeFrom(this.vehiclesBySeller, sellerName, vehicleId);

        String brand = vehicleToRemove.getBrand();
        this.removeFrom(this.vehiclesByBrand, brand, vehicleId);
    }

    private <K1, K2, V> void removeFrom(Map<K1, Map <K2, V>> collection, K1 firstKey, K2 secondKey) {
        Map<K2, V> innerMap = collection.get(firstKey);

        if (innerMap.size() == 1) {
            collection.remove(firstKey);
        } else {
            innerMap.remove(secondKey);
        }
    }

    @Override
    public int size() {
        return this.vehiclesById.size();
    }

    @Override
    public boolean contains(Vehicle vehicle) {
        return this.vehiclesById.containsKey(vehicle.getId());
    }

    @Override
    public Iterable<Vehicle> getVehicles(List<String> keywords) {
        return this.vehiclesById
                .values()
                .stream()
                .filter(vehicle -> keywords.contains(vehicle.getBrand())
                        || keywords.contains(vehicle.getModel())
                        || keywords.contains(vehicle.getLocation())
                        || keywords.contains(vehicle.getColor()))
                .sorted(Comparator.comparing(Vehicle::getIsVIP).reversed()
                        .thenComparing(Vehicle::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Vehicle> getVehiclesBySeller(String sellerName) {
        Map<String, Vehicle> vehicles = this.vehiclesBySeller.get(sellerName);

        if (vehicles == null) {
            throw new IllegalArgumentException();
        }

        return vehicles.values();
    }

    @Override
    public Iterable<Vehicle> getVehiclesInPriceRange(double lowerBound, double upperBound) {
        return this.vehiclesById
                .values()
                .stream()
                .filter(vehicle -> lowerBound <= vehicle.getPrice()
                        && vehicle.getPrice() <= upperBound)
                .sorted(Comparator.comparingInt(Vehicle::getHorsepower).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Vehicle>> getAllVehiclesGroupedByBrand() {
        if (this.vehiclesByBrand.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return this.vehiclesByBrand
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                (entry) -> entry.getValue().values()
                                        .stream()
                                        .sorted(Comparator.comparingDouble(Vehicle::getPrice))
                                        .collect(Collectors.toList())
                        )
                );
    }

    @Override
    public Iterable<Vehicle> getAllVehiclesOrderedByHorsepowerDescendingThenByPriceThenBySellerName() {
        return this.vehiclesById
                .values()
                .stream()
                .sorted(compareByHorsepowerDescThenPriceAscThenSellerNameAsc())
                .collect(Collectors.toList());
    }

    private Comparator<Vehicle> compareByHorsepowerDescThenPriceAscThenSellerNameAsc() {
        return (firstVehicle, secondVehicle) -> {
            int result = Integer.compare(secondVehicle.getHorsepower(), firstVehicle.getHorsepower());

            if (result == 0) {
                result = Double.compare(firstVehicle.getPrice(), secondVehicle.getPrice());
            }

            if (result == 0) {
                result = this.sellersByVehicle.get(firstVehicle).compareTo(this.sellersByVehicle.get(secondVehicle));
            }

            return result;
        };
    }

    @Override
    public Vehicle buyCheapestFromSeller(String sellerName) {
        Map<String, Vehicle> sellerVehicles = this.vehiclesBySeller.get(sellerName);

        if (sellerVehicles == null || sellerVehicles.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Vehicle cheapestVehicle = sellerVehicles
                .values()
                .stream()
                .min(Comparator.comparingDouble(Vehicle::getPrice))
                .orElse(null);

        this.removeVehicle(cheapestVehicle.getId());

        return cheapestVehicle;
    }
}