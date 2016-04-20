package com.viiup.android.flock.application;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.viiup.android.flock.models.UserEventModel;
import com.viiup.android.flock.models.UserModel;
import com.viiup.android.flock.services.IAsyncEventResponse;
import com.viiup.android.flock.services.UserService;

import java.util.List;

/**
 * Created by AbdullahMoyeen on 4/11/16.
 */
public class HomeEventsFragment extends ListFragment implements AdapterView.OnItemClickListener, IAsyncEventResponse {

    HomeEventsCellAdapter adapter;
    public List<UserEventModel> userEvents;
    private ProgressDialog progressDialog = null;

    public HomeEventsFragment() {
    }

    public static HomeEventsFragment newInstance() {
        return new HomeEventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressDialog = ProgressDialog.show(getActivity(), "EVENTS", getString(R.string.msg_loading_data));
        return inflater.inflate(R.layout.home_events_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        SharedPreferences mPref = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        String loggedInUserJson = mPref.getString("loggedInUserJson", null);

        Gson gson = new Gson();
        UserModel loggedInUser = gson.fromJson(loggedInUserJson, UserModel.class);

        UserService userService = new UserService();
        userService.getUserEventsByUserId(loggedInUser.getUserId(), this);

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

            boolean isAttendingChanged = data.getBooleanExtra("isAttendingChanged", false);

            if (isAttendingChanged) {
                userEvents.get(requestCode).setIsAttending(!userEvents.get(requestCode).getIsAttending());
                int attendeeCount = userEvents.get(requestCode).event.getAttendeeCount();
                userEvents.get(requestCode).event.setAttendeeCount(userEvents.get(requestCode).getIsAttending() ? attendeeCount + 1 : attendeeCount - 1);
                this.getListView().setAdapter(this.getListView().getAdapter());
            }
        }
    }

    @Override
    public void postUserEvents(List<UserEventModel> userEvents) {
        // dismiss the progress
        if (progressDialog != null)
            progressDialog.dismiss();

        if (userEvents != null && userEvents.size() > 0) {
            // Bind the adapter to list view
            this.userEvents = userEvents;
            ((HomeActivity) this.getActivity()).userEvents = userEvents;
            adapter = new HomeEventsCellAdapter(getActivity(), getListView(), userEvents);
            setListAdapter(adapter);
        } else {
            Toast.makeText(this.getContext(), R.string.msg_no_event, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void backGroundErrorHandler(Exception ex) {

        if(progressDialog != null) progressDialog.dismiss();

        // Print stack trace...may be add logging in future releases
        ex.printStackTrace();

        // display error message
        Toast.makeText(this.getContext(), R.string.msg_something_wrong, Toast.LENGTH_SHORT).show();
    }
}
