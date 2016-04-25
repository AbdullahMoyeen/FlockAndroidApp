package com.viiup.android.flock.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.viiup.android.flock.models.UserModel;

public class StartupActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private StartupTabPagerAdapter mStartupTabPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        setContentView(R.layout.startup_activity);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mStartupTabPagerAdapter = new StartupTabPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mStartupTabPagerAdapter);

        Button buttonSignUp = (Button) findViewById(R.id.buttonSignUp);

        TextView textViewSignIn = (TextView) findViewById(R.id.textViewSignIn);
        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                // Move this section to Login service
                SharedPreferences mPref = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
                SharedPreferences.Editor mPrefsEditor = mPref.edit();

                UserModel loggedInUser = new UserModel();
                loggedInUser.setUserId(2);

                Gson gson = new Gson();
                String loggedInUserJson = gson.toJson(loggedInUser);
                mPrefsEditor.putString("loggedInUserJson", loggedInUserJson);
                mPrefsEditor.apply();
                // End move to Login service

                Intent homeActivityIntent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(homeActivityIntent);
            }
        });
    }
}
