package me.tatocaster.radiostreamtest;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.tatocaster.radiostreamtest.interfaces.IStationPLSReceiver;
import me.tatocaster.radiostreamtest.interfaces.ITopStationReceiver;
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
     * @param listener interface
     * @param genre    , if genre exists fetch only for this genre
     */
    public void getTopStations(final ITopStationReceiver listener, String genre) {

        VolleyClient.getInstance(mContext).getTopStations(

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        stationsList = parseTopStations(responseString);
                        listener.onTopStationsReceived(stationsList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
    }

    /**
     * get single PLS file for single station
     * @param listener
     * @param stationID
     */
    public void getStationPLS(final IStationPLSReceiver listener, int stationID) {
        VolleyClient.getInstance(mContext).checkoutPLS(
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        listener.onStationPLSReceived(parsePLSFiles(responseString));
                    }

                },
                null, stationID
        );
    }


    /**
     * parse Station PLS file
     * @param response
     * @return
     */
    private String parsePLSFiles(String response){
        String[] responseArray = response.split("\n");
        for (String kvPair : responseArray) {
            if (!kvPair.contains("=")) {
                continue;
            }
            String[] kv = kvPair.split("=");
            String key = kv[0];
            String value = kv[1];
            if (key.equals("File1")) {
                streamingURL = value;
                break;
            }
        }
        return streamingURL;
    }


    /**
     * parse top stations response,
     * @param responseString
     * @return
     */
    private List<Station> parseTopStations(String responseString) {
        List<Station> parsedData = new ArrayList<>();

        try {
            JSONArray responseJsonArray = new JSONArray(responseString);
            for (int i = 0; i < responseJsonArray.length(); i++) {
                JSONObject stationsJsonObj = (JSONObject) responseJsonArray.get(i);
                // creating java object for all json object and set fields
                Station station = new Station();
                station.setStationId(Integer.valueOf(stationsJsonObj.getString("ID")));
                station.setBitrate(Integer.valueOf(stationsJsonObj.getString("Bitrate")));
                station.setStationName(stationsJsonObj.getString("Name"));
                station.setGenre(stationsJsonObj.getString("Genre"));
                parsedData.add(station);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parsedData;
    }

}
