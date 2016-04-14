package com.viiup.android.flock.application;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.viiup.android.flock.helpers.CommonHelper;
import com.viiup.android.flock.models.UserEventModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by AbdullahMoyeen on 4/12/16.
 */
public class HomeEventsCellAdapter extends BaseAdapter {

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

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.home_events_cell, null);
        }

        ImageView imageViewEvent = (ImageView) convertView.findViewById(R.id.imageViewEvent);
        TextView textViewGroupName = (TextView) convertView.findViewById(R.id.textViewGroupName);
        TextView textViewEventName = (TextView) convertView.findViewById(R.id.textViewEventName);
        TextView textViewEventStartDateTime = (TextView) convertView.findViewById(R.id.textViewEventStartDateTime);
        TextView textViewEventDesc = (TextView) convertView.findViewById(R.id.textViewEventDesc);

        UserEventModel userEvent = userEvents.get(position);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        imageViewEvent.setImageDrawable(CommonHelper.getIconDrawableByEventCategory(this.context, userEvent.event.getEventCategory()));
        textViewGroupName.setText(userEvent.event.getGroupName());
        textViewEventName.setText(userEvent.event.getEventName());
        textViewEventStartDateTime.setText(dateFormat.format(userEvent.event.getEventStartDatetime()));
        textViewEventDesc.setText(userEvent.event.getEventDescription());

        return convertView;
    }
}
