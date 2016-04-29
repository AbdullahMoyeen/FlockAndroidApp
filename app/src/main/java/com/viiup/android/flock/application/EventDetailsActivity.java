package com.viiup.android.flock.application;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joanzapata.iconify.Iconify;
import com.viiup.android.flock.helpers.CommonHelper;
import com.viiup.android.flock.models.UserEventModel;
import com.viiup.android.flock.services.IAsyncRequestResponse;
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
    private String userEventFullAddress;
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
        userEventFullAddress = userEvent.event.getEventAddressLine1() + ", " + userEvent.event.getEventCity() + ", " + userEvent.event.getEventStateCode() + " " + userEvent.event.getEventPostalCode();

        itemsViewHolder = new ItemsViewHolder();
        itemsViewHolder.textViewSecondaryBar = (TextView) findViewById(R.id.secondaryBar);
        itemsViewHolder.textViewAttendeeCount = (TextView) findViewById(R.id.textViewAttendeeCount);
        itemsViewHolder.textViewEventStartDateTime = (TextView) findViewById(R.id.textViewEventStartDateTime);
        itemsViewHolder.textViewEventDescription = (TextView) findViewById(R.id.textViewEventDescription);
        itemsViewHolder.imageViewGroup = (ImageView) findViewById(R.id.imageViewGroup);
        itemsViewHolder.textViewGroupName = (TextView) findViewById(R.id.textViewGroupName);
        itemsViewHolder.textViewEventAddress = (TextView) findViewById(R.id.textViewEventAddress);
        itemsViewHolder.switchRsvp = (Switch) findViewById(R.id.switchRsvp);

        DateFormat dateFormat = new SimpleDateFormat(getString(R.string.fmt_date_format));

        itemsViewHolder.textViewSecondaryBar.setText(userEvent.event.getEventName().toUpperCase());
        itemsViewHolder.textViewAttendeeCount.setText(userEvent.event.getAttendeeCount() + " going");
        itemsViewHolder.textViewEventStartDateTime.setText(dateFormat.format(userEvent.event.getEventStartDatetime()));
        if (userEvent.getIsAttending()) {
            itemsViewHolder.textViewEventStartDateTime.setTextColor(ContextCompat.getColor(context, R.color.text_link_color));
            itemsViewHolder.textViewEventStartDateTime.setPaintFlags(itemsViewHolder.textViewEventStartDateTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            itemsViewHolder.textViewEventStartDateTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, userEvent.event.getEventStartDatetime().getTime())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, userEvent.event.getEventEndDatetime().getTime())
                            .putExtra(CalendarContract.Events.TITLE, userEvent.event.getEventName())
                            .putExtra(CalendarContract.Events.DESCRIPTION, userEvent.event.getEventDescription())
                            .putExtra(CalendarContract.Events.EVENT_LOCATION, userEventFullAddress)
                            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                    startActivity(intent);
                }
            });
        }
        itemsViewHolder.textViewEventDescription.setText(userEvent.event.getEventDescription());
        itemsViewHolder.imageViewGroup.setImageDrawable(CommonHelper.getIconDrawableByEventCategory(this, userEvent.event.getEventCategory()));
        itemsViewHolder.textViewGroupName.setText(userEvent.event.getGroupName());
        itemsViewHolder.textViewEventAddress.setText(userEventFullAddress);
        itemsViewHolder.switchRsvp.setTextOff(Iconify.compute(context, getString(R.string.icon_fa_not_going)));
        itemsViewHolder.switchRsvp.setTextOn(Iconify.compute(context, getString(R.string.icon_fa_going)));
        itemsViewHolder.switchRsvp.setChecked(userEvent.getIsAttending());
        itemsViewHolder.switchRsvp.setOnCheckedChangeListener(new SwitchRsvpOnCheckedChangeListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.empty_menu, menu);
        return true;
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
    private class SwitchRsvpOnCheckedChangeListener implements IAsyncRequestResponse, CompoundButton.OnCheckedChangeListener {

        private boolean isAttending;

        @Override
        public void responseHandler(String response) {

            // Dismiss progress dialogue
            if (progressDialog != null) progressDialog.dismiss();

            if (response.equalsIgnoreCase("OK")) {

                userEvent.setIsAttending(this.isAttending);
                int attendeeCount = userEvent.event.getAttendeeCount();
                userEvent.event.setAttendeeCount(this.isAttending ? attendeeCount + 1 : attendeeCount - 1);
                isAttendingChanged = !isAttendingChanged;
                itemsViewHolder.textViewAttendeeCount.setText(userEvent.event.getAttendeeCount() + " going");
                if (userEvent.getIsAttending()) {
                    itemsViewHolder.textViewEventStartDateTime.setTextColor(ContextCompat.getColor(context, R.color.text_link_color));
                    itemsViewHolder.textViewEventStartDateTime.setPaintFlags(itemsViewHolder.textViewEventStartDateTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    itemsViewHolder.textViewEventStartDateTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_INSERT)
                                    .setData(CalendarContract.Events.CONTENT_URI)
                                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, userEvent.event.getEventStartDatetime().getTime())
                                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, userEvent.event.getEventEndDatetime().getTime())
                                    .putExtra(CalendarContract.Events.TITLE, userEvent.event.getEventName())
                                    .putExtra(CalendarContract.Events.DESCRIPTION, userEvent.event.getEventDescription())
                                    .putExtra(CalendarContract.Events.EVENT_LOCATION, userEventFullAddress)
                                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                            startActivity(intent);
                        }
                    });
                } else {
                    itemsViewHolder.textViewEventStartDateTime.setTextColor(ContextCompat.getColor(context, R.color.colorContentText));
                    itemsViewHolder.textViewEventStartDateTime.setPaintFlags(0);
                    itemsViewHolder.textViewEventStartDateTime.setOnClickListener(null);
                }
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
            Toast.makeText(context, R.string.error_something_wrong, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isOn) {

            this.isAttending = isOn;

            // Show the progress bar
            progressDialog = ProgressDialog.show(context, "RSVP", getString(R.string.msg_processing_request));

            UserService userService = new UserService();
            userService.setUserEventRsvp(userEvent.getUserId(), userEvent.event.getEventId(), isOn, this);
        }
    }
}
