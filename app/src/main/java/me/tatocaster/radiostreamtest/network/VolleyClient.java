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
    private final static String appendingUrl = "";

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


    public void checkoutPLS(final Response.Listener<String> response, final Response.ErrorListener error, String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response, error);
        execute(stringRequest);
    }

    public void getRandomStation(final Response.Listener<String> response, final Response.ErrorListener error) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.GET_RANDOM_STATION, response, error);
        execute(stringRequest);
    }


}
