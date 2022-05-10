package core;

import models.Track;

import java.util.*;
import java.util.stream.Collectors;

public class RePlayerImpl implements RePlayer {

    private final Set<Track> data;
    private final Map<String, Map<String, Track>> byAlbum;

    private final Deque<Track> queue;

    public RePlayerImpl() {
        this.data = new HashSet<>();
        this.byAlbum = new TreeMap<>();
        this.queue = new ArrayDeque<>();
    }

    @Override
    public void addTrack(Track track, String album) {
        this.data.add(track);
        this.byAlbum.putIfAbsent(album, new HashMap<>());
        this.byAlbum.get(album).put(track.getTitle(), track);
    }

    @Override
    public boolean contains(Track track) {
        return this.data.contains(track);
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public Track getTrack(String title, String albumName) {
        Map<String, Track> album = this.byAlbum.get(albumName);
        if (album == null) throw new IllegalArgumentException();
        Track track = album.get(title);
        if (track == null) throw new IllegalArgumentException();
        return track;
    }

    @Override
    public Iterable<Track> getAlbum(String albumName) {
        if (this.byAlbum.isEmpty()) throw new IllegalArgumentException();
        Map<String, Track> album = this.byAlbum.get(albumName);
        if (album == null) throw new IllegalArgumentException();
        return album.values()
                .stream()
                .sorted((t1, t2) -> Integer.compare(t2.getPlays(), t1.getPlays()))
                .collect(Collectors.toList());
    }

    @Override
    public void addToQueue(String trackName, String albumName) {
        Track track = this.getTrack(trackName, albumName);
        this.queue.offer(track);
    }

    @Override
    public Track play() {
        if (this.queue.isEmpty()) throw new IllegalArgumentException();
        Track track = this.queue.poll();
        track.incrementPlays();
        return track;
    }

    @Override
    public void removeTrack(String trackTitle, String albumName) {
        Map<String, Track> album = this.byAlbum.get(albumName);
        if (album == null) throw new IllegalArgumentException();
        Track removedTrack = album.remove(trackTitle);
        if (removedTrack == null) throw new IllegalArgumentException();
        this.data.remove(removedTrack);
        this.queue.remove(removedTrack);
    }

    @Override
    public Iterable<Track> getTracksInDurationRangeOrderedByDurationThenByPlaysDescending(int lowerBound, int upperBound) {
        if (this.data.isEmpty()) return new ArrayList<>();
        return this.data
                .stream()
                .filter(t -> lowerBound <= t.getDurationInSeconds() && t.getDurationInSeconds() <= upperBound)
                .sorted((t1, t2) -> {
                    int compareDurations = Integer.compare(t1.getDurationInSeconds(), t2.getDurationInSeconds());
                    if (compareDurations == 0) {
                        return Integer.compare(t2.getPlays(), t1.getPlays());
                    }
                    return compareDurations;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Track> getTracksOrderedByAlbumNameThenByPlaysDescendingThenByDurationDescending() {
        if (this.byAlbum.isEmpty()) return new ArrayList<>();
        return this.byAlbum
                .entrySet()
                .stream()
                .flatMap(x -> x.getValue()
                        .values()
                        .stream()
                        .sorted((t1, t2) -> {
                            int comparePlays = Integer.compare(t2.getPlays(), t1.getPlays());
                            if (comparePlays == 0) {
                                return Integer.compare(t2.getDurationInSeconds(), t1.getDurationInSeconds());
                            }
                            return comparePlays;
                        })).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Track>> getDiscography(String artistName) {
        if (this.data.isEmpty()) throw new IllegalArgumentException();
        Map<String, List<Track>> discography = new HashMap<>();
        this.byAlbum
                .entrySet()
                .stream()
                .forEach(a -> {
                    a.getValue()
                            .values()
                            .stream()
                            .forEach(t -> {
                                if (t.getArtist().equals(artistName)) {
                                    discography.putIfAbsent(a.getKey(), new ArrayList<>());
                                    discography.get(a.getKey()).add(t);
                                }
                            });
                });
        if (discography.isEmpty()) throw new IllegalArgumentException();
        return discography;
    }
}
