package com.viiup.android.flock.application;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by AbdullahMoyeen on 4/12/16.
 */
public class StartupTabPagerAdapter extends FragmentPagerAdapter {

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public StartupTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0)
            return StartupFragment1.newInstance();
        else if (position == 1)
            return StartupFragment2.newInstance();
        else if (position == 2)
            return StartupFragment3.newInstance();
        else
            return StartupFragment4.newInstance();
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Startup1";
            case 1:
                return "Startup2";
            case 2:
                return "Startup3";
            case 3:
                return "Startup4";
        }
        return null;
    }
}
