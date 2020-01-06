package com.csci571.forecast;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.csci571.forecast.dummy.ImagesInfo;
import com.csci571.forecast.dummy.Summary_weekly;
import com.csci571.forecast.dummy.Weekly;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SearchResult extends AppCompatActivity implements ItemFragment2.OnListFragmentInteractionListener{
    public LinearLayout linlaHeaderProgress;
    public int count = 0;
    public FloatingActionButton fav_add;
    public FloatingActionButton fav_minus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        ActionBar actionBar =  getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Sharable.location);

        this.setCardValues();
        fav_add = (FloatingActionButton) findViewById(R.id.fab_plus);
        fav_minus = (FloatingActionButton) findViewById(R.id.fab_minus);
        this.setFavButton();

        fav_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            fav_add.hide();
            fav_minus.show();
            Sharable.getInstance().locationList.add(Sharable.searchCity.trim().toLowerCase());
            notifyUser(Sharable.location + " was added to favorites");
            }
        });

        fav_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            fav_minus.hide();
            fav_add.show();
            Sharable.getInstance().locationList.remove(Sharable.searchCity.trim().toLowerCase());
            notifyUser(Sharable.location + " was removed from favorites");
            }
        });
    }

    public void setFavButton() {
        if(Sharable.getInstance().locationList.contains(Sharable.searchCity.trim().toLowerCase()))
        {
            fav_minus.show();
            fav_add.hide();
        }
        else
        {
            fav_minus.hide();
            fav_add.show();
        }
    }

    public void notifyUser(String message) {
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.customtoast,(ViewGroup) findViewById(R.id.custom_toast_layout));

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);//setting the view of custom toast layout
        TextView v = (TextView)toast.getView().findViewById(R.id.custom_toast_message);
        v.setText(message);
        toast.show();
    }

    public void setCardValues() {
        try
        {
            TextView locationText = (TextView) this.findViewById(R.id.location);
            locationText.setText(Sharable.location);

            JSONObject currently = Sharable.jsonObj.getJSONObject("currently");
            JSONObject daily = Sharable.jsonObj.getJSONObject("daily");

            String icon = currently.getString("icon");
            Integer temp = currently.getInt("temperature");
            String summary = currently.getString("summary");
            String temperature = temp.toString() + (char) 0x00B0 + " F";
            setCard1(icon, temperature, summary);
            Sharable.temperature = temperature;

            DecimalFormat df = new DecimalFormat("0.00");
            df.setMaximumFractionDigits(2);

            String humidity = df.format(currently.getDouble("humidity") * 100);
            String windSpeed = df.format(currently.getDouble("windSpeed"));
            String visibility = df.format(currently.getDouble("visibility"));
            String pressure = df.format(currently.getDouble("pressure"));
            setCard2(humidity, windSpeed, visibility, pressure);

            JSONArray jsonArr = (JSONArray)daily.get("data");
            Summary_weekly.ITEMS.clear();
            for(int i = 0; i < jsonArr.length(); i++)
            {
                String icon_name =  jsonArr.getJSONObject(i).getString("icon");
                long time =  jsonArr.getJSONObject(i).getLong("time");
                Integer min_temp =  jsonArr.getJSONObject(i).getInt("temperatureLow");
                Integer max_temp =  jsonArr.getJSONObject(i).getInt("temperatureHigh");
                setCard3(time, icon_name, min_temp.toString(), max_temp.toString());
            }
        }catch(JSONException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setCard1(String icon, String temperature, String summary) {
        TextView summaryText = (TextView) findViewById(R.id.summary);
        TextView tempText = (TextView) findViewById(R.id.temperature);
        ImageView iconImage = (ImageView) findViewById(R.id.icon_img);

        iconImage.setImageResource(Sharable.getIconResourceId(icon));
        summaryText.setText(summary);
        tempText.setText(temperature);
    }

    public void setCard2(String humidity, String windSpeed, String visibility, String pressure) {
        TextView humidityText = (TextView) findViewById(R.id.humidity);
        TextView windSpeedText = (TextView) findViewById(R.id.windSpeed);
        TextView visibilityText = (TextView) findViewById(R.id.visibility);
        TextView pressureText = (TextView) findViewById(R.id.pressure);

        humidityText.setText(humidity + "%");
        windSpeedText.setText(windSpeed + " mph");
        visibilityText.setText(visibility + " km");
        pressureText.setText(pressure + " mb");
    }

    public void setCard3(long time, String icon_name, String min_temperature, String max_temperature){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date timestamp = new Date(time * 1000);
        String dt = sdf.format(timestamp);

        Weekly wk = new Weekly(dt, icon_name, min_temperature, max_temperature);
        Summary_weekly.addItem(wk);
    }

    public void OnCard1Click(View view) {
        this.LoadAllTabs(true);
    }

    public void hideItems(){
        TextView txt = (TextView) findViewById(R.id.search_result);
        txt.setText("");
        this.fav_add.hide();
        this.fav_minus.hide();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.LoadAllTabs(false);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void GotoSummaryPage() {
        Intent intent = new Intent(this, Summary.class);
        startActivity(intent);
        this.finish();
    }

    public void GoToDetailPage() {
        Intent detailedActivity = new Intent(this, Detailed.class);
        startActivity(detailedActivity);
        this.finish();
    }

    public void LoadAllTabs(boolean isDetail){
        this.hideItems();
        List<String> lst =   Sharable.getInstance().locationList;
        if(lst.size() <= 0)
        {
            if(isDetail)
            {
                linlaHeaderProgress = (LinearLayout)findViewById(R.id.linlaHeaderProgress);
                linlaHeaderProgress.setVisibility(View.VISIBLE);
                this.loadImages(Sharable.location);
            }else
            {
                GotoSummaryPage();
            }
        }
        else
        {
            Sharable.getInstance().mMap.clear();
            linlaHeaderProgress = (LinearLayout)findViewById(R.id.linlaHeaderProgress);
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            for(int i = 0; i < lst.size(); i++)
            {
                loadDataUsingAPI(lst.get(i), isDetail);
            }
        }
    }

    private void loadDataUsingAPI(final String cityName, final boolean isDetail){
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
                                                    if(isDetail)
                                                    {
                                                        loadImages(Sharable.location);
                                                    }
                                                    else
                                                    {
                                                        GotoSummaryPage();
                                                    }
                                                }
                                            }
                                        },
                                        new Response.ErrorListener(){
                                            @Override
                                            public void onErrorResponse(VolleyError error){
                                                //txtLat.setText("That didn't work!");
                                            }
                                        });

                                MySingleton.getInstance(SearchResult.this).addToRequestQueue(jsonObjectRequest2);

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

    @Override
    public void onListFragmentInteraction(Weekly item) {

    }

    public void loadImages(String cityName) {
        String city = null;
        try {
            city = URLEncoder.encode(cityName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ImagesInfo.ITEMS.clear();
        String url = "http://weatherangular.us-east-2.elasticbeanstalk.com/hw_9/customSearch/" + city;

        JsonObjectRequest imagesObj = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{

                            JSONArray items = (JSONArray) response.get("items");
                            for(int i = 0; i < items.length(); i++)
                            {
                                String imgURL = items.getJSONObject(i).getString("link");
                                ImagesInfo.addItem(imgURL);
                            }
                            GoToDetailPage();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        //txtLat.setText("That didn't work!");
                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(imagesObj);

    }
}
