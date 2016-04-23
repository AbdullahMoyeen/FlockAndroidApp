package com.viiup.android.flock.application;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.viiup.android.flock.models.UserEventModel;
import com.viiup.android.flock.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private HomeTabPagerAdapter mHomeTabPagerAdapter;
    public List<UserEventModel> userEvents;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Move this section to Login service
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor mPrefsEditor = mPrefs.edit();

        UserModel loggedInUser = new UserModel();
        loggedInUser.setUserId(2);

        Gson gson = new Gson();
        String loggedInUserJson = gson.toJson(loggedInUser);
        mPrefsEditor.putString("loggedInUserJson", loggedInUserJson);
        mPrefsEditor.apply();
        // End move to Login service

        Iconify.with(new FontAwesomeModule());

        setContentView(R.layout.home_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo_transparent);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mHomeTabPagerAdapter = new HomeTabPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mHomeTabPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fabNearbyEvents = (FloatingActionButton) findViewById(R.id.fabNearbyEvents);
        fabNearbyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG).setAction(getString(R.string.title_nearby_events), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Gson gson = new Gson();
                        String userEventsJson = gson.toJson(userEvents);
                        Intent mapIntent = new Intent(v.getContext(), MapActivity.class);
                        mapIntent.putExtra("userEventsJson", userEventsJson);
                        startActivity(mapIntent);
                    }
                });

                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(sbView.getContext(), R.color.colorButton));
                snackbar.setActionTextColor(ContextCompat.getColor(sbView.getContext(), R.color.colorBarText));
                snackbar.show();
            }
        });

        FloatingActionButton fabMyEvents = (FloatingActionButton) findViewById(R.id.fabMyEvents);
        fabMyEvents.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_calendar).colorRes(R.color.colorBarText));
        fabMyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar snackbar = Snackbar.make(view, getString(R.string.title_my_events), Snackbar.LENGTH_LONG).setAction("", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(sbView.getContext(), R.color.colorButton));
                snackbar.setActionTextColor(ContextCompat.getColor(sbView.getContext(), R.color.colorBarText));
                snackbar.show();

                List<UserEventModel> myEvents = new ArrayList<UserEventModel>();
                for (UserEventModel userEvent: userEvents){
                    if (userEvent.getIsAttending())
                        myEvents.add(userEvent);
                }

                HomeEventsFragment eventsFragment = (HomeEventsFragment) getSupportFragmentManager().getFragments().get(0);
                HomeEventsCellAdapter adapter = new HomeEventsCellAdapter(eventsFragment.getActivity(), eventsFragment.getListView(), myEvents);
                eventsFragment.getListView().setAdapter(adapter);
                tabLayout.getTabAt(0).select();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
