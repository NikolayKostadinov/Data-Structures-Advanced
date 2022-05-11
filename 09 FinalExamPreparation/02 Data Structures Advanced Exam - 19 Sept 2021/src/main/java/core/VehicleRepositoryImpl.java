package core;

import models.Vehicle;

import java.util.List;
import java.util.Map;

public class VehicleRepositoryImpl implements VehicleRepository {

    @Override
    public void addVehicleForSale(Vehicle vehicle, String sellerName) {

    }

    @Override
    public void removeVehicle(String vehicleId) {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean contains(Vehicle vehicle) {
        return false;
    }

    @Override
    public Iterable<Vehicle> getVehicles(List<String> keywords) {
        return null;
    }

    @Override
    public Iterable<Vehicle> getVehiclesBySeller(String sellerName) {
        return null;
    }

    @Override
    public Iterable<Vehicle> getVehiclesInPriceRange(double lowerBound, double upperBound) {
        return null;
    }

    @Override
    public Map<String, List<Vehicle>> getAllVehiclesGroupedByBrand() {
        return null;
    }

    @Override
    public Iterable<Vehicle> getAllVehiclesOrderedByHorsepowerDescendingThenByPriceThenBySellerName() {
        return null;
    }

    @Override
    public Vehicle buyCheapestFromSeller(String sellerName) {
        return null;
    }
}
