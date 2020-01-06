package com.csci571.forecast;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Sharable {

    public static JSONObject currJsonObj;
    public static String currLocation;

    /* Stores data fro Current Location */
    public static JSONObject jsonObj;
    public static String temperature;
    public static String location;

    public static String searchCity;


    public static int getIconResourceId(String icon_name){
        switch(icon_name){
            case "clear-day":
                return R.drawable.clear_day_icon;

            case "clear-night":
                return R.drawable.clear_night_icon;

            case "rain":
                return R.drawable.rain_icon;

            case "snow":
                return R.drawable.snow_icon;

            case "sleet":
                return R.drawable.sleet_icon;

            case "wind":
                return R.drawable.wind_icon;

            case "fog":
                return R.drawable.fog_icon;

            case "cloudy":
                return R.drawable.cloudy_icon;

            case "partly-cloudy-day":
                return R.drawable.partly_cloudy_day_icon;

            case "partly-cloudy-night":
                return R.drawable.partly_cloudy_night_icon;

            default:
                return R.drawable.cloudy_icon;
        }
    }
    public List<String> locationList;
    public Map<String,JSONObject> mMap;

    private static Sharable single_instance = null;

    private Sharable()
    {
        mMap = new HashMap<String, JSONObject>();
        locationList = new ArrayList<String>();
    }

    public static Sharable getInstance()
    {
        if (single_instance == null)
            single_instance = new Sharable();

        return single_instance;
    }

    /*public static void putLocation(String name, JSONObject jsonObj)
    {
        mMap.put(name, jsonObj);
    }

    public JSONObject getDataforLocation(String locationName) {
        if(mMap.containsKey(locationName))
        {
            return mMap.get(locationName);
        }
        return null;
    }*/
}

    //Iterator iter = mMap.entrySet().iterator();
/*
while (iter.hasNext()) {
        Map.Entry mEntry = (Map.Entry) iter.next();
        System.out.println(mEntry.getKey() + " : " + mEntry.getValue());
        }*/
