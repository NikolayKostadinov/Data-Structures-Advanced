package core;

import models.Planet;
import models.Star;

import java.util.Map;
import java.util.Set;

public interface Galaxy {
    void add(Star star);

    void add(Planet planet, Star star);

    boolean contains(Planet planet);

    boolean contains(Star star);

    Star getStar(int id);

    Planet getPlanet(int id);

    Star removeStar(int id);

    Planet removePlanet(int id);

    int countObjects();

    Iterable<Planet> getPlanetsByStar(Star star);

    Iterable<Star> getStars();

    Iterable<Star> getStarsInOrderOfDiscovery();

    Iterable<Planet> getAllPlanetsByMass();

    Iterable<Planet> getAllPlanetsByDistanceFromStar(Star star);

    Map<Star, Set<Planet>> getStarsAndPlanetsByOrderOfStarDiscoveryAndPlanetDistanceFromStarThenByPlanetMass();
}
