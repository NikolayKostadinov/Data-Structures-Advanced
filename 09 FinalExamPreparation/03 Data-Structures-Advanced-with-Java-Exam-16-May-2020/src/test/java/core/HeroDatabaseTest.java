package core;

import models.Hero;
import models.HeroType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HeroDatabaseTest {

    private HeroDatabase heroDatabase;

    @Before
    public void setUp() {
        this.heroDatabase = new HeroDatabaseImpl();

        HeroType[] heroTypes = HeroType.values();

        String[] heroNames = {
                "Azazel",
                "Mephisto",
                "Mort",
                "Sheldor",
                "Thor",
                "Legolas",
                "Seth"
        };

        for (int i = 0; i < 7; i++) {
            this.heroDatabase.addHero(new Hero(heroTypes[i % heroTypes.length], i + 10, heroNames[i], i * 100));
        }
    }

    @Test
    public void addHero() {
        Hero hero = new Hero(HeroType.WARRIOR, 100, "Thor", 9000);
        HeroDatabase heroDatabase = new HeroDatabaseImpl();
        heroDatabase.addHero(hero);
        assertTrue(heroDatabase.contains(hero));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addHeroThrows() {
        this.heroDatabase.addHero(new Hero(HeroType.WARRIOR, 1, "Azazel", 100));
    }

    @Test
    public void contains() {
        Hero hero = new Hero(HeroType.WARRIOR, 100, "Thor", 9000);
        HeroDatabase heroDatabase = new HeroDatabaseImpl();
        heroDatabase.addHero(hero);
        assertTrue(heroDatabase.contains(hero));
        assertTrue(this.heroDatabase.contains(new Hero(HeroType.WARRIOR, 10, "Azazel", 100)));
        assertFalse(heroDatabase.contains(new Hero(HeroType.DRUID, 23, "Missing", 90)));
    }

    @Test
    public void size() {
        Hero hero = new Hero(HeroType.WARRIOR, 100, "Thor", 9000);
        HeroDatabase heroDatabase = new HeroDatabaseImpl();
        assertEquals(0, heroDatabase.size());
        heroDatabase.addHero(hero);
        assertEquals(1, heroDatabase.size());
        assertEquals(7, this.heroDatabase.size());
    }

    @Test
    public void getHero() {
        Hero legolas = this.heroDatabase.getHero("Legolas");
        assertNotNull(legolas);
        assertEquals("Legolas", legolas.getName());
    }

    @Test
    public void remove() {
        Hero legolas = this.heroDatabase.remove("Legolas");
        assertNotNull(legolas);
        assertEquals("Legolas", legolas.getName());
        assertFalse(this.heroDatabase.contains(legolas));
    }
}