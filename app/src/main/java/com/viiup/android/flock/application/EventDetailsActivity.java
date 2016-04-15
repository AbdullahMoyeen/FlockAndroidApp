package com.viiup.android.flock.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.viiup.android.flock.helpers.CommonHelper;
import com.viiup.android.flock.models.UserEventModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EventDetailsActivity extends AppCompatActivity {

    private UserEventModel userEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        setContentView(R.layout.event_details_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Gson gson = new Gson();
        String userEventJson = getIntent().getStringExtra("userEventJson");
        userEvent = gson.fromJson(userEventJson, UserEventModel.class);
        System.out.println(userEvent.event.getEventName());

        TextView textViewSecondaryBar = (TextView) findViewById(R.id.secondaryBar);
        TextView textViewAttendeeCount = (TextView) findViewById(R.id.textViewAttendeeCount);
        TextView textViewEventStartDateTime = (TextView) findViewById(R.id.textViewEventStartDateTime);
        TextView textViewEventDescription = (TextView) findViewById(R.id.textViewEventDescription);
        ImageView imageViewGroup = (ImageView) findViewById(R.id.imageViewGroup);
        TextView textViewGroupName = (TextView) findViewById(R.id.textViewGroupName);
        TextView textViewEventAddress = (TextView) findViewById(R.id.textViewEventAddress);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        textViewSecondaryBar.setText(userEvent.event.getEventName().toUpperCase());
        textViewAttendeeCount.setText(userEvent.event.getAttendeeCount() + " going");
        textViewEventStartDateTime.setText(dateFormat.format(userEvent.event.getEventStartDatetime()));
        textViewEventDescription.setText(userEvent.event.getEventDescription());
        imageViewGroup.setImageDrawable(CommonHelper.getIconDrawableByEventCategory(this, userEvent.event.getEventCategory()));
        textViewGroupName.setText(userEvent.event.getGroupName());
        textViewEventAddress.setText(userEvent.event.getEventAddressLine1() + ", " + userEvent.event.getEventCity() + ", " + userEvent.event.getEventStateCode() + " " + userEvent.event.getEventPostalCode());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
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
