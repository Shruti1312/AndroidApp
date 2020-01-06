package com.csci571.forecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class SearchableA extends AppCompatActivity{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FavViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new FavViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();

        LoadSavedPreferences();
    }

    public void notifyUser(String message) {
       Toast.makeText(SearchableA.this, message, Toast.LENGTH_SHORT).show();
    }

    public void removeTab(int position) {
        if (tabLayout.getTabCount() >= 1 && position<tabLayout.getTabCount()) {
            tabLayout.removeTabAt(position);
            adapter.removeTabPage(position);
            removefromSavedPreferences(Integer.toString(position));
            notifyUser("The tab was removed");
        }
    }

    public void addTab(String title) {
        int position = adapter.getCount();
        tabLayout.addTab(tabLayout.newTab().setText(title));
        adapter.addTabPage(title);
        SavetoPreferences(Integer.toString(position), title);
        notifyUser("The tab was added!");
    }

    private void SavetoPreferences(String key, String value) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void LoadSavedPreferences(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            addTab(entry.getValue().toString());
            notifyUser("Adding :"+ entry.getValue().toString());
            //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }
    }

    private void removefromSavedPreferences(String key) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.commit();
    }
}