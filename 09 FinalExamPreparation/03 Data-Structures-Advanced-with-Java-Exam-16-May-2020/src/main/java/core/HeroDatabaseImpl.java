package core;

import models.Hero;
import models.HeroType;

public class HeroDatabaseImpl implements HeroDatabase {

    @Override
    public void addHero(Hero hero) {

    }

    @Override
    public boolean contains(Hero hero) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Hero getHero(String name) {
        return null;
    }

    @Override
    public Hero remove(String name) {
        return null;
    }

    @Override
    public Iterable<Hero> removeAllByType(HeroType type) {
        return null;
    }

    @Override
    public void levelUp(String name) {

    }

    @Override
    public void rename(String oldName, String newName) {

    }

    @Override
    public Iterable<Hero> getAllByType(HeroType type) {
        return null;
    }

    @Override
    public Iterable<Hero> getAllByLevel(int level) {
        return null;
    }

    @Override
    public Iterable<Hero> getInPointsRange(int lowerBound, int upperBound) {
        return null;
    }

    @Override
    public Iterable<Hero> getAllOrderedByLevelDescendingThenByName() {
        return null;
    }
}
