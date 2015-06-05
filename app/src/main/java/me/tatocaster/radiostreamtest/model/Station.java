package me.tatocaster.radiostreamtest.model;

/**
 * Created by tatocaster on 6/5/2015.
 */
public class Station {
    private int stationId;
    private String stationName;
    private String genre;
    private int bitrate;

    public Station() {

    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }
}
