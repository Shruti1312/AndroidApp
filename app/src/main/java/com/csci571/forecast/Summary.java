package com.csci571.forecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import android.view.View;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.csci571.forecast.dummy.ImagesInfo;
import com.csci571.forecast.dummy.Weekly;

import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.support.v4.view.MenuItemCompat;
import android.graphics.Color;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Summary extends AppCompatActivity implements ItemFragment2.OnListFragmentInteractionListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FavViewAdapter adapter;
    ActionBar actionBar;

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    public LinearLayout linlaHeaderProgress;
    public FloatingActionButton fav_minus;

    public AutoCompleteTextView searchAutoComplete;
    public SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new FavViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        LoadAllTabs();
        fav_minus = (FloatingActionButton) findViewById(R.id.fab_minus);
        fav_minus.hide();
        fav_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = tabLayout.getSelectedTabPosition();
                removeTab(position);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition() == 0)
                {
                    fav_minus.hide();
                }
                else
                {
                    fav_minus.show();
                }
                //do stuff here
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void LoadAllTabs(){
        removeAllbutFirst();
        for ( String entry : Sharable.getInstance().locationList) {
            addTab(entry);
        }
    }

    public void addTab(String title) {
        tabLayout.addTab(tabLayout.newTab().setText(title));
        adapter.addTabPage(title);
        int position = adapter.getCount();
        SavetoPreferences(Integer.toString(position), title);
    }

    private void removeAllbutFirst() {
        tabLayout.removeAllTabs();
        adapter.removeALlTabPages();
        clearPreferences();

        tabLayout.addTab(tabLayout.newTab().setText(Sharable.currLocation));
        adapter.addTabPage(Sharable.currLocation);
        //SavetoPreferences(Integer.toString(0), Sharable.currLocation);
    }

    private void makeApiCall(String text) {
        Autocomplete.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray predictions = (JSONArray) responseObject.get("predictions");
                    Integer length = predictions.length() > 5 ? 5 : predictions.length();
                    for(int i = 0; i < length; i++)
                    {
                        stringList.add(predictions.getJSONObject(i).getJSONObject("structured_formatting").getString("main_text"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_action, menu);

        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.app_bar_menu_search);
        searchMenu.collapseActionView();
        // Get SearchView object.
        searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
        // Get SearchView autocomplete object.
        searchAutoComplete = (AutoCompleteTextView)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.parseColor("#1E1E1E"));
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setTextSize(18f);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);
        searchAutoComplete.setHintTextColor(Color.DKGRAY);
        searchAutoComplete.setThreshold(0);

        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line)
        {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                text1.setTextColor(Color.DKGRAY);
                text1.setBackgroundColor(Color.WHITE);
                return view;
            };
        };
        //searchAutoComplete.setThreshold(2);
        searchAutoComplete.setAdapter(autoSuggestAdapter);
        searchAutoComplete.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String queryString=(String)parent.getItemAtPosition(position);
                        searchAutoComplete.setText(queryString);
                        searchAutoComplete.dismissDropDown();
                        searchAutoComplete.clearFocus();
                    }
                });
        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadDataUsingAPI(query.trim(), searchAutoComplete);
                searchAutoComplete.dismissDropDown();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(searchAutoComplete.getText())) {
                        makeApiCall(searchAutoComplete.getText().toString());
                    }
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void loadDataUsingAPI(final String cityName, final AutoCompleteTextView srcText){
        actionBar.setTitle(Sharable.location);
        linlaHeaderProgress = (LinearLayout)findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        String city = null;
        try {
            city = URLEncoder.encode(cityName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        searchAutoComplete.dismissDropDown();
        searchView.clearFocus();
        //searchView.onActionViewCollapsed();
        //actionBar.setDisplayUseLogoEnabled(true);

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
                                Sharable.searchCity = cityName;
                                srcText.setText(location);
                                String latitude = geo.getJSONObject("location").getString("lat");
                                String longitude = geo.getJSONObject("location").getString("lng");
                                String url2 = "http://weatherangular.us-east-2.elasticbeanstalk.com/hw_9/weather/" + latitude + "/" + longitude;
                                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url2, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Sharable.jsonObj = response;
                                                GotoSearchResultActivity();
                                            }
                                        },
                                        new Response.ErrorListener(){
                                            @Override
                                            public void onErrorResponse(VolleyError error){
                                                //txtLat.setText("That didn't work!");
                                            }
                                        });

                                MySingleton.getInstance(Summary.this).addToRequestQueue(jsonObjectRequest2);

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

    public void removeTab(int position) {
        if (tabLayout.getTabCount() >= 1 && position<tabLayout.getTabCount()) {
            String slocation = FavViewAdapter.items.get(tabLayout.getSelectedTabPosition());
            tabLayout.removeTabAt(position);
            adapter.removeTabPage(position);
            removefromSavedPreferences(Integer.toString(position));
            Sharable.getInstance().locationList.remove(slocation.toLowerCase());
            Sharable.getInstance().mMap.remove(slocation.toLowerCase());
        }
    }

    private void SavetoPreferences(String key, String value) {
        SharedPreferences sharedPref = this.getSharedPreferences("com.csci571.forecast", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void removefromSavedPreferences(String key) {
        SharedPreferences sharedPref = this.getSharedPreferences("com.csci571.forecast", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.commit();
    }

    private void clearPreferences(){
        SharedPreferences sharedPref = this.getSharedPreferences("com.csci571.forecast", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }

    public void OnCard1Click(View view) {
        String location = FavViewAdapter.items.get(tabLayout.getSelectedTabPosition());
        Sharable.location = location;
        if(location.equals(Sharable.currLocation))
        {
            Sharable.jsonObj =  Sharable.currJsonObj;
        }else
        {
            Sharable.jsonObj = Sharable.getInstance().mMap.get(location.toLowerCase());
        }
        actionBar.setTitle(Sharable.location);
        this.loadImages(Sharable.location);
    }

    public void loadImages(String cityName) {
        linlaHeaderProgress = (LinearLayout)findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
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
                            gotoDetailActivity();

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

    @Override
    public void onListFragmentInteraction(Weekly item) {

    }

    private void gotoDetailActivity() {
        Intent intent = new Intent(this, Detailed.class);
        startActivity(intent);
        this.finish();
    }

    public void GotoSearchResultActivity(){
        Intent detailedActivity = new Intent(this, SearchResult.class);
        startActivity(detailedActivity);
        this.finish();
    }
}
