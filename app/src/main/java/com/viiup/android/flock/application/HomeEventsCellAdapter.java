package com.viiup.android.flock.application;

import android.app.Activity;
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
    ListView listView;
    List<UserEventModel> userEvents;

    HomeEventsCellAdapter(Context context, ListView listView, List<UserEventModel> userEvents) {
        this.context = context;
        this.listView = listView;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CellItemsViewHolder cellItemsViewHolder;
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

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

            cellItemsViewHolder.imageViewEvent.setImageDrawable(CommonHelper.getIconDrawableByEventCategory(this.context, userEvent.event.getEventCategory()));
            cellItemsViewHolder.textViewGroupName.setText(userEvent.event.getGroupName());
            cellItemsViewHolder.textViewEventName.setText(userEvent.event.getEventName());
            cellItemsViewHolder.textViewEventStartDateTime.setText(dateFormat.format(userEvent.event.getEventStartDatetime()));
            cellItemsViewHolder.textViewEventDescription.setText(userEvent.event.getEventDescription());
            cellItemsViewHolder.switchRsvp.setOnCheckedChangeListener(null);
            cellItemsViewHolder.switchRsvp.setChecked(userEvent.isAttending());
            cellItemsViewHolder.switchRsvp.setOnCheckedChangeListener(switchRsvpOnCheckedChangeListener);
        }

        return convertView;
    }

    private CompoundButton.OnCheckedChangeListener switchRsvpOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isOn) {

            final int position = listView.getPositionForView(buttonView);

//            Toast.makeText(context, "changed event " + userEvents.get(position).event.getEventId() + " to " + isOn, Toast.LENGTH_SHORT).show();

            UserService userService = new UserService();
            userService.setUserEventRsvp(userEvents.get(position).getUserId(), userEvents.get(position).event.getEventId(), isOn);

            userEvents.get(position).setIsAttending(isOn);
        }
    };
}
