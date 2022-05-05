package models;

import java.util.Objects;

public class Planet {
    private int id;
    private int distanceFromStar;
    private int mass;

    public Planet(int id, int distanceFromStar, int mass) {
        this.id = id;
        this.distanceFromStar = distanceFromStar;
        this.mass = mass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDistanceFromStar() {
        return distanceFromStar;
    }

    public void setDistanceFromStar(int distanceFromStar) {
        this.distanceFromStar = distanceFromStar;
    }

    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Planet planet = (Planet) o;
        return id == planet.id &&
                distanceFromStar == planet.distanceFromStar &&
                mass == planet.mass;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, distanceFromStar, mass);
    }
}
