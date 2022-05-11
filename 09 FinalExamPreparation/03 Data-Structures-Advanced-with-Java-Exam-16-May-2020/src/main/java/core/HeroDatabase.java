package core;

import models.Hero;
import models.HeroType;

public interface HeroDatabase {
    void addHero(Hero hero);

    boolean contains(Hero hero);

    int size();

    Hero getHero(String name);

    Hero remove(String name);

    Iterable<Hero> removeAllByType(HeroType type);

    void levelUp(String name);

    void rename(String oldName, String newName);

    Iterable<Hero> getAllByType(HeroType type);

    Iterable<Hero> getAllByLevel(int level);

    Iterable<Hero> getInPointsRange(int lowerBound, int upperBound);

    Iterable<Hero> getAllOrderedByLevelDescendingThenByName();
}
