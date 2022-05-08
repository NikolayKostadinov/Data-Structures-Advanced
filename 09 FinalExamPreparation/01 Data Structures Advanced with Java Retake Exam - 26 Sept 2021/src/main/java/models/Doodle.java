package models;

import java.util.Objects;

public class Doodle {
    private String id;

    private String title;

    private int visits;

    private boolean isAd;

    private double revenue;

    public Doodle(String id, String title, int visits, boolean isAd, double revenue) {
        this.id = id;
        this.title = title;
        this.visits = visits;
        this.isAd = isAd;
        this.revenue = revenue;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVisits() {
        return this.visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public boolean getIsAd() {
        return this.isAd;
    }

    public void setIsAd(boolean ad) {
        this.isAd = ad;
    }

    public double getRevenue() {
        return this.revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public void incrementVisits() {
        this.visits++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doodle)) return false;
        Doodle doodle = (Doodle) o;
        return getId().equals(doodle.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Doodle{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", visits=" + visits +
                ", isAd=" + isAd +
                ", revenue=" + revenue +
                '}';
    }
}
