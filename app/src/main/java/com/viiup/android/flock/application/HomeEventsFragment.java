package com.viiup.android.flock.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
        HomeEventsFragment fragment = new HomeEventsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_events_fragment, container, false);
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

        adapter = new HomeEventsCellAdapter(getActivity(), userEvents);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + (position + 1), Toast.LENGTH_SHORT).show();
    }
}
