package com.viiup.android.flock.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.viiup.android.flock.helpers.CommonHelper;
import com.viiup.android.flock.models.UserEventModel;
import com.viiup.android.flock.services.UserService;

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
        System.out.println(userEvent.isAttending());

        TextView textViewSecondaryBar = (TextView) findViewById(R.id.secondaryBar);
        TextView textViewAttendeeCount = (TextView) findViewById(R.id.textViewAttendeeCount);
        TextView textViewEventStartDateTime = (TextView) findViewById(R.id.textViewEventStartDateTime);
        TextView textViewEventDescription = (TextView) findViewById(R.id.textViewEventDescription);
        ImageView imageViewGroup = (ImageView) findViewById(R.id.imageViewGroup);
        TextView textViewGroupName = (TextView) findViewById(R.id.textViewGroupName);
        TextView textViewEventAddress = (TextView) findViewById(R.id.textViewEventAddress);
        Switch switchRsvp = (Switch) findViewById(R.id.switchRsvp);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        textViewSecondaryBar.setText(userEvent.event.getEventName().toUpperCase());
        textViewAttendeeCount.setText(userEvent.event.getAttendeeCount() + " going");
        textViewEventStartDateTime.setText(dateFormat.format(userEvent.event.getEventStartDatetime()));
        textViewEventDescription.setText(userEvent.event.getEventDescription());
        imageViewGroup.setImageDrawable(CommonHelper.getIconDrawableByEventCategory(this, userEvent.event.getEventCategory()));
        textViewGroupName.setText(userEvent.event.getGroupName());
        textViewEventAddress.setText(userEvent.event.getEventAddressLine1() + ", " + userEvent.event.getEventCity() + ", " + userEvent.event.getEventStateCode() + " " + userEvent.event.getEventPostalCode());
        switchRsvp.setChecked(userEvent.isAttending());
        switchRsvp.setOnCheckedChangeListener(switchRsvpOnCheckedChangeListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            // up button
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("isAttending", userEvent.isAttending());
                setResult(Activity.RESULT_OK, returnIntent);
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

    private CompoundButton.OnCheckedChangeListener switchRsvpOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isOn) {

//            Toast.makeText(buttonView.getContext(), "changed event " + userEvent.event.getEventId() + " to " + isOn, Toast.LENGTH_SHORT).show();

            UserService userService = new UserService();
            userService.setUserRsvp(userEvent.getUserId(), userEvent.event.getEventId(), isOn);

            userEvent.setIsAttending(isOn);
        }
    };
}
