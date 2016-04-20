package com.viiup.android.flock.application;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.joanzapata.iconify.Iconify;
import com.viiup.android.flock.helpers.CommonHelper;
import com.viiup.android.flock.models.UserEventModel;
import com.viiup.android.flock.services.IAsyncPutRequestResponse;
import com.viiup.android.flock.services.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EventDetailsActivity extends AppCompatActivity {

    private static class ItemsViewHolder {
        TextView textViewSecondaryBar;
        TextView textViewAttendeeCount;
        TextView textViewEventStartDateTime;
        TextView textViewEventDescription;
        ImageView imageViewGroup;
        TextView textViewGroupName;
        TextView textViewEventAddress;
        Switch switchRsvp;
    }

    private Context context;
    private ItemsViewHolder itemsViewHolder;
    private UserEventModel userEvent;
    private boolean isAttendingChanged = false;
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = this;

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        setContentView(R.layout.event_details_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Gson gson = new Gson();
        String userEventJson = getIntent().getStringExtra("userEventJson");
        userEvent = gson.fromJson(userEventJson, UserEventModel.class);

        itemsViewHolder = new ItemsViewHolder();
        itemsViewHolder.textViewSecondaryBar = (TextView) findViewById(R.id.secondaryBar);
        itemsViewHolder.textViewAttendeeCount = (TextView) findViewById(R.id.textViewAttendeeCount);
        itemsViewHolder.textViewEventStartDateTime = (TextView) findViewById(R.id.textViewEventStartDateTime);
        itemsViewHolder.textViewEventDescription = (TextView) findViewById(R.id.textViewEventDescription);
        itemsViewHolder.imageViewGroup = (ImageView) findViewById(R.id.imageViewGroup);
        itemsViewHolder.textViewGroupName = (TextView) findViewById(R.id.textViewGroupName);
        itemsViewHolder.textViewEventAddress = (TextView) findViewById(R.id.textViewEventAddress);
        itemsViewHolder.switchRsvp = (Switch) findViewById(R.id.switchRsvp);

        DateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.fmt_date_format));

        itemsViewHolder.textViewSecondaryBar.setText(userEvent.event.getEventName().toUpperCase());
        itemsViewHolder.textViewAttendeeCount.setText(userEvent.event.getAttendeeCount() + " going");
        itemsViewHolder.textViewEventStartDateTime.setText(dateFormat.format(userEvent.event.getEventStartDatetime()));
        itemsViewHolder.textViewEventDescription.setText(userEvent.event.getEventDescription());
        itemsViewHolder.imageViewGroup.setImageDrawable(CommonHelper.getIconDrawableByEventCategory(this, userEvent.event.getEventCategory()));
        itemsViewHolder.textViewGroupName.setText(userEvent.event.getGroupName());
        itemsViewHolder.textViewEventAddress.setText(userEvent.event.getEventAddressLine1() + ", " + userEvent.event.getEventCity() + ", " + userEvent.event.getEventStateCode() + " " + userEvent.event.getEventPostalCode());
        itemsViewHolder.switchRsvp.setTextOff(Iconify.compute(context, context.getResources().getString(R.string.fa_icon_off)));
        itemsViewHolder.switchRsvp.setTextOn(Iconify.compute(context, context.getResources().getString(R.string.fa_icon_on)));
        itemsViewHolder.switchRsvp.setChecked(userEvent.getIsAttending());
        itemsViewHolder.switchRsvp.setOnCheckedChangeListener(new SwitchRsvpOnCheckedChangeListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            // up button
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("isAttendingChanged", isAttendingChanged);
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

    /*
        Helper class acting as OnCheckedChangeListener
     */
    private class SwitchRsvpOnCheckedChangeListener implements IAsyncPutRequestResponse, CompoundButton.OnCheckedChangeListener {

        private boolean isAttending;

        @Override
        public void putRequestResponse(String response) {

            // Dismiss progress dialogue
            if (progressDialog != null) progressDialog.dismiss();

            if (response.equalsIgnoreCase("OK")) {

                userEvent.setIsAttending(this.isAttending);
                int attendeeCount = userEvent.event.getAttendeeCount();
                userEvent.event.setAttendeeCount(this.isAttending ? attendeeCount + 1 : attendeeCount - 1);
                isAttendingChanged = !isAttendingChanged;
                itemsViewHolder.textViewAttendeeCount.setText(userEvent.event.getAttendeeCount() + " going");
            } else {
                Toast.makeText(context, R.string.msg_processing_failed, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void backGroundErrorHandler(Exception ex) {

            if (progressDialog != null) progressDialog.dismiss();

            // Print stack trace...may be add logging in future releases
            ex.printStackTrace();

            // display error message
            Toast.makeText(context, R.string.msg_something_wrong, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isOn) {

            this.isAttending = isOn;

            // Show the progress bar
            progressDialog = ProgressDialog.show(context, "RSVP", getResources().getString(R.string.msg_processing_request));

            UserService userService = new UserService();
            userService.setUserEventRsvp(userEvent.getUserId(), userEvent.event.getEventId(), isOn, this);
        }
    }
}
