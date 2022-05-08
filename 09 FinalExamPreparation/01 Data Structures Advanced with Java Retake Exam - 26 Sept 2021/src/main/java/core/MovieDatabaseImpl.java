package core;

import models.Movie;

import java.util.*;
import java.util.stream.Collectors;

public class MovieDatabaseImpl implements MovieDatabase {
    private Map<String, Movie> data;
    private Map<String, Integer> actors;


    public MovieDatabaseImpl() {
        this.data = new LinkedHashMap<>();
        this.actors = new HashMap<>();
    }

    @Override
    public void addMovie(Movie movie) {
        this.data.put(movie.getId(), movie);
        movie.getActors().forEach(a -> {
            this.actors.putIfAbsent(a, 0);
            this.actors.put(a, this.actors.get(a) + 1);
        });
    }

    @Override
    public void removeMovie(String movieId) {
        Movie removed = this.data.remove(movieId);
        if (removed == null) throw new IllegalArgumentException();
        removed.getActors()
                .forEach(a -> this.actors.put(a, this.actors.get(a) - 1));
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public boolean contains(Movie movie) {
        return this.data.containsKey(movie.getId());
    }

    @Override
    public Iterable<Movie> getMoviesByActor(String actorName) {
        // todo: create index By Author
        List<Movie> movies = this.data.values()
                .stream()
                .filter(m -> m.getActors().contains(actorName))
                .sorted((m1, m2) -> {
                    int ratingCompare = Double.compare(m2.getRating(), m1.getRating());
                    if (ratingCompare == 0) {
                        return Integer.compare(m2.getReleaseYear(), m1.getReleaseYear());
                    }

                    return ratingCompare;
                })
                .collect(Collectors.toList());

        if (movies.isEmpty()) throw new IllegalArgumentException();
        return movies;
    }

    @Override
    public Iterable<Movie> getMoviesByActors(List<String> actors) {
        List<Movie> movies = this.data.values()
                .stream()
                .filter(m -> m.getActors().containsAll(actors))
                .sorted((m1, m2) -> {
                    int ratingCompare = Double.compare(m2.getRating(), m1.getRating());
                    if (ratingCompare == 0) {
                        return Integer.compare(m2.getReleaseYear(), m1.getReleaseYear());
                    }

                    return ratingCompare;
                })
                .collect(Collectors.toList());

        if (movies.isEmpty()) throw new IllegalArgumentException();
        return movies;
    }

    @Override
    public Iterable<Movie> getMoviesByYear(Integer releaseYear) {
        return this.data
                .values()
                .stream()
                .filter(m -> m.getReleaseYear() == releaseYear)
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Movie> getMoviesInRatingRange(double lowerBound, double upperBound) {
        return this.data.values()
                .stream()
                .filter(m -> lowerBound <= m.getRating() && m.getRating() <= upperBound)
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Movie> getAllMoviesOrderedByActorPopularityThenByRatingThenByYear() {
        return this.data
                .values()
                .stream()
                .sorted((m1, m2) -> {
                    int compare = Integer.compare(getMovieActorsPopularity(m2), getMovieActorsPopularity(m1));
                    compare = (compare == 0) ? Double.compare(m2.getRating(), m1.getRating()) : compare;
                    compare = (compare == 0) ? Integer.compare(m2.getReleaseYear(), m1.getReleaseYear()) : compare;
                    return compare;
                }).collect(Collectors.toList());
    }

    private int getMovieActorsPopularity(Movie movie) {
        return movie.getActors()
                .stream()
                .mapToInt(a -> this.actors.get(a))
                .sum();
    }
}
