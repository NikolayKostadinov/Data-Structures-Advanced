package core;

import models.Track;

import java.util.*;
import java.util.stream.Collectors;

public class RePlayerImpl implements RePlayer {
    private Set<Track> tracks;
    private Map<String, Map<String, Track>> albums;
    private Map<String, Map<String, List<Track>>> artistsAlbums;
    private final ArrayDeque<Track> queue;

    public RePlayerImpl() {
        this.tracks = new HashSet<>();
        this.albums = new TreeMap<>();
        this.queue = new ArrayDeque<>();
        this.artistsAlbums = new HashMap<>();
    }

    @Override
    public void addTrack(Track track, String album) {
        this.tracks.add(track);

        this.albums.computeIfAbsent(album, k -> new HashMap<>())
                .put(track.getTitle(), track);

        this.artistsAlbums
                .computeIfAbsent(track.getArtist(), k -> new LinkedHashMap<>())
                .computeIfAbsent(album, k -> new ArrayList<>())
                .add(track);
    }

    @Override
    public void removeTrack(String trackTitle, String albumName) {
        Map<String, Track> album = this.albums.get(albumName);
        if (album != null) {
            Track trackToRemove = album.remove(trackTitle);
            if (trackToRemove != null) {
                removeIndices(trackToRemove, albumName);
                return;
            }
        }
        throw new IllegalArgumentException();
    }

    private void removeIndices(Track trackToRemove, String albumName) {
        this.tracks.remove(trackToRemove);
        this.queue.remove(trackToRemove);
        List<Track> album = this.artistsAlbums.get(trackToRemove.getArtist()).get(albumName);

        if (album.size() == 1){
            this.artistsAlbums.get(trackToRemove.getArtist()).remove(albumName);
        } else {
            album.remove(trackToRemove);
        }
    }

    @Override
    public boolean contains(Track track) {
        return this.tracks.contains(track);
    }

    @Override
    public int size() {
        return this.tracks.size();
    }

    @Override
    public Track getTrack(String title, String albumName) {
        Map<String, Track> album = this.albums.get(albumName);
        if (album != null) {
            Track track = album.get(title);
            if (track != null) return track;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Iterable<Track> getAlbum(String albumName) {
        Map<String, Track> album = this.albums.get(albumName);
        if (album != null) {
            return album.values()
                    .stream()
                    .sorted(Comparator.comparing(Track::getPlays).reversed())
                    .collect(Collectors.toList());
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void addToQueue(String trackName, String albumName) {
        Track track = this.getTrack(trackName, albumName);
        this.queue.offer(track);
    }

    @Override
    public Track play() {
        if (this.queue.isEmpty()) throw new IllegalArgumentException();
        Track truck = this.queue.poll();
        if (truck == null) throw new IllegalArgumentException();
        truck.incrementPlays();
        return truck;
    }

    @Override
    public Iterable<Track> getTracksInDurationRangeOrderedByDurationThenByPlaysDescending(int lowerBound, int upperBound) {
        return this.tracks
                .stream()
                .filter(t -> lowerBound <= t.getDurationInSeconds() && t.getDurationInSeconds() <= upperBound)
                .sorted((t1, t2) -> {
                    int compare = Integer.compare(t1.getDurationInSeconds(), t2.getDurationInSeconds());
                    if (compare == 0) {
                        return Integer.compare(t2.getPlays(), t1.getPlays());
                    }
                    return compare;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Track> getTracksOrderedByAlbumNameThenByPlaysDescendingThenByDurationDescending() {
        List<Track> result = new ArrayList<>();
        this.albums
                .entrySet()
                .stream()
                .forEach(a ->
                {
                    result.addAll(a.getValue().values()
                            .stream()
                            .sorted((t1, t2) -> {
                                int comparePlays = Integer.compare(t2.getPlays(), t1.getPlays());
                                if (comparePlays == 0) {
                                    return Integer.compare(t2.getDurationInSeconds(), t1.getDurationInSeconds());
                                }

                                return comparePlays;
                            })
                            .collect(Collectors.toList()));
                });

        return result;

    }

    @Override
    public Map<String, List<Track>> getDiscography(String artistName) {
        if (this.artistsAlbums.isEmpty() || !this.artistsAlbums.containsKey(artistName))
            throw new IllegalArgumentException();
        Map<String, List<Track>> artistAlbums = this.artistsAlbums.get(artistName);
        if (artistAlbums.isEmpty()) throw new IllegalArgumentException();
        return artistAlbums;
    }
}
