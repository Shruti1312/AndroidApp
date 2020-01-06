package com.csci571.forecast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Detailed extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener{
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detailed);

            ActionBar actionBar =  getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(Sharable.location);

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.getTabAt(0).setIcon( R.drawable.today);
            tabLayout.getTabAt(1).setIcon(R.drawable.weekly_s);
            tabLayout.getTabAt(2).setIcon(R.drawable.photos_s);


            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch(tabLayout.getSelectedTabPosition())
                    {
                        case 0:
                            tabLayout.getTabAt(0).setIcon( R.drawable.today);
                            tabLayout.getTabAt(1).setIcon(R.drawable.weekly_s);
                            tabLayout.getTabAt(2).setIcon(R.drawable.photos_s);
                            break;
                        case 1:
                            tabLayout.getTabAt(0).setIcon( R.drawable.today_s);
                            tabLayout.getTabAt(1).setIcon(R.drawable.weekly);
                            tabLayout.getTabAt(2).setIcon(R.drawable.photos_s);
                            break;
                        case 2:
                            tabLayout.getTabAt(0).setIcon( R.drawable.today_s);
                            tabLayout.getTabAt(1).setIcon(R.drawable.weekly_s);
                            tabLayout.getTabAt(2).setIcon(R.drawable.photos);
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        }catch (Exception ex)
        {
            Toast.makeText(Detailed.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.goToSummaryPage();
                return true;

            case R.id.twit_away:

                String tweetUpdate = "Check out " + Sharable.location + "'s Weather! It is " + Sharable.temperature +"! ";
                try {
                    String url = "http://twitter.com/intent/tweet?text=" + URLEncoder.encode(tweetUpdate, "UTF-8") + "&button_hashtag=CSCI571WeatherSearch";
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListFragmentInteraction(String item) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action, menu);
        MenuItem twitMenu = menu.findItem(R.id.twit_away);

        return true;
    }

    public void goToSummaryPage() {
        Intent intent = new Intent(this, Summary.class);
        startActivity(intent);
        this.finish();
    }
}
