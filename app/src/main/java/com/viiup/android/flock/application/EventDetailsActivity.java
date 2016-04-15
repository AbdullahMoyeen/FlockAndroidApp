package com.viiup.android.flock.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class EventDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        setContentView(R.layout.event_details_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id) {
            // up button
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}
