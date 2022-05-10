package models;

import java.util.Objects;

public class Track{
    private String id;

    private String title;

    private String artist;

    private int plays;

    private int durationInSeconds;

    public Track(String id, String title, String artist, int plays, int durationInSeconds) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.plays = plays;
        this.durationInSeconds = durationInSeconds;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getPlays() {
        return this.plays;
    }

    public void setPlays(int plays) {
        this.plays = plays;
    }

    public int getDurationInSeconds() {
        return this.durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Track)) return false;
        Track track = (Track) o;
        return getId().equals(track.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public void incrementPlays() {
        this.plays++;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", plays=" + plays +
                ", durationInSeconds=" + durationInSeconds +
                '}';
    }
}
