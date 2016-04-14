package com.viiup.android.flock.application;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by AbdullahMoyeen on 4/12/16.
 */
public class HomeTabPagerAdapter extends FragmentPagerAdapter {

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public HomeTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0)
            return HomeEventsFragment.newInstance();
        else
            return HomeGroupsFragment.newInstance();
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "EVENTS";
            case 1:
                return "GROUPS";
        }
        return null;
    }
}
