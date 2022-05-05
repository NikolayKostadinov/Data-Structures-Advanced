package core;

import models.Planet;
import models.Star;

import java.util.Map;
import java.util.Set;

public class MilkyWay implements Galaxy {
    @Override
    public void add(Star star) {

    }

    @Override
    public void add(Planet planet, Star star) {

    }

    @Override
    public boolean contains(Planet planet) {
        return false;
    }

    @Override
    public boolean contains(Star star) {
        return false;
    }

    @Override
    public Star getStar(int id) {
        return null;
    }

    @Override
    public Planet getPlanet(int id) {
        return null;
    }

    @Override
    public Star removeStar(int id) {
        return null;
    }

    @Override
    public Planet removePlanet(int id) {
        return null;
    }

    @Override
    public int countObjects() {
        return 0;
    }

    @Override
    public Iterable<Planet> getPlanetsByStar(Star star) {
        return null;
    }

    @Override
    public Iterable<Star> getStars() {
        return null;
    }

    @Override
    public Iterable<Star> getStarsInOrderOfDiscovery() {
        return null;
    }

    @Override
    public Iterable<Planet> getAllPlanetsByMass() {
        return null;
    }

    @Override
    public Iterable<Planet> getAllPlanetsByDistanceFromStar(Star star) {
        return null;
    }

    @Override
    public Map<Star, Set<Planet>> getStarsAndPlanetsByOrderOfStarDiscoveryAndPlanetDistanceFromStarThenByPlanetMass() {
        return null;
    }
}
