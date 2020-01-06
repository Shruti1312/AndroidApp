package com.csci571.forecast;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class ViewPagerAdapter extends FragmentPagerAdapter {

    private String title[] = {"Today", "Weekly", "Photos"};

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = Tab1Fragment.getInstance(position);
                break;
            case 1:
                fragment = Tab2Fragment.getInstance(position);
                break;
            case 2:
                fragment = Tab3Fragment.getInstance(position);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}