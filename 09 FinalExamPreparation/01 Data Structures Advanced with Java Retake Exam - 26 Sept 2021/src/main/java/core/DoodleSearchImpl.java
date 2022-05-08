package core;

import models.Doodle;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DoodleSearchImpl implements DoodleSearch {
    private final Map<String, Doodle> data;
    private final Map<String, Doodle> byTitle;

    public DoodleSearchImpl() {
        this.data = new LinkedHashMap<>();
        this.byTitle = new HashMap<>();
    }

    @Override
    public void addDoodle(Doodle doodle) {
        this.data.put(doodle.getId(), doodle);
        this.byTitle.put(doodle.getTitle(), doodle);
    }

    @Override
    public void removeDoodle(String doodleId) {
        Doodle removed = this.data.remove(doodleId);
        if (removed == null) throw new IllegalArgumentException();

        this.byTitle.remove(removed.getTitle());
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public boolean contains(Doodle doodle) {
        return this.data.containsKey(doodle.getId());
    }

    @Override
    public Doodle getDoodle(String id) {
        Doodle doodle = this.data.get(id);
        if (doodle == null) throw new IllegalArgumentException();
        return doodle;
    }

    @Override
    public double getTotalRevenueFromDoodleAds() {
        return this.data.values()
                .stream()
                .filter(Doodle::getIsAd)
                .mapToDouble(d -> d.getRevenue() * d.getVisits())
                .sum();
    }

    @Override
    public void visitDoodle(String title) {
        Doodle doodle = this.byTitle.get(title);
        if (doodle == null) throw new IllegalArgumentException();
        doodle.incrementVisits();
    }

    @Override
    public Iterable<Doodle> searchDoodles(String searchQuery) {
        return this.data.values()
                .stream()
                .filter(d -> search(d, searchQuery) > -1)
                .sorted((d1, d2) -> {
                    int adsCompare = Boolean.compare(d2.getIsAd(), d1.getIsAd());
                    if (adsCompare == 0) {
                        int relevanceCompare = Integer.compare(search(d1, searchQuery), search(d2, searchQuery));
                        if (relevanceCompare == 0) {
                            return Integer.compare(d2.getVisits(), d1.getVisits());
                        }
                        return relevanceCompare;
                    }
                    return adsCompare;
                })
                .collect(Collectors.toList());
    }

    private int search(Doodle d, String searchQuery) {
        return d.getTitle().indexOf(searchQuery);
    }


    @Override
    public Iterable<Doodle> getDoodleAds() {
        return this.data.values()
                .stream()
                .filter(Doodle::getIsAd)
                .sorted((d1, d2) -> {
                    int revenueCompare = Double.compare(d2.getRevenue(), d1.getRevenue());
                    if (revenueCompare == 0) {
                        return Integer.compare(d2.getVisits(), d1.getVisits());
                    }
                    return revenueCompare;
                }).collect(Collectors.toList());
    }

    @Override
    public Iterable<Doodle> getTop3DoodlesByRevenueThenByVisits() {
        return this.data.values()
                .stream()
                .sorted((d1, d2) -> {
                    int revenueCompare = Double.compare(d2.getRevenue(), d1.getRevenue());
                    if (revenueCompare == 0) {
                        return Integer.compare(d2.getVisits(), d1.getVisits());
                    }
                    return revenueCompare;
                })
                .limit(3)
                .collect(Collectors.toList());
    }
}
