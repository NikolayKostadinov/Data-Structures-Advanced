package core;

import models.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

public class VehicleRepositoryImpl implements VehicleRepository {

    private final Map<String, Vehicle> data;
    private final Map<String, Set<Vehicle>> bySeller;
    private final Map<String, Set<Vehicle>> bySellerAndPrice;

    public VehicleRepositoryImpl() {
        this.data = new HashMap<>();
        this.bySeller = new TreeMap<>();
        this.bySellerAndPrice = new TreeMap<>();
    }


    public Map<String, Set<Vehicle>> getBySeller() {
        return bySeller;
    }

    public Map<String, Set<Vehicle>> getBySellerAndPrice() {
        return bySellerAndPrice;
    }

    @Override
    public void addVehicleForSale(Vehicle vehicle, String sellerName) {
        this.data.put(vehicle.getId(), vehicle);
        this.bySeller.putIfAbsent(sellerName, new LinkedHashSet<>());
        this.bySeller.get(sellerName).add(vehicle);
        this.bySellerAndPrice.putIfAbsent(sellerName, new TreeSet<>());
        this.bySellerAndPrice.get(sellerName).add(vehicle);
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public boolean contains(Vehicle vehicle) {

        return this.data.containsKey(vehicle.getId());
    }

    @Override
    public Iterable<Vehicle> getVehicles(List<String> keywords) {
        return this.data
                .values()
                .stream()
                .filter(v -> keywords.contains(v.getBrand())
                        || keywords.contains(v.getModel())
                        || keywords.contains(v.getLocation())
                        || keywords.contains(v.getColor())
                )
                .sorted((v1, v2) -> {
                    int compareVip = Boolean.compare(v2.getIsVIP(), v1.getIsVIP());
                    if (compareVip == 0) {
                        return Double.compare(v1.getPrice(), v2.getPrice());
                    }
                    return compareVip;
                })

                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Vehicle> getVehiclesBySeller(String sellerName) {
        Collection<Vehicle> vehicles = this.bySeller.get(sellerName);
        if (vehicles == null || vehicles.isEmpty()) throw new IllegalArgumentException();
        return vehicles;
    }

    @Override
    public Iterable<Vehicle> getVehiclesInPriceRange(double lowerBound, double upperBound) {
        return this.data
                .values()
                .stream()
                .filter(v -> lowerBound <= v.getPrice() && v.getPrice() <= upperBound)
                .sorted((v1, v2) -> v2.getHorsepower() - v1.getHorsepower())
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Vehicle>> getAllVehiclesGroupedByBrand() {
        if (this.data.isEmpty()) throw new IllegalArgumentException();
        Map<String, List<Vehicle>> result = new HashMap<>();

        this.data.values()
                .stream()
                .sorted(Comparator.comparing(Vehicle::getPrice))
                .forEach(v -> {
                    result.putIfAbsent(v.getBrand(), new ArrayList<>());
                    result.get(v.getBrand()).add(v);
                });

        return result;
    }

    @Override
    public void removeVehicle(String vehicleId) {
        Vehicle removedVehicle = this.data.remove(vehicleId);
        if (removedVehicle == null) throw new IllegalArgumentException();
        for (Set<Vehicle> vehicles : bySeller.values()) {
            boolean success = vehicles.remove(removedVehicle);
            if (success) return;
        }

        for (Set<Vehicle> vehicles : bySellerAndPrice.values()) {
            boolean success = vehicles.remove(removedVehicle);
            if (success) return;
        }
    }

    @Override
    public Iterable<Vehicle> getAllVehiclesOrderedByHorsepowerDescendingThenByPriceThenBySellerName() {
        return this.bySeller
                .values()
                .stream()
                .flatMap(Collection::stream)
                .sorted((v1,v2)->{
                    int horsePwrCompare = v2.getHorsepower() - v1.getHorsepower();
                    if (horsePwrCompare == 0){
                        return Double.compare(v1.getPrice(), v2.getPrice());
                    }

                    return horsePwrCompare;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Vehicle buyCheapestFromSeller(String sellerName) {
        Set<Vehicle> vehicles = this.bySellerAndPrice.get(sellerName);
        if (vehicles == null || vehicles.isEmpty()) throw new IllegalArgumentException();
        return vehicles.iterator().next();
    }
}
