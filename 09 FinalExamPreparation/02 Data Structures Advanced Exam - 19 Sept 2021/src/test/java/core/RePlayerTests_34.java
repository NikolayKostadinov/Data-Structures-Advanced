import core.*;

import models.Track;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class RePlayerTests_34 {
    private interface InternalTest {
        void execute();
    }

    private RePlayer rePlayer;

    private Track getRandomTrack() {
        return new Track(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                (int) Math.min(1, Math.random() * 1_000_000_000),
                (int) Math.min(10, Math.random() * 10_000));
    }

    @Before
    public void setup() {
        this.rePlayer = new RePlayerImpl();
    }

    public void performCorrectnessTesting(InternalTest[] methods) {
        Arrays.stream(methods)
                .forEach(method -> {
                    this.rePlayer = new RePlayerImpl();

                    try {
                        method.execute();
                    } catch (IllegalArgumentException ignored) {
                    }
                });

        this.rePlayer = new RePlayerImpl();
    }


    private void testGetDiscography_WithNonExistentArtist_ShouldThrowException() {
        Track track = new Track("asd", "bsd", "Donald", 4000, 400);

        this.rePlayer.addTrack(track, "randomAlbum");
        this.rePlayer.removeTrack(track.getTitle(), "randomAlbum");

        this.rePlayer.getDiscography("George");
    }


    private void testGetDiscography_WithNoTracks_ShouldThrowException() {
        Track track = new Track("asd", "bsd", "Donald", 4000, 400);
        this.rePlayer.addTrack(track, "randomAlbum");
        this.rePlayer.removeTrack(track.getTitle(), "randomAlbum");

        this.rePlayer.getDiscography("Donald");
    }


    private void testGetDiscography_WithTracks_ShouldReturnCorrectlyGroupedData() {
        Track track = new Track("asd", "bsd", "Donald", 4000, 400);
        Track track2 = new Track("csd", "dsd", "Donald", 4000, 400);
        Track track3 = new Track("esd", "fsd", "Donald", 4000, 400);
        Track track4 = new Track("hsd", "isd", "Jack", 4000, 400);

        this.rePlayer.addTrack(track, "randomAlbum");
        this.rePlayer.addTrack(track2, "randomAlbum2");
        this.rePlayer.addTrack(track3, "randomAlbum2");
        this.rePlayer.addTrack(track4, "randomAlbum");

        Map<String, List<Track>> discography = this.rePlayer.getDiscography("Donald");

        assertEquals(2, discography.size());

        List<Track> randomAlbum = discography.get("randomAlbum");
        List<Track> randomAlbum2 = discography.get("randomAlbum2");

        assertEquals(1, randomAlbum.size());
        assertEquals(2, randomAlbum2.size());
        assertEquals(track, randomAlbum.get(0));
        assertEquals(track2, randomAlbum2.get(0));
        assertEquals(track3, randomAlbum2.get(1));
    }

    @Test
    public void testGetDiscography_With100000Results_ShouldPassNearlyInstantly() {
        this.performCorrectnessTesting(new InternalTest[] {
                this::testGetDiscography_WithTracks_ShouldReturnCorrectlyGroupedData,
                this::testGetDiscography_WithNoTracks_ShouldThrowException,
                this::testGetDiscography_WithNonExistentArtist_ShouldThrowException
        });

        int count = 100000;

        for (int i = count; i >= 0; i--)
        {
            Track track = new Track(i + "", "Title" + i, "GenericArtist", i * 1000, i * 100);

            String album = null;

            if (i <= 30000)
            {
                album = "randomAlbum5";
            }
            else if (i <= 50000)
            {
                album = "randomAlbum3";
            }
            else
            {
                album = "randomAlbum";
            }

            this.rePlayer.addTrack(track, album);
        }

        long start = System.currentTimeMillis();

        Map<String, List<Track>> discography = this.rePlayer.getDiscography("GenericArtist");

        long stop = System.currentTimeMillis();

        long elapsedTime = stop - start;

        assertTrue(elapsedTime <= 1);
    }
}
