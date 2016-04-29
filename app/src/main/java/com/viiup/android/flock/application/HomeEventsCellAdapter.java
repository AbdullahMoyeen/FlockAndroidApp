package com.viiup.android.flock.application;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.Iconify;
import com.viiup.android.flock.helpers.CommonHelper;
import com.viiup.android.flock.models.UserEventModel;
import com.viiup.android.flock.services.IAsyncRequestResponse;
import com.viiup.android.flock.services.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by AbdullahMoyeen on 4/12/16.
 */
public class HomeEventsCellAdapter extends BaseAdapter {

    private static class CellItemsViewHolder {
        ImageView imageViewEvent;
        TextView textViewGroupName;
        TextView textViewEventName;
        TextView textViewEventStartDateTime;
        TextView textViewEventDescription;
        Switch switchRsvp;
    }

    private Context context;
    private ListView listView;
    private List<UserEventModel> userEvents;
    private List<UserEventModel> userEventsFull;
    private CellItemsViewHolder cellItemsViewHolder;
    private ProgressDialog progressDialog = null;

    HomeEventsCellAdapter(Context context, ListView listView, List<UserEventModel> userEvents, List<UserEventModel> userEventsFull) {
        this.context = context;
        this.listView = listView;
        this.userEvents = userEvents;
        this.userEventsFull = userEventsFull;
    }

    @Override
    public int getCount() {
        return userEvents.size();
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return userEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final UserEventModel userEvent = userEvents.get(position);

        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.home_events_cell, null);

            cellItemsViewHolder = new CellItemsViewHolder();
            cellItemsViewHolder.imageViewEvent = (ImageView) convertView.findViewById(R.id.imageViewEvent);
            cellItemsViewHolder.textViewGroupName = (TextView) convertView.findViewById(R.id.textViewGroupName);
            cellItemsViewHolder.textViewEventName = (TextView) convertView.findViewById(R.id.textViewEventName);
            cellItemsViewHolder.textViewEventStartDateTime = (TextView) convertView.findViewById(R.id.textViewEventStartDateTime);
            cellItemsViewHolder.textViewEventDescription = (TextView) convertView.findViewById(R.id.textViewEventDescription);
            cellItemsViewHolder.switchRsvp = (Switch) convertView.findViewById(R.id.switchRsvp);

            convertView.setTag(cellItemsViewHolder);
        } else {
            cellItemsViewHolder = (CellItemsViewHolder) convertView.getTag();
        }

        if (userEvent != null) {

            DateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.fmt_date_format));

            cellItemsViewHolder.imageViewEvent.setImageDrawable(CommonHelper.getIconDrawableByEventCategory(this.context, userEvent.event.getEventCategory()));
            cellItemsViewHolder.textViewGroupName.setText(userEvent.event.getGroupName());
            cellItemsViewHolder.textViewEventName.setText(userEvent.event.getEventName());
            cellItemsViewHolder.textViewEventStartDateTime.setText(dateFormat.format(userEvent.event.getEventStartDatetime()));
            cellItemsViewHolder.textViewEventDescription.setText(userEvent.event.getEventDescription());
            cellItemsViewHolder.switchRsvp.setTextOff(Iconify.compute(context, context.getString(R.string.icon_fa_not_going)));
            cellItemsViewHolder.switchRsvp.setTextOn(Iconify.compute(context, context.getString(R.string.icon_fa_going)));
            cellItemsViewHolder.switchRsvp.setOnCheckedChangeListener(null);
            cellItemsViewHolder.switchRsvp.setChecked(userEvent.getIsAttending());
            cellItemsViewHolder.switchRsvp.setOnCheckedChangeListener(new SwitchRsvpOnCheckedChangeListener());
        }

        return convertView;
    }

    private class SwitchRsvpOnCheckedChangeListener implements IAsyncRequestResponse, CompoundButton.OnCheckedChangeListener {

        private boolean isAttending;
        private int position;

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isOn) {

            this.isAttending = isOn;
            this.position = listView.getPositionForView(buttonView);

            // Show the progress bar
            progressDialog = ProgressDialog.show(context, buttonView.getResources().getString(R.string.title_rsvp), buttonView.getResources().getString(R.string.msg_processing_request));

            UserService userService = new UserService();
            userService.setUserEventRsvp(userEvents.get(position).getUserId(), userEvents.get(position).event.getEventId(), isOn, this);
        }

        @Override
        public void responseHandler(String response) {

            // Dismiss progress dialogue
            if (progressDialog != null) progressDialog.dismiss();

            if (response.equalsIgnoreCase("OK")) {

                UserEventModel changedUserEvent = userEvents.get(position);
                int attendeeCount = changedUserEvent.event.getAttendeeCount();
                changedUserEvent.setIsAttending(this.isAttending);
                changedUserEvent.event.setAttendeeCount(this.isAttending ? attendeeCount + 1 : attendeeCount - 1);
                for (UserEventModel userEventFromFull : userEventsFull) {
                    if (userEventFromFull.event.getEventId() == changedUserEvent.event.getEventId()) {
                        userEventFromFull.setIsAttending(changedUserEvent.getIsAttending());
                        userEventFromFull.event.setAttendeeCount(changedUserEvent.event.getAttendeeCount());
                        break;
                    }
                }
            } else {
                Toast.makeText(context, R.string.msg_processing_failed, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void backGroundErrorHandler(Exception ex) {

            // Dismiss progress dialogue
            if (progressDialog != null) progressDialog.dismiss();

            // Print stack trace...may be add logging in future releases
            ex.printStackTrace();

            // display error message
            Toast.makeText(context, R.string.error_something_wrong, Toast.LENGTH_SHORT).show();
        }
    }
}
