package models;

import java.util.Objects;

public class Star {
    private int id;
    private String description;
    private int luminosity;

    public Star(int id, int luminosity) {
        this(id, null, luminosity);
    }

    public Star(int id, String description, int luminosity) {
        this.id = id;
        this.description = description;
        this.luminosity = luminosity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLuminosity() {
        return luminosity;
    }

    public void setLuminosity(int luminosity) {
        this.luminosity = luminosity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Star star = (Star) o;
        return id == star.id &&
                luminosity == star.luminosity &&
                Objects.equals(description, star.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, luminosity);
    }
}
