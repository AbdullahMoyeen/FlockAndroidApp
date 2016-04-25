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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.viiup.android.flock.models.UserEventModel;
import com.viiup.android.flock.models.UserModel;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, TabLayout.OnTabSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SearchView searchView;
    private HomeTabPagerAdapter mHomeTabPagerAdapter;
    private TabLayout tabLayout;
    private FloatingActionButton fabMine;
    private HomeEventsFragment eventsFragment;
    private HomeGroupsFragment groupsFragment;
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
        getSupportActionBar().setIcon(R.drawable.ic_logo_with_name);
        getSupportActionBar().setTitle("");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mHomeTabPagerAdapter = new HomeTabPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mHomeTabPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(this);

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

        fabMine = (FloatingActionButton) findViewById(R.id.fabMyEvents);
        fabMine.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_calendar).colorRes(R.color.colorBarText));
        fabMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabLayout.getSelectedTabPosition() == 0) {

                    Snackbar snackbar = Snackbar.make(view, getString(R.string.title_my_events), Snackbar.LENGTH_LONG).setAction("", null);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(sbView.getContext(), R.color.colorButton));
                    snackbar.setActionTextColor(ContextCompat.getColor(sbView.getContext(), R.color.colorBarText));
                    snackbar.show();

                    eventsFragment = (HomeEventsFragment) getSupportFragmentManager().getFragments().get(0);
                    eventsFragment.filterMyEvents();
                    tabLayout.getTabAt(0).select();
                } else {

                    Snackbar snackbar = Snackbar.make(view, getString(R.string.title_my_groups), Snackbar.LENGTH_LONG).setAction("", null);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(sbView.getContext(), R.color.colorButton));
                    snackbar.setActionTextColor(ContextCompat.getColor(sbView.getContext(), R.color.colorBarText));
                    snackbar.show();

                    groupsFragment = (HomeGroupsFragment) getSupportFragmentManager().getFragments().get(1);
                    groupsFragment.filterMyGroups();
                    tabLayout.getTabAt(1).select();
                }
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            fabMine.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_calendar).colorRes(R.color.colorBarText));
            eventsFragment = (HomeEventsFragment) getSupportFragmentManager().getFragments().get(0);
            eventsFragment.resetToFull();
        } else {
            fabMine.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_users).colorRes(R.color.colorBarText));
            groupsFragment = (HomeGroupsFragment) getSupportFragmentManager().getFragments().get(1);
            groupsFragment.resetToFull();
        }
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        int searchOpenImageId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView searchOpenImageView = (ImageView) searchView.findViewById(searchOpenImageId);
        searchOpenImageView.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_search_plus).actionBarSize().colorRes(R.color.colorContentBackground));
        int searchCloseImageId = getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView searchCLoseImageView = (ImageView) searchView.findViewById(searchCloseImageId);
        searchCLoseImageView.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_search_minus).actionBarSize().colorRes(R.color.colorContentBackground));
        searchView.setOnQueryTextListener(this);

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

    public boolean onQueryTextChange(String newText) {
        if (tabLayout.getSelectedTabPosition() == 0) {
            eventsFragment = (HomeEventsFragment) getSupportFragmentManager().getFragments().get(0);
            return eventsFragment.onQueryTextChange(newText);
        } else {
            groupsFragment = (HomeGroupsFragment) getSupportFragmentManager().getFragments().get(1);
            return groupsFragment.onQueryTextChange(newText);
        }
    }

    public boolean onQueryTextSubmit(String query) {
        if (tabLayout.getSelectedTabPosition() == 0) {
            return eventsFragment.onQueryTextSubmit(query);
        } else {
            return groupsFragment.onQueryTextSubmit(query);
        }
    }
}
