package com.viiup.android.flock.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.viiup.android.flock.models.UserEventModel;
import com.viiup.android.flock.models.UserModel;
import com.viiup.android.flock.services.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AbdullahMoyeen on 4/11/16.
 */
public class HomeEventsFragment extends ListFragment implements AdapterView.OnItemClickListener {

    HomeEventsCellAdapter adapter;
    private List<UserEventModel> userEvents;

    public HomeEventsFragment() {
    }

    public static HomeEventsFragment newInstance() {
        return new HomeEventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_events_fragment, container, false);

        Button buttonNearbyEvents = (Button) view.findViewById(R.id.buttonNearbyEvents);
        buttonNearbyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(getContext(), MapActivity.class);
                startActivity(mapIntent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        SharedPreferences mPref = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        String loggedInUserJson = mPref.getString("loggedInUserJson", null);

        Gson gson = new Gson();
        UserModel loggedInUser = gson.fromJson(loggedInUserJson, UserModel.class);

        UserService userService = new UserService();
        userEvents = userService.getUserEventsByUserId(loggedInUser.getUserId());

        adapter = new HomeEventsCellAdapter(getActivity(), getListView(), userEvents);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Gson gson = new Gson();
        UserEventModel userEvent = userEvents.get(position);
        String userEventJson = gson.toJson(userEvent);

        Intent eventDetailsIntent = new Intent(this.getContext(), EventDetailsActivity.class);
        eventDetailsIntent.putExtra("userEventJson", userEventJson);
        startActivityForResult(eventDetailsIntent, position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            boolean isAttending = data.getBooleanExtra("isAttending", userEvents.get(requestCode).isAttending());
            userEvents.get(requestCode).setIsAttending(isAttending);
            this.getListView().setAdapter(this.getListView().getAdapter());
        }
    }
}
