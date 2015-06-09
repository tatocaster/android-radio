package me.tatocaster.radiostreamtest.model;

/**
 * Created by tatocaster on 6/9/2015.
 */
public class CurrentTrackInfo {
    private String artistName;
    private String artistImageURL;


    public CurrentTrackInfo() {

    }

    public String getArtistImageURL() {
        return artistImageURL;
    }

    public void setArtistImageURL(String artistImageURL) {
        this.artistImageURL = artistImageURL;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
