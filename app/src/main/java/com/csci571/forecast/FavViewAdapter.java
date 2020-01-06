package com.csci571.forecast;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

class FavViewAdapter extends FragmentPagerAdapter {

    private long baseId = 0;
    public static final List<String> items = new ArrayList<String>();

    public FavViewAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return SummaryFragment.getInstance(position, items.get(position));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    public void removeTabPage(int position) {
        if (!items.isEmpty() && position<items.size()) {
            items.remove(position);
            notifyChangeInPosition(1);
            notifyDataSetChanged();
        }
    }

    public void removeALlTabPages() {
        if (!items.isEmpty()) {
            items.clear();
            notifyChangeInPosition(items.size());
            notifyDataSetChanged();
        }
    }

    public void addTabPage(String title) {
        items.add(title);
        notifyChangeInPosition(1);
        notifyDataSetChanged();
    }

    public boolean hasPage(String pageTitle) {
        if(items.contains(pageTitle))
        {
            return true;
        }
        return false;
    }

    //this is called when notifyDataSetChanged() is called
    @Override
    public int getItemPosition(Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
    }


    @Override
    public long getItemId(int position) {
        // give an ID different from position when position has been changed
        return baseId + position;
    }

    /**
     * Notify that the position of a fragment has been changed.
     * Create a new ID for each position to force recreation of the fragment
     * @param n number of items which have been changed
     */
    public void notifyChangeInPosition(int n) {
        // shift the ID returned by getItemId outside the range of all previous fragments
        baseId += getCount() + n;
    }
}