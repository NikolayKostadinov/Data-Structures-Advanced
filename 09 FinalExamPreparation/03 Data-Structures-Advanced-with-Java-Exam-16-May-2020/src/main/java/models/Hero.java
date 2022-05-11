package models;

public class Hero {
    private HeroType heroType;
    private int level;
    private String name;
    private int points;

    public Hero(HeroType heroType, int level, String name, int points) {
        this.heroType = heroType;
        this.level = level;
        this.name = name;
        this.points = points;
    }

    public HeroType getHeroType() {
        return heroType;
    }

    public void setHeroType(HeroType heroType) {
        this.heroType = heroType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
