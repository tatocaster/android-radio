package me.tatocaster.radiostreamtest;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.tatocaster.radiostreamtest.model.Station;
import me.tatocaster.radiostreamtest.network.VolleyClient;

/**
 * Created by tatocaster on 6/5/2015.
 */
public class RadioDataManager {

    Context mContext;
    List<Station> stationsList = new ArrayList<>();

    public RadioDataManager(Context context) {
        this.mContext = context;
    }


    public List<Station> getTopStations(String genre) {

        VolleyClient.getInstance(mContext).getTopStations(

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
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
                                stationsList.add(station);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        return stationsList;
    }


}
