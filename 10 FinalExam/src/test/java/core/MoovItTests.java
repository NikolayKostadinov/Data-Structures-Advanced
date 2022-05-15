package core;

import models.Route;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class MoovItTests {
    private interface InternalTest {
        void execute();
    }

    private MoovIt moovIt;

    private List<String> getRandomLocationPoints() {
        int randomLength = (int) Math.max(5, Math.random() * 10);

        List<String> randomLocationPoints = new ArrayList<>();

        for (int i = 0; i < randomLength; i++) {
            randomLocationPoints.add(UUID.randomUUID().toString());
        }

        return randomLocationPoints;
    }

    private Route getRandomRoute() {
        return new Route(
                UUID.randomUUID().toString(),
                Math.min(1, Math.random() * 1_000_000),
                (int) Math.min(1, Math.random() * 1_000),
                (int) Math.min(1, Math.random() * 1_000) > 500 ? true : false,
                getRandomLocationPoints());
    }

    @Before
    public void setup() {
        this.moovIt = new MoovItImpl();
    }

    // Correctness Tests

    @Test
    public void testAddRoute_WithCorrectData_ShouldSuccessfullyAddRoute() {
        this.moovIt.addRoute(getRandomRoute());
        this.moovIt.addRoute(getRandomRoute());

        assertEquals(2, this.moovIt.size());
    }

    @Test
    public void testAddRoute_WithRepeatedData_ShouldThrowException() {
        Route route = new Route("Test1", 10D, 1, false, List.of("Sofia", "Plovdiv", "Stara Zagora", "Burgas"));
        Route route2 = new Route("Test2", 10D, 1, false, List.of("Sofia", "Pleven", "Veliko Turnovo", "Varna", "Burgas"));
        Route route3 = new Route("Test1", 100D, 1, false, List.of("Sofia", "Pleven", "Veliko Turnovo", "Varna",
                "Burgas"));

        this.moovIt.addRoute(route);
       assertThrows(IllegalArgumentException.class, ()->this.moovIt.addRoute(route));
       assertThrows(IllegalArgumentException.class, ()->this.moovIt.addRoute(route2));
       assertThrows(IllegalArgumentException.class, ()->this.moovIt.addRoute(route3));

       assertEquals(1, this.moovIt.size());
    }

    @Test
    public void testContains_WithEqualRoute_ShouldReturnTrue() {
        Route route = new Route("Test1", 10D, 1, false, List.of("Sofia", "Plovdiv", "Stara Zagora", "Burgas"));
        Route route2 = new Route("Test2", 10D, 1, false, List.of("Sofia", "Pleven", "Veliko Turnovo", "Varna", "Burgas"));

        this.moovIt.addRoute(route);

        assertTrue(this.moovIt.contains(route2));
    }

    @Test
    public void testCount_With5Routes_ShouldReturn5() {
        this.moovIt.addRoute(this.getRandomRoute());
        this.moovIt.addRoute(this.getRandomRoute());
        this.moovIt.addRoute(this.getRandomRoute());
        this.moovIt.addRoute(this.getRandomRoute());
        this.moovIt.addRoute(this.getRandomRoute());

        assertEquals(5, this.moovIt.size());
    }

    @Test
    public void testChooseRoute_WithCorrectRoute_ShouldReactToRoute() {
        Route route = this.getRandomRoute();

        Integer expected = route.getPopularity() + 1;

        this.moovIt.addRoute(route);

        this.moovIt.chooseRoute(route.getId());

        Route received = this.moovIt.getRoute(route.getId());

        assertEquals(received.getPopularity(), expected);
    }

    @Test
    public void testSearchRoutes_WithContainedPoints_ShouldReturnCorrectRoutes() {
        Route route = new Route("Test1", 10D, 200, false, List.of("Sofia", "Plovdiv", "Stara Zagora", "Burgas"));
        Route route2 = new Route("Test2", 10D, 1, false, List.of("Vidin", "Pleven", "Veliko Turnovo", "Varna", "Burgas"));
        Route route3 = new Route("Test3", 10D, 400, false, List.of("Vraca", "Plovdiv", "Stara Zagora", "Varna", "Burgas"));
        Route route4 = new Route("Test4", 500D, 500, false, List.of("Sofia", "Plovdiv", "Stara Zagora", "Varna", "Burgas"));

        this.moovIt.addRoute(route);
        this.moovIt.addRoute(route2);
        this.moovIt.addRoute(route3);
        this.moovIt.addRoute(route4);

        List<Route> routes = StreamSupport.stream(this.moovIt.searchRoutes("Plovdiv", "Burgas").spliterator(), false)
                .collect(Collectors.toList());


        assertEquals(3, routes.size());
        assertEquals(route, routes.get(0));
        assertEquals(route4, routes.get(1));
        assertEquals(route3, routes.get(2));
    }

    @Test
    public void testSearchRoutes_WithAbsentPoints_ShouldReturnEmptyCollection() {
        Route route = new Route("Test1", 10D, 200, false, List.of("Sofia", "Plovdiv", "Stara Zagora", "Burgas"));
        Route route2 = new Route("Test2", 10D, 1, false, List.of("Vidin", "Pleven", "Veliko Turnovo", "Varna", "Burgas"));
        Route route3 = new Route("Test3", 10D, 400, false, List.of("Vraca", "Plovdiv", "Stara Zagora", "Varna", "Burgas"));
        Route route4 = new Route("Test4", 500D, 500, false, List.of("Sofia", "Plovdiv", "Stara Zagora", "Varna", "Burgas"));

        this.moovIt.addRoute(route);
        this.moovIt.addRoute(route2);
        this.moovIt.addRoute(route3);
        this.moovIt.addRoute(route4);

        List<Route> routes = StreamSupport.stream(this.moovIt.searchRoutes("NewYork", "Burgas").spliterator(), false)
                .collect(Collectors.toList());
        assertTrue(routes.isEmpty());

        routes = StreamSupport.stream(this.moovIt.searchRoutes("Sofia", "Meden Rudnik").spliterator(), false)
                .collect(Collectors.toList());
        assertTrue(routes.isEmpty());

        routes = StreamSupport.stream(this.moovIt.searchRoutes("NewYork", "St. Francisco").spliterator(), false)
                .collect(Collectors.toList());
        assertTrue(routes.isEmpty());
    }

    @Test
    public void testRemoveRoute_WithCorrectData_ShouldSuccessfullyRemoveRoute() {
        Route route = new Route("Test1", 100D, 50, true, List.of("Sofia", "Plovdiv", "Stara Zagora", "Burgas"));
        Route route2 = new Route("Test2", 100D, 50, true, List.of("Vidin", "Pleven", "Burgas"));
        Route route3 = new Route("Test3", 10D, 100, true, List.of("Vraca", "Plovdiv", "Stara Zagora", "Burgas"));

        this.moovIt.addRoute(route);
        this.moovIt.addRoute(route2);
        this.moovIt.addRoute(route3);

        this.moovIt.removeRoute("Test2");

        assertFalse(this.moovIt.contains(route2));
    }

    // Performance Tests

    @Test
    public void testAddRoute_With100000Results_ShouldPassQuickly() {
        List<Route> routesToAdd = new ArrayList<>();

        int count = 100000;

        for (int i = 0; i < count; i++) {
            routesToAdd.add(new Route(i + "", i * 1000D, i * 100, false, getRandomLocationPoints()));
        }

        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            this.moovIt.addRoute(routesToAdd.get(i));
        }

        long stop = System.currentTimeMillis();

        long elapsedTime = stop - start;

        assertTrue(elapsedTime < 450);
    }

    @Test
    public void testGetFavoriteRoutes_WithContainedPoints_ShouldReturnCorrectRoutes() {

        List<Route> routes = StreamSupport.stream(this.moovIt.getFavoriteRoutes("Sofia").spliterator(), false)
                .collect(Collectors.toList());
        assertTrue(routes.isEmpty());

        Route route = new Route("Test1", 10D, 200, false, List.of("Sofia", "Plovdiv", "Stara Zagora", "Burgas"));
        Route route2 = new Route("Test2", 501D, 1, true, List.of("Vidin", "Pleven", "Veliko Turnovo","Sofia", "Varna",
                "Burgas"));
        Route route3 = new Route("Test3", 10D, 400, true, List.of("Vraca", "Plovdiv", "Stara Zagora", "Varna", "Sofia", "Burgas"));
        Route route4 = new Route("Test4", 500D, 500, true, List.of("Sofia", "Plovdiv", "Stara Zagora", "Varna",
                "Burgas"));
        Route route5 = new Route("Test5", 501D, 503, true, List.of("Plovdiv", "Sofia", "Stara Zagora", "Varna",
                "Burgas"));

        this.moovIt.addRoute(route);
        this.moovIt.addRoute(route2);
        this.moovIt.addRoute(route3);
        this.moovIt.addRoute(route4);
        this.moovIt.addRoute(route5);

        assertEquals(5, this.moovIt.size());

        routes = StreamSupport.stream(this.moovIt.getFavoriteRoutes("Sofia").spliterator(), false)
                .collect(Collectors.toList());


        assertEquals(3, routes.size());
        assertEquals(route3, routes.get(0));
        assertEquals(route5, routes.get(1));
        assertEquals(route2, routes.get(2));
    }

    @Test
    public void testGetTop5RoutesByPopularityThenByDistanceThenByCountOfLocationPoints_WithContainedPoints_ShouldReturnCorrectRoutes() {

        List<Route> routes = StreamSupport.stream(this.moovIt.getTop5RoutesByPopularityThenByDistanceThenByCountOfLocationPoints().spliterator(), false)
                .collect(Collectors.toList());
        assertTrue(routes.isEmpty());

        Route route1 = new Route("Test1", 1D, 90, false, List.of("Sofia", "Plovdiv", "Stara Zagora", "Burgas"));
        Route route2 = new Route("Test2", 2D, 100, false, List.of("Sofia", "Plovdiv", "Stara Zagora", "Burgas"));
        Route route3 = new Route("Test3", 3D, 100, false, List.of("Sofia", "Plovdiv", "Stara Zagora", "Burgas"));
        Route route4 = new Route("Test4", 3D, 100, false, List.of("Sofia", "Stara Zagora", "Stara Zagora"));
        Route route5 = new Route("Test5", 5D, 200, false, List.of("Sofia", "Plovdiv", "Stara Zagora", "Burgas"));
        Route route6 = new Route("Test6", 6D, 80, false, List.of("Sofia", "Plovdiv", "Stara Zagora", "Burgas"));



        this.moovIt.addRoute(route1);
        this.moovIt.addRoute(route2);
        this.moovIt.addRoute(route3);
        this.moovIt.addRoute(route4);
        this.moovIt.addRoute(route5);
        this.moovIt.addRoute(route6);

        assertEquals(6, this.moovIt.size());

        routes = StreamSupport.stream(this.moovIt.getTop5RoutesByPopularityThenByDistanceThenByCountOfLocationPoints().spliterator(), false)
                .collect(Collectors.toList());


        assertEquals(5, routes.size());
        assertEquals(route5, routes.get(0));
        assertEquals(route2, routes.get(1));
        assertEquals(route4, routes.get(2));
        assertEquals(route3, routes.get(3));
        assertEquals(route1, routes.get(4));
    }


}
