package core;

import models.Route;

import java.util.*;
import java.util.stream.Collectors;

public class MoovItImpl implements MoovIt {

    private final Map<String, Route> routeById;
    private final Set<Route> filteredRoutes;

    public MoovItImpl() {
        this.routeById = new LinkedHashMap<>();
        this.filteredRoutes = new LinkedHashSet<>();
    }

    @Override
    public void addRoute(Route route) {
        if (this.contains(route)) throw new IllegalArgumentException();
        this.routeById.put(route.getId(), route);
        this.filteredRoutes.add(route);
    }

    @Override
    public void removeRoute(String routeId) {
        Route routeToRemove = this.routeById.remove(routeId);
        if (routeToRemove == null) throw new IllegalArgumentException();
        this.filteredRoutes.remove(routeToRemove);
    }

    @Override
    public boolean contains(Route route) {
        return this.filteredRoutes.contains(route) || this.routeById.containsKey(route.getId());
    }

    @Override
    public int size() {
        return this.filteredRoutes.size();
    }

    @Override
    public Route getRoute(String routeId) {
        Route route = this.routeById.get(routeId);
        if (route == null) throw new IllegalArgumentException();
        return route;
    }

    @Override
    public void chooseRoute(String routeId) {
        Route route = this.getRoute(routeId);
        route.setPopularity(route.getPopularity() + 1);
    }

    @Override
    public Iterable<Route> searchRoutes(String startPoint, String endPoint) {
        if (startPoint == null || endPoint == null) return new ArrayList<>();
        return this.filteredRoutes
                .stream()
                .filter(route -> {
                    int startIndex = route.getLocationPoints().indexOf(startPoint);
                    int endIndex = route.getLocationPoints().indexOf(endPoint);

                    return startIndex > 0 && endIndex > 0 && startIndex < endIndex;
                })
                .sorted((r1, r2) -> {
                    int comparison = Boolean.compare(r2.getIsFavorite(), r1.getIsFavorite());
                    if (comparison == 0) {
                        int distance1 =
                               r1.getLocationPoints().indexOf(endPoint) - r1.getLocationPoints().indexOf(startPoint);
                        int distance2 =
                               r2.getLocationPoints().indexOf(endPoint) - r2.getLocationPoints().indexOf(startPoint);
                        comparison = Integer.compare(distance1, distance2);
                    }

                    if (comparison == 0) {
                        comparison = Integer.compare(r2.getPopularity(), r1.getPopularity());
                    }

                    return comparison;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Route> getFavoriteRoutes(String destinationPoint) {
        if (destinationPoint == null) return new ArrayList<>();
        return this.filteredRoutes
                .stream()
                .filter(route -> route.getIsFavorite() && route.getLocationPoints().indexOf(destinationPoint) > 0)
                .sorted((r1, r2) -> {
                    int comparison = Double.compare(r1.getDistance(), r2.getDistance());
                    if (comparison == 0) {
                        comparison = Integer.compare(r2.getPopularity(), r1.getPopularity());
                    }
                    return comparison;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Route> getTop5RoutesByPopularityThenByDistanceThenByCountOfLocationPoints() {
        return this.filteredRoutes
                .stream()
                .sorted((r1, r2) -> {
                    int comparison = Integer.compare(r2.getPopularity(), r1.getPopularity());

                    if (comparison == 0) {
                        comparison = Double.compare(r1.getDistance(), r2.getDistance());
                    }

                    if (comparison == 0) {
                        comparison = Integer.compare(r1.getLocationPoints().size(), r2.getLocationPoints().size());
                    }
                    return comparison;
                })
                .limit(5)
                .collect(Collectors.toList());
    }
}
