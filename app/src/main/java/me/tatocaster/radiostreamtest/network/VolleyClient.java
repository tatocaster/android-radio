package me.tatocaster.radiostreamtest.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import me.tatocaster.radiostreamtest.Constants;

/**
 * Created by tatocaster on 2015-06-04.
 */
public class VolleyClient {

    private static VolleyClient mInstance;
    private Context context;
    private RequestQueue mRequestQueue;

    public static VolleyClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyClient(context);
        }
        return mInstance;
    }

    private VolleyClient(Context context) {
        this.context = context;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }

    public void execute(Request request) {
        getRequestQueue().add(request);
        getRequestQueue().start();
    }


    public void checkoutPLS(final Response.Listener<String> response, final Response.ErrorListener error, int stationID) {
        final String stationURL = Constants.STATION_BASE_URL + stationID;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, stationURL, response, error);
        execute(stringRequest);
    }

    // string request because volley cant request JsonArray with POST
    public void getTopStations(final Response.Listener<String> response, final Response.ErrorListener error) {
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Constants.TOP_STATIONS_URL, response, error);
        execute(jsonArrayRequest);
    }


}
