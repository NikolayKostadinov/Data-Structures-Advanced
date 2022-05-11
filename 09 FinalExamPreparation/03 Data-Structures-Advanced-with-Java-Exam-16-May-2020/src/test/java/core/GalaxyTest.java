package core;

import models.Planet;
import models.Star;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class GalaxyTest {

    private Galaxy galaxy;
    private List<Star> stars;
    private List<Planet> planets;

    @Before
    public void setUp() {
        this.galaxy = new MilkyWay();
        this.stars = new ArrayList<>();
        this.planets = new ArrayList<>();
        int planetParams = 100;
        for (int i = 0; i < 10; i++) {
            Star star = new Star(10 + i, 10 + i);
            this.stars.add(star);
            this.galaxy.add(star);
            for (int j = 0; j < 5; j++) {
                Planet planet = new Planet(planetParams += 73, planetParams += 10, planetParams += 100);
                this.planets.add(planet);
                this.galaxy.add(planet, star);
            }
        }
    }

    @Test
    public void addStar() {
        Galaxy galaxy = new MilkyWay();
        assertEquals(0, galaxy.countObjects());

        Star star = new Star(10, 11);

        galaxy.add(star);
        assertTrue(galaxy.contains(star));
    }

    @Test
    public void addPlanet() {
        Galaxy galaxy = new MilkyWay();
        assertEquals(0, galaxy.countObjects());

        Star star = new Star(10, 11);

        galaxy.add(star);
        assertTrue(galaxy.contains(star));

        Planet planet = new Planet(11, 12, 13);

        galaxy.add(planet, star);

        assertTrue(galaxy.contains(planet));
    }

    @Test
    public void containsPlanet() {
        assertTrue(this.galaxy.contains(planets.get(planets.size() - 1)));
        assertFalse(this.galaxy.contains(new Planet(999999, 123, 435)));
    }

    @Test
    public void containsStar() {
        assertTrue(this.galaxy.contains(stars.get(stars.size() - 1)));
        assertFalse(this.galaxy.contains(new Star(999999,435)));
    }

    @Test
    public void getStar() {
        int id = this.stars.get(this.stars.size() / 2).getId();
        Star star = this.galaxy.getStar(id);
        assertNotNull(star);
        assertEquals(id, star.getId());
    }

    @Test
    public void getPlanet() {
        int id = this.planets.get(this.planets.size() / 2).getId();
        Planet planet = this.galaxy.getPlanet(id);
        assertNotNull(planet);
        assertEquals(id, planet.getId());
    }

    @Test
    public void removeStar() {
        int id = this.stars.get(0).getId();
        Star star = this.galaxy.removeStar(id);
        assertNotNull(star);
        assertEquals(id, star.getId());
        assertFalse(this.galaxy.contains(star));
    }

    @Test
    public void removePlanet() {
        int id = this.planets.get(0).getId();
        Planet planet = this.galaxy.removePlanet(id);
        assertNotNull(planet);
        assertEquals(id, planet.getId());
        assertFalse(this.galaxy.contains(planet));
    }

    @Test
    public void countObjects() {
        assertEquals(0, new MilkyWay().countObjects());
        assertEquals(60, this.galaxy.countObjects());
    }
}