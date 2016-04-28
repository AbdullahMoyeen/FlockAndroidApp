package com.viiup.android.flock.application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class StartupActivity extends AppCompatActivity {

    private Context context;
    private StartupTabPagerAdapter mStartupTabPagerAdapter;
    private ViewPager mViewPager;
    private TextView textViewDot1;
    private TextView textViewDot2;
    private TextView textViewDot3;
    private TextView textViewDot4;
    private Button buttonJoin;
    private TextView textViewSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Iconify.with(new FontAwesomeModule());

        setContentView(R.layout.startup_activity);

        context = this;

        mStartupTabPagerAdapter = new StartupTabPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mStartupTabPagerAdapter);

        textViewDot1 = (TextView) findViewById(R.id.textViewDot1);
        textViewDot2 = (TextView) findViewById(R.id.textViewDot2);
        textViewDot3 = (TextView) findViewById(R.id.textViewDot3);
        textViewDot4 = (TextView) findViewById(R.id.textViewDot4);

        if (mViewPager.getCurrentItem() == 0)
            textViewDot1.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_solid)));
        else
            textViewDot1.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_hollow)));

        if (mViewPager.getCurrentItem() == 1)
            textViewDot2.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_solid)));
        else
            textViewDot2.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_hollow)));

        if (mViewPager.getCurrentItem() == 2)
            textViewDot3.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_solid)));
        else
            textViewDot3.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_hollow)));

        if (mViewPager.getCurrentItem() == 3)
            textViewDot4.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_solid)));
        else
            textViewDot4.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_hollow)));

        buttonJoin = (Button) findViewById(R.id.buttonJoin);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupActivityIntent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(signupActivityIntent);
            }
        });

        textViewSignin = (TextView) findViewById(R.id.textViewSignin);
        textViewSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signinActivityIntent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(signinActivityIntent);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    textViewDot1.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_solid)));
                else
                    textViewDot1.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_hollow)));

                if (position == 1)
                    textViewDot2.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_solid)));
                else
                    textViewDot2.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_hollow)));

                if (position == 2)
                    textViewDot3.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_solid)));
                else
                    textViewDot3.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_hollow)));

                if (position == 3)
                    textViewDot4.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_solid)));
                else
                    textViewDot4.setText(Iconify.compute(context, getString(R.string.icon_fa_circle_hollow)));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        SharedPreferences mPref = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        String authenticatedUserJson = mPref.getString("authenticatedUserJson", null);

        if (authenticatedUserJson != null) {
            Intent homeIntent = new Intent(this, HomeActivity.class);
            startActivity(homeIntent);
        }
    }
}
