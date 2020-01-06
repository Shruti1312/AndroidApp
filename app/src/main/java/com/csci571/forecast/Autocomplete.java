package com.csci571.forecast;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
/**
 * Created by MG on 04-03-2018.
 */
public class Autocomplete {
    private static Autocomplete mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    public Autocomplete(Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }
    public static synchronized Autocomplete getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Autocomplete(context);
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
    public static void make(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + query + "&types=(cities)&language=en&key=AIzaSyBFx4FaEYddgVz09pOD-hhxkmyt-gHXWqA";
        /*String url = "https://itunes.apple.com/search?term=" + query
                + "&country=US";*/
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        Autocomplete.getInstance(ctx).addToRequestQueue(stringRequest);
    }
}