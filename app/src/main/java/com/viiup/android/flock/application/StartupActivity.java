package com.viiup.android.flock.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartupActivity extends AppCompatActivity {

    private StartupTabPagerAdapter mStartupTabPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        setContentView(R.layout.startup_activity);

        mStartupTabPagerAdapter = new StartupTabPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mStartupTabPagerAdapter);

        Button buttonSignup = (Button) findViewById(R.id.buttonSignup);
        TextView textViewSignin = (TextView) findViewById(R.id.textViewSignin);
        textViewSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signinActivityIntent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(signinActivityIntent);
            }
        });
    }
}
