package me.tatocaster.radiostreamtest;

import android.content.Context;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.tatocaster.radiostreamtest.interfaces.IArtistInfoReceiver;
import me.tatocaster.radiostreamtest.interfaces.ICurrentTrackReceiver;
import me.tatocaster.radiostreamtest.interfaces.IStationPLSReceiver;
import me.tatocaster.radiostreamtest.interfaces.ITopStationReceiver;
import me.tatocaster.radiostreamtest.model.CurrentTrackInfo;
import me.tatocaster.radiostreamtest.model.Station;
import me.tatocaster.radiostreamtest.network.VolleyClient;

/**
 * Created by tatocaster on 6/5/2015.
 */
public class RadioDataManager {

    Context mContext;
    private String streamingURL = "";
    List<Station> stationsList = new ArrayList<>();

    public RadioDataManager(Context context) {
        this.mContext = context;
    }

    /**
     * get top stations. default is 500
     *
     * @param listener  interface
     * @param genreName , if genre exists fetch only for this genre
     */
    public void getTopStations(final ITopStationReceiver listener, String genreName) {

        if (!genreName.equals("")) {
            VolleyClient.getInstance(mContext).getStationsByGenre(
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String responseString) {
                            stationsList = parseTopStations(responseString, Constants.STATIONS_BY_GENRE_LIMIT);
                            listener.onTopStationsReceived(stationsList);
                        }
                    }, null, genreName
            );
        } else {
            VolleyClient.getInstance(mContext).getTopStations(

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String responseString) {
                            stationsList = parseTopStations(responseString, 0);
                            listener.onTopStationsReceived(stationsList);
                        }
                    }, null
            );
        }
    }

    /**
     * get single PLS file for single station
     *
     * @param listener
     * @param stationID
     */
    public void getStationPLS(final IStationPLSReceiver listener, int stationID, final Boolean PLSFile) {
        VolleyClient.getInstance(mContext).checkoutPLS(
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        if (PLSFile) {
                            listener.onStationPLSReceived(parsePLSFiles(responseString));
                        } else {
                            listener.onStationPLSReceived(parseM3UFiles(responseString));
                        }
                        listener.onStationPLSReceived(parseM3UFiles(responseString));
                    }

                },
                null, stationID, PLSFile
        );
    }


    /**
     * get current track
     *
     * @param listener
     * @param stationID
     */
    public void getCurrentTrackInfo(final ICurrentTrackReceiver listener, int stationID) {
        VolleyClient.getInstance(mContext).getCurrentTrack(
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        final CurrentTrackInfo trackInfoObj = new CurrentTrackInfo();
                        String currentTrack = parseCurrentTrack(responseString);
                        if (!currentTrack.equals("")) {
                            final String artist = getArtistFromCurrentTrack(currentTrack);
                            if (artist.equals("")) {
                                trackInfoObj.setArtistName(artist);
                                trackInfoObj.setArtistImageURL("");
                                listener.onCurrentTrackReceived(trackInfoObj);
                            } else {
                                artistGetImage(new IArtistInfoReceiver() {
                                    @Override
                                    public void onArtistInfoReceived(String imageURL) {
                                        trackInfoObj.setArtistImageURL(imageURL);
                                        trackInfoObj.setArtistName(artist);
                                        listener.onCurrentTrackReceived(trackInfoObj);
                                    }
                                }, artist);
                            }
                        }
                    }

                }

                ,
                null, stationID
        );
    }


    /**
     * @param artist
     * @return
     */
    private void artistGetImage(final IArtistInfoReceiver listener, String artist) {
        VolleyClient.getInstance(mContext).artistGetImage(
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {

                        listener.onArtistInfoReceived(parseImageURL(responseString));
                    }
                },
                null, artist
        );
    }


    /**
     * @param response
     * @return
     */
    private String parseImageURL(String response) {
        String imageURL = "";

        try {
            JSONObject responseJSONObject = new JSONObject(response);
            JSONObject artistObj = responseJSONObject.getJSONObject("artist");
            JSONArray imagesArr = artistObj.getJSONArray("image");
            for (int i = 0; i < imagesArr.length(); i++) {
                JSONObject imageObj = imagesArr.getJSONObject(i);
                if (imageObj.getString("size").equals("extralarge")) {
                    imageURL = imageObj.getString("#text");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imageURL;
    }

    private String getArtistFromCurrentTrack(String currentTrack) {
        if (currentTrack.isEmpty()) {
            return "";
        }
        String[] parts = currentTrack.split("-");
        if (parts[0] == null) {
            return parts[0];
        }
        return parts[0];
    }

    /**
     * @param response
     * @return
     */
    private String parseCurrentTrack(String response) {
        String currentTrack = "";

        try {
            JSONObject responseJSONObject = new JSONObject(response);
            JSONObject stationObj = responseJSONObject.getJSONObject("Station");
            currentTrack = stationObj.getString("CurrentTrack");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return currentTrack;
    }


    /**
     * parse Station M3U file
     *
     * @param response
     * @return
     */
    private static List<String> parseM3UFiles(String response) {

        List<String> urlList = new ArrayList<>();
        String[] responseArray = response.split("\\r?\\n");
        if (responseArray.length <= 2) {
            return urlList;
        } else {
            for (String currentStr : responseArray) {
                if (!currentStr.equals("")) {
                    try {
                        URL url = new URL(currentStr);
                    } catch (MalformedURLException e) {
                        continue;
                    }
                    /*if(Character.isDigit(currentStr.charAt(currentStr.length()-1))){
                        urlList.add(currentStr + "/;");
                    }else{
                        urlList.add(currentStr);
                    }*/
                    urlList.add(currentStr + "/;");
                }
            }
            return urlList;
        }
    }

    /**
     * parse top stations response,
     *
     * @param responseString
     * @return
     */
    private List<Station> parseTopStations(String responseString, int limit) {
        List<Station> parsedData = new ArrayList<>();
        try {
            JSONArray responseJsonArray = new JSONArray(responseString);
            limit = limit != 0 ? limit : responseJsonArray.length();
            for (int i = 0; i < limit; i++) {
                JSONObject stationsJsonObj = (JSONObject) responseJsonArray.get(i);
                // creating java object for all json object and set fields
                Station station = new Station();
                station.setStationId(Integer.valueOf(stationsJsonObj.getString("ID")));
                station.setBitrate(Integer.valueOf(stationsJsonObj.getString("Bitrate")));
                station.setStationName(stationsJsonObj.getString("Name"));
                station.setGenre(stationsJsonObj.getString("Genre"));
                station.setNowPlaying(stationsJsonObj.getString("CurrentTrack"));
                parsedData.add(station);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parsedData;
    }


    /**
     * parse Station PLS file
     *
     * @param response
     * @return
     */
    private static List<String> parsePLSFiles(String response) {
        List<String> urlList = new ArrayList<>();
        String[] responseArray = response.split("\\n");
        for (String kvPair : responseArray) {
            if (!kvPair.contains("=")) {
                continue;
            }
            String[] kv = kvPair.split("=");
            try {
                URL url = new URL(kv[1]);
            } catch (MalformedURLException e) {
                continue;
            }
/*            if(Character.isDigit(kv[1].charAt(kv[1].length() - 1))) {
                urlList.add(kv[1] + "/;");
            }else{
                urlList.add(kv[1]);
            }*/
            urlList.add(kv[1] + "/;");
        }
        return urlList;
    }

}
