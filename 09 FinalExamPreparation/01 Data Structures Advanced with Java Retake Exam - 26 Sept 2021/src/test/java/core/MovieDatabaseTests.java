import core.*;

import models.Movie;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class MovieDatabaseTests {
    private interface InternalTest {
        void execute();
    }

    private MovieDatabase movieDatabase;

    private Movie getRandomMovie() {
        return new Movie(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                (int) Math.min(1, Math.random() * 2_000),
                Math.min(1, Math.random() * 10),
                new ArrayList<String>(IntStream.range(1,  (int) Math.min(2, Math.random() * 10)).mapToObj(x -> UUID.randomUUID().toString()).collect(Collectors.toList())));
    }

    @Before
    public void setup() {
        this.movieDatabase = new MovieDatabaseImpl();
    }

    public void performCorrectnessTesting(InternalTest[] methods) {
        Arrays.stream(methods)
                .forEach(method -> {
                    this.movieDatabase = new MovieDatabaseImpl();

                    try {
                        method.execute();
                    } catch (IllegalArgumentException ignored) { }
                });

        this.movieDatabase = new MovieDatabaseImpl();
    }

    // Correctness Tests

    @Test
    public void testAddMovie_WithCorrectData_ShouldSuccessfullyAddMovie() {
        this.movieDatabase.addMovie(this.getRandomMovie());
        this.movieDatabase.addMovie(this.getRandomMovie());

        assertEquals(2, this.movieDatabase.size());
    }

    @Test
    public void testContains_WithExistentMovie_ShouldReturnTrue() {
        Movie randomMovie = this.getRandomMovie();

        this.movieDatabase.addMovie(randomMovie);

        assertTrue(this.movieDatabase.contains(randomMovie));
    }

    @Test
    public void testContains_WithNonexistentMovie_ShouldReturnFalse() {
        Movie randomMovie = this.getRandomMovie();

        this.movieDatabase.addMovie(randomMovie);

        assertFalse(this.movieDatabase.contains(this.getRandomMovie()));
    }

    @Test
    public void testCount_With5Movies_ShouldReturn5() {
        this.movieDatabase.addMovie(this.getRandomMovie());
        this.movieDatabase.addMovie(this.getRandomMovie());
        this.movieDatabase.addMovie(this.getRandomMovie());
        this.movieDatabase.addMovie(this.getRandomMovie());
        this.movieDatabase.addMovie(this.getRandomMovie());

        assertEquals(5, this.movieDatabase.size());
    }

    @Test
    public void testCount_WithEmpty_ShouldReturnZero()
    {
        assertEquals(0, this.movieDatabase.size());
    }

    @Test
    public void testGetMoviesOrderedByMultiCriteria_WithCorrectData_ShouldReturnCorrectResults() {
        Movie Movie = new Movie("asd", "bsd", 2010, 5000, List.of("Pesho", "Gosho"));
        Movie Movie2 = new Movie("dsd", "esd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie3 = new Movie("hsd", "isd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie4 = new Movie("ksd", "lsd", 2013, 4500, List.of("Pesho", "Gosho"));
        Movie Movie5 = new Movie("nsd", "osd", 2014, 4500, List.of("Pesho", "Gosho"));

        this.movieDatabase.addMovie(Movie);
        this.movieDatabase.addMovie(Movie2);
        this.movieDatabase.addMovie(Movie3);
        this.movieDatabase.addMovie(Movie4);
        this.movieDatabase.addMovie(Movie5);

        List<Movie> list =
                StreamSupport.stream(this.movieDatabase.getAllMoviesOrderedByActorPopularityThenByRatingThenByYear().spliterator(), false)
                        .collect(Collectors.toList());

        assertEquals(5, list.size());
        assertEquals(Movie, list.get(0));
        assertEquals(Movie5, list.get(1));
        assertEquals(Movie4, list.get(2));
        assertEquals(Movie2, list.get(3));
        assertEquals(Movie3, list.get(4));
    }

    @Test
    public void testGetMoviesByActor_WithCorrectData_ShouldReturnCorrectResults() {
        Movie Movie = new Movie("asd", "bsd", 2010, 5000, List.of("Pesho", "Gosho"));
        Movie Movie2 = new Movie("dsd", "esd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie3 = new Movie("hsd", "isd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie4 = new Movie("ksd", "lsd", 2013, 4500, List.of("Pesho", "Gosho"));
        Movie Movie5 = new Movie("nsd", "osd", 2014, 4500, List.of("Pesho", "Gosho"));

        this.movieDatabase.addMovie(Movie);
        this.movieDatabase.addMovie(Movie2);
        this.movieDatabase.addMovie(Movie3);
        this.movieDatabase.addMovie(Movie4);
        this.movieDatabase.addMovie(Movie5);

        List<Movie> list =
                StreamSupport.stream(this.movieDatabase.getMoviesByActor("Pesho").spliterator(), false)
                        .collect(Collectors.toList());

        assertEquals(3, list.size());
        assertEquals(Movie, list.get(0));
        assertEquals(Movie5, list.get(1));
        assertEquals(Movie4, list.get(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMoviesByActor_WithInCorrectData_ShouldThrowException() {
        Movie Movie = new Movie("asd", "bsd", 2010, 5000, List.of("Pesho", "Gosho"));
        Movie Movie2 = new Movie("dsd", "esd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie3 = new Movie("hsd", "isd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie4 = new Movie("ksd", "lsd", 2013, 4500, List.of("Pesho", "Gosho"));
        Movie Movie5 = new Movie("nsd", "osd", 2014, 4500, List.of("Pesho", "Gosho"));

        this.movieDatabase.addMovie(Movie);
        this.movieDatabase.addMovie(Movie2);
        this.movieDatabase.addMovie(Movie3);
        this.movieDatabase.addMovie(Movie4);
        this.movieDatabase.addMovie(Movie5);

        this.movieDatabase.getMoviesByActor("Ivan");
    }

    @Test
    public void testGetMoviesByActors_WithCorrectData_ShouldReturnCorrectResults() {
        Movie Movie = new Movie("asd", "bsd", 2010, 5000, List.of("Pesho", "Gosho"));
        Movie Movie2 = new Movie("dsd", "esd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie3 = new Movie("hsd", "isd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie4 = new Movie("ksd", "lsd", 2013, 4500, List.of("Pesho", "Gosho"));
        Movie Movie5 = new Movie("ssd", "osd", 2014, 4500, List.of("Pesho", "Gosho"));
        Movie Movie6 = new Movie("csd", "osd", 2013, 4500, List.of("Ivan", "Jelio"));

        this.movieDatabase.addMovie(Movie);
        this.movieDatabase.addMovie(Movie2);
        this.movieDatabase.addMovie(Movie3);
        this.movieDatabase.addMovie(Movie4);
        this.movieDatabase.addMovie(Movie5);
        this.movieDatabase.addMovie(Movie6);

        List<Movie> list =
                StreamSupport.stream(this.movieDatabase.getMoviesByActors(List.of("Pesho","Gosho")).spliterator(),
                                false)
                        .collect(Collectors.toList());

        assertEquals(3, list.size());
        assertEquals(Movie, list.get(0));
        assertEquals(Movie5, list.get(1));
        assertEquals(Movie4, list.get(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMoviesByActors_WithInCorrectData_ShouldThrowException() {
        Movie Movie = new Movie("asd", "bsd", 2010, 5000, List.of("Pesho", "Gosho"));
        Movie Movie2 = new Movie("dsd", "esd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie3 = new Movie("hsd", "isd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie4 = new Movie("ksd", "lsd", 2013, 4500, List.of("Pesho", "Gosho"));
        Movie Movie5 = new Movie("nsd", "osd", 2014, 4500, List.of("Pesho", "Gosho"));

        this.movieDatabase.addMovie(Movie);
        this.movieDatabase.addMovie(Movie2);
        this.movieDatabase.addMovie(Movie3);
        this.movieDatabase.addMovie(Movie4);
        this.movieDatabase.addMovie(Movie5);

        this.movieDatabase.getMoviesByActors(List.of("Ivan","Stefan"));
    }

    @Test
    public void testGetMoviesByYear_WithCorrectData_ShouldReturnCorrectResults() {
        Movie Movie = new Movie("asd", "bsd", 2010, 5000, List.of("Pesho", "Gosho"));
        Movie Movie2 = new Movie("dsd", "esd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie3 = new Movie("hsd", "isd", 2012, 4001, List.of("Sasho", "Tosho"));
        Movie Movie4 = new Movie("ksd", "lsd", 2013, 4500, List.of("Pesho", "Gosho"));
        Movie Movie5 = new Movie("ssd", "osd", 2014, 4500, List.of("Pesho", "Gosho"));
        Movie Movie6 = new Movie("csd", "osd", 2013, 4500, List.of("Ivan", "Jelio"));

        this.movieDatabase.addMovie(Movie);
        this.movieDatabase.addMovie(Movie2);
        this.movieDatabase.addMovie(Movie3);
        this.movieDatabase.addMovie(Movie4);
        this.movieDatabase.addMovie(Movie5);
        this.movieDatabase.addMovie(Movie6);

        List<Movie> list =
                StreamSupport.stream(this.movieDatabase.getMoviesByYear(2012).spliterator(),
                                false)
                        .collect(Collectors.toList());

        assertEquals(2, list.size());
        assertEquals(Movie3, list.get(0));
        assertEquals(Movie2, list.get(1));
    }

    @Test
    public void testGetMoviesByActors_WithInCorrectData_ShouldReturnEmptyCollection() {
        Movie Movie = new Movie("asd", "bsd", 2010, 5000, List.of("Pesho", "Gosho"));
        Movie Movie2 = new Movie("dsd", "esd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie3 = new Movie("hsd", "isd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie4 = new Movie("ksd", "lsd", 2013, 4500, List.of("Pesho", "Gosho"));
        Movie Movie5 = new Movie("nsd", "osd", 2014, 4500, List.of("Pesho", "Gosho"));

        this.movieDatabase.addMovie(Movie);
        this.movieDatabase.addMovie(Movie2);
        this.movieDatabase.addMovie(Movie3);
        this.movieDatabase.addMovie(Movie4);
        this.movieDatabase.addMovie(Movie5);

        List<Movie> list =
                StreamSupport.stream(this.movieDatabase.getMoviesByYear(2022).spliterator(),
                                false)
                        .collect(Collectors.toList());

        assertTrue(list.isEmpty());
    }

    @Test
    public void testGetMoviesInRatingRange_WithCorrectData_ShouldReturnCorrectResults() {
        Movie Movie = new Movie("asd", "bsd", 2010, 5000, List.of("Pesho", "Gosho"));
        Movie Movie2 = new Movie("dsd", "esd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie3 = new Movie("hsd", "isd", 2012, 4001, List.of("Sasho", "Tosho"));
        Movie Movie4 = new Movie("ksd", "lsd", 2013, 4500, List.of("Pesho", "Gosho"));
        Movie Movie5 = new Movie("ssd", "osd", 2014, 4500, List.of("Pesho", "Gosho"));
        Movie Movie6 = new Movie("csd", "osd", 2013, 4500, List.of("Ivan", "Jelio"));

        this.movieDatabase.addMovie(Movie);
        this.movieDatabase.addMovie(Movie2);
        this.movieDatabase.addMovie(Movie3);
        this.movieDatabase.addMovie(Movie4);
        this.movieDatabase.addMovie(Movie5);
        this.movieDatabase.addMovie(Movie6);

        List<Movie> list =
                StreamSupport.stream(this.movieDatabase.getMoviesInRatingRange(4000,4500).spliterator(),
                                false)
                        .collect(Collectors.toList());

        assertEquals(5, list.size());
        assertEquals(Movie4, list.get(0));
        assertEquals(Movie5, list.get(1));
        assertEquals(Movie6, list.get(2));
        assertEquals(Movie3, list.get(3));
        assertEquals(Movie2, list.get(4));
    }

    @Test
    public void testGetMoviesInRatingRange_WithInCorrectData_ShouldReturnEmptyCollection() {
        Movie Movie = new Movie("asd", "bsd", 2010, 5000, List.of("Pesho", "Gosho"));
        Movie Movie2 = new Movie("dsd", "esd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie3 = new Movie("hsd", "isd", 2012, 4000, List.of("Sasho", "Tosho"));
        Movie Movie4 = new Movie("ksd", "lsd", 2013, 4500, List.of("Pesho", "Gosho"));
        Movie Movie5 = new Movie("nsd", "osd", 2014, 4500, List.of("Pesho", "Gosho"));

        this.movieDatabase.addMovie(Movie);
        this.movieDatabase.addMovie(Movie2);
        this.movieDatabase.addMovie(Movie3);
        this.movieDatabase.addMovie(Movie4);
        this.movieDatabase.addMovie(Movie5);

        List<Movie> list =
                StreamSupport.stream(this.movieDatabase.getMoviesInRatingRange(1000,2000).spliterator(),
                                false)
                        .collect(Collectors.toList());

        assertTrue(list.isEmpty());
    }
}
