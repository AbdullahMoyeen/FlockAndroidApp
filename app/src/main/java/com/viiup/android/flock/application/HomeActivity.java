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
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.viiup.android.flock.models.UserEventModel;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, TabLayout.OnTabSelectedListener {

    private SearchView searchView;
    private ViewPager mViewPager;
    private HomeTabPagerAdapter mHomeTabPagerAdapter;
    private TabLayout tabLayout;
    private FloatingActionButton fabMine;
    private HomeEventsFragment eventsFragment;
    private HomeGroupsFragment groupsFragment;
    public List<UserEventModel> userEvents;
    public int selectedTabPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo_with_name);
        getSupportActionBar().setTitle("");

        mHomeTabPagerAdapter = new HomeTabPagerAdapter(getSupportFragmentManager());
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

        selectedTabPosition = tab.getPosition();

        if (selectedTabPosition == 0) {
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

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent passwordChangeIntent = new Intent(this, PasswordChangeActivity.class);
            startActivity(passwordChangeIntent);

            return true;
        } else if (id == R.id.action_logout) {

            SharedPreferences mPref = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
            SharedPreferences.Editor mPrefsEditor = mPref.edit();
            mPrefsEditor.clear();
            mPrefsEditor.apply();

            Intent startupIntent = new Intent(this, StartupActivity.class);
            startupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startupIntent);

            overridePendingTransition(R.anim.left_in, R.anim.left_out);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (tabLayout.getSelectedTabPosition() == 0) {
            eventsFragment = (HomeEventsFragment) getSupportFragmentManager().getFragments().get(0);
            return eventsFragment.onQueryTextChange(newText);
        } else {
            groupsFragment = (HomeGroupsFragment) getSupportFragmentManager().getFragments().get(1);
            return groupsFragment.onQueryTextChange(newText);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (tabLayout.getSelectedTabPosition() == 0) {
            return eventsFragment.onQueryTextSubmit(query);
        } else {
            return groupsFragment.onQueryTextSubmit(query);
        }
    }

    @Override
    public void onBackPressed() {

        if (selectedTabPosition == 0) {
            super.onBackPressed();

            Intent startupIntent = new Intent(this, StartupActivity.class);
            startupIntent.putExtra("backFromHome", true);
            startupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startupIntent);

            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        } else
            tabLayout.getTabAt(0).select();
    }
}
