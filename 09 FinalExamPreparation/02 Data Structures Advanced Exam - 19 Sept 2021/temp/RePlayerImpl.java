package core;

import models.Track;

import java.util.*;
import java.util.stream.Collectors;

public class RePlayerImpl implements RePlayer {
    //            Id    Track
    private Map<String, Track> tracksById;
    //          Album       Title   Track
    private Map<String, Map<String, Track>> albumsWithTracksByTitle;
    //                Album   Tracks by plays desc
    private SortedMap<String, SortedSet<Track>> albumWithSortedTracks;
    private Queue<Track> listeningQueue;
    //             Duration  Tracks by plays desc
    private TreeMap<Integer, TreeSet<Track>> tracksByDurationByPlays;
    //          Album       Artist  Tracks
    private Map<String, Map<String, List<Track>>> tracksByArtist;

    public RePlayerImpl() {
        this.tracksById = new HashMap<>();

        this.albumsWithTracksByTitle = new HashMap<>();
        this.albumWithSortedTracks = new TreeMap<>();
        this.listeningQueue = new ArrayDeque<>();
        this.tracksByDurationByPlays = new TreeMap<>();
        this.tracksByArtist = new HashMap<>();
    }

    @Override
    public void addTrack(Track track, String album) {
        this.tracksById.putIfAbsent(track.getId(), track);

        this.addToIndices(track, album);

    }

    private void addToIndices(Track track, String album) {
        this.albumsWithTracksByTitle.computeIfAbsent(album, k -> new HashMap<>()).put(track.getTitle(), track);

        this.albumWithSortedTracks.computeIfAbsent(album, k ->
                        new TreeSet<>(Comparator.comparing(Track::getPlays)
                                .thenComparing(Track::getDurationInSeconds).reversed()
                                .thenComparing(Track::getId)))
                .add(track);

        this.tracksByDurationByPlays.computeIfAbsent(track.getDurationInSeconds(), k ->
                        new TreeSet<>(Comparator.comparing(Track::getPlays).reversed()
                                .thenComparing(Track::getId)))
                .add(track);

        this.tracksByArtist
                .computeIfAbsent(track.getArtist(), k -> new HashMap<>())
                .computeIfAbsent(album, k -> new LinkedList<>())
                .add(track);
    }

    private void removeFromIndices(Track track, String albumName) {
        int durationInSeconds = track.getDurationInSeconds();
        String artist = track.getArtist();

        this.tracksById.remove(track.getId());
        this.listeningQueue.remove(track);
        this.albumWithSortedTracks.get(albumName).remove(track);

        TreeSet<Track> sortedTracks = this.tracksByDurationByPlays.get(durationInSeconds);

        if (sortedTracks.size() == 1) {
            this.tracksByDurationByPlays.remove(durationInSeconds);
        } else {
            sortedTracks.remove(track);
        }

        Map<String, List<Track>> albums = this.tracksByArtist.get(artist);
        List<Track> trackList = albums.get(albumName);

        if (albums.size() == 1 && trackList.size() == 1) {
            this.tracksByArtist.remove(artist);
        } else if (trackList.size() == 1) {
            albums.remove(albumName);
        } else {
            trackList.remove(track);
        }
    }

    @Override
    public void removeTrack(String trackTitle, String albumName) {
        if (trackTitle != null && albumName != null) {
            Map<String, Track> tracksByName = this.albumsWithTracksByTitle.get(albumName);

            if (tracksByName != null) {
                Track removedTrack = tracksByName.remove(trackTitle);

                if (removedTrack != null) {
                    this.removeFromIndices(removedTrack, albumName);
                    return;
                }
            }
        }

        throw new IllegalArgumentException();
    }

    @Override
    public boolean contains(Track track) {
        if (track != null) {
            return this.tracksById.containsKey(track.getId());
        }

        return false;
    }

    @Override
    public int size() {
        return this.tracksById.size();
    }

    @Override
    public Track getTrack(String title, String albumName) {
        if (title != null && albumName != null) {
            Map<String, Track> tracksByTitle = this.albumsWithTracksByTitle.get(albumName);

            if (tracksByTitle != null) {
                Track track = tracksByTitle.get(title);

                if (track != null) {
                    return track;
                }
            }
        }

        throw new IllegalArgumentException();
    }

    @Override
    public Iterable<Track> getAlbum(String albumName) {
        SortedSet<Track> sortedTracks = this.albumWithSortedTracks.get(albumName);

        if (sortedTracks != null) {
            return sortedTracks;
        }

        throw new IllegalArgumentException();
    }

    @Override
    public void addToQueue(String trackName, String albumName) {
        Track track = this.getTrack(trackName, albumName);

        this.listeningQueue.add(track);
    }

    @Override
    public Track play() {
        Track track = this.listeningQueue.poll();

        if (track == null) {
            throw new IllegalArgumentException();
        }

        track.setPlays(track.getPlays() + 1);

        return track;
    }

    @Override
    public Iterable<Track> getTracksInDurationRangeOrderedByDurationThenByPlaysDescending(int lowerBound, int upperBound) {
        Collection<TreeSet<Track>> sortedByPlays = this.tracksByDurationByPlays.subMap(lowerBound, true, upperBound, true).values();

        if (lowerBound > upperBound || sortedByPlays.isEmpty()) {
            return Collections.emptyList();
        }

        List<Track> result = new ArrayList<>();

        for (TreeSet<Track> sorted : sortedByPlays) {
            result.addAll(sorted);
        }

        return result;
    }

    @Override
    public Iterable<Track> getTracksOrderedByAlbumNameThenByPlaysDescendingThenByDurationDescending() {
        if (this.albumWithSortedTracks.isEmpty()) {
            return Collections.emptyList();
        }

        return this.albumWithSortedTracks.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Track>> getDiscography(String artistName) {
        Map<String, List<Track>> albumsWithTracks = this.tracksByArtist.get(artistName);

        if (albumsWithTracks == null || albumsWithTracks.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return albumsWithTracks;
    }
}