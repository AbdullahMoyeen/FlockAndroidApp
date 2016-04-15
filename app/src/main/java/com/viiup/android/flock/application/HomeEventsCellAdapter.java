package com.viiup.android.flock.application;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.viiup.android.flock.helpers.CommonHelper;
import com.viiup.android.flock.models.UserEventModel;
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

    Context context;
    List<UserEventModel> userEvents;

    HomeEventsCellAdapter(Context context, List<UserEventModel> userEvents) {
        this.context = context;
        this.userEvents = userEvents;
    }

    @Override
    public int getCount() {
        return userEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return userEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userEvents.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CellItemsViewHolder cellItemsViewHolder;

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

//            cellItemsViewHolder.switchRsvp.setOnCheckedChangeListener(switchRsvpOnCheckedChangeListener);

            convertView.setTag(cellItemsViewHolder);
        } else {
            cellItemsViewHolder = (CellItemsViewHolder) convertView.getTag();
        }

        final UserEventModel userEvent = userEvents.get(position);

        if (userEvent != null) {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

            cellItemsViewHolder.imageViewEvent.setImageDrawable(CommonHelper.getIconDrawableByEventCategory(this.context, userEvent.event.getEventCategory()));
            cellItemsViewHolder.textViewGroupName.setText(userEvent.event.getGroupName());
            cellItemsViewHolder.textViewEventName.setText(userEvent.event.getEventName());
            cellItemsViewHolder.textViewEventStartDateTime.setText(dateFormat.format(userEvent.event.getEventStartDatetime()));
            cellItemsViewHolder.textViewEventDescription.setText(userEvent.event.getEventDescription());
            cellItemsViewHolder.switchRsvp.setChecked(userEvent.isAttending());
        }

        return convertView;
    }

    private CompoundButton.OnCheckedChangeListener switchRsvpOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isOn) {

            Toast.makeText(context, "Clicked: eventId switch " + isOn, Toast.LENGTH_SHORT).show();

//            Toast.makeText(context, "Clicked: eventId " + userEvent.event.getEventId() + " " + isOn, Toast.LENGTH_SHORT).show();
//            UserService userService = new UserService();
//            userService.setUserRsvp(userEvent.getUserId(), userEvent.event.getEventId(), isOn);
        }
    };
}
