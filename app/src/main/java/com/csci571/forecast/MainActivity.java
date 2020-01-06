package com.csci571.forecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Requesting for internet", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.INTERNET},
                    1);
        } else {
            Toast.makeText(MainActivity.this, "Using Internet Connection!", Toast.LENGTH_SHORT).show();
            loadDataUsingAPI();
        }

        SharedPreferences sharedPref = this.getSharedPreferences("com.csci571.forecast", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "User allowed internet!", Toast.LENGTH_SHORT).show();
                    this.loadDataUsingAPI();
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied for Internet!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    private void loadDataUsingAPI(){
        String url1 ="http://ip-api.com/json/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{

                            String latitude = response.getString("lat");
                            String longitude = response.getString("lon");
                            String city = response.getString("city");
                            String state = response.getString("region");
                            String country = response.getString("countryCode");
                            String location = city + ", " + state + ", " + country;
                            Sharable.location = location;
                            Sharable.searchCity = city;
                            Sharable.currLocation = location;
                            FavViewAdapter.items.add(location);

                                    String url2 = "http://weatherangular.us-east-2.elasticbeanstalk.com/hw_9/weather/" + latitude + "/" + longitude;
                            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url2, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                                Sharable.currJsonObj = response;
                                                Sharable.jsonObj = response;
                                                LoadSavedPreferences();
                                                LoadAllTabs();
                                                }
                                    },
                                    new Response.ErrorListener(){
                                        @Override
                                        public void onErrorResponse(VolleyError error){
                                            Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest2);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // txtLat.setText("That didn't work!");
                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void LoadAllTabs() {
        List<String> lst =   Sharable.getInstance().locationList;
        Sharable.getInstance().mMap.clear();
        if(lst.size() <= 0)
        {
            GotoSummaryPage();
        }
        for(int i = 0; i < lst.size(); i++)
        {
            loadDataUsingAPI(lst.get(i));
        }
    }

    private void loadDataUsingAPI(final String cityName){
        String city = null;
        try {
            city = URLEncoder.encode(cityName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url1 = "http://weatherangular.us-east-2.elasticbeanstalk.com/hw_9/location/" + city;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String status = response.getString("status");
                            if(status.equals("OK"))
                            {
                                JSONArray res = (JSONArray) response.get("results");
                                JSONObject geo = res.getJSONObject(0).getJSONObject("geometry");
                                String location = res.getJSONObject(0).getString("formatted_address");
                                Sharable.location = location;
                                String latitude = geo.getJSONObject("location").getString("lat");
                                String longitude = geo.getJSONObject("location").getString("lng");

                                String url2 = "http://weatherangular.us-east-2.elasticbeanstalk.com/hw_9/weather/" + latitude + "/" + longitude;
                                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url2, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Sharable.getInstance().mMap.put(cityName.toLowerCase(), response);
                                                count++;
                                                if(count == Sharable.getInstance().locationList.size())
                                                {
                                                    GotoSummaryPage();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener(){
                                            @Override
                                            public void onErrorResponse(VolleyError error){

                                                //txtLat.setText("That didn't work!");
                                            }
                                        });

                                MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest2);

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // txtLat.setText("That didn't work!");
                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void LoadSavedPreferences(){
        SharedPreferences sharedPref = this.getSharedPreferences("com.csci571.forecast", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Sharable.getInstance().locationList.add(entry.getValue().toString());
        }
    }

    private void GotoSummaryPage() {
        Intent intent = new Intent(this, Summary.class);
        startActivity(intent);
        this.finish();
    }
}
