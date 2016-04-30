package com.viiup.android.flock.application;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viiup.android.flock.models.UserEventModel;
import com.viiup.android.flock.models.UserModel;
import com.viiup.android.flock.services.IAsyncEventResponse;
import com.viiup.android.flock.services.UserService;

import java.util.List;

/**
 * Created by AbdullahMoyeen on 4/11/16.
 */
public class HomeEventsFragment extends ListFragment implements IAsyncEventResponse, SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    private ListView eventsListView;
    private HomeEventsCellAdapter adapter;
    private List<UserEventModel> userEvents;
    private List<UserEventModel> userEventsFull;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserModel authenticatedUser;

    public HomeEventsFragment() {
    }

    public static HomeEventsFragment newInstance() {
        return new HomeEventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressDialog = ProgressDialog.show(getActivity(), getString(R.string.title_events_groups), getString(R.string.msg_loading_data));
        return inflater.inflate(R.layout.home_events_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        // Find the swipe refresh layout
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_container_events);

        // Hook up the refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutListener());

        // Set color skim for refresh ui
        swipeRefreshLayout.setColorSchemeResources(R.color.colorFlockBird1, R.color.colorFlockBird2, R.color.colorFlockBird3, R.color.colorFlockBird4);

        SharedPreferences mPref = getActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        String authenticatedUserJson = mPref.getString("authenticatedUserJson", null);

        Gson gson = new Gson();
        authenticatedUser = gson.fromJson(authenticatedUserJson, UserModel.class);

        UserService userService = new UserService();
        userService.getUserEventsByUserId(authenticatedUser.getUserId(), this);

        eventsListView = getListView();
        eventsListView.setOnItemClickListener(this);

        // Attach scroll listener for list view to block swipe refresh from activating on scroll up
        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enabled = false;
                if (getListView() != null && getListView().getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = getListView().getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = getListView().getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enabled = firstItemVisible && topOfFirstItemVisible;
                }

                // Enable the layout
                swipeRefreshLayout.setEnabled(enabled);
            }
        });
    }

    public boolean onQueryTextChange(String newText) {
        filterEvents(newText);
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public void filterEvents(String newText) {

        if (userEventsFull != null && newText != null) {

            userEvents.clear();

            for (UserEventModel userEvent : userEventsFull) {
                if (userEvent.event.getEventName().toLowerCase().contains(newText.toLowerCase())
                        || userEvent.event.getEventDescription().toLowerCase().contains(newText.toLowerCase())
                        || userEvent.event.getEventKeywords().toLowerCase().contains(newText.toLowerCase())
                        || userEvent.event.getGroupName().toLowerCase().contains(newText.toLowerCase())) {
                    userEvents.add(userEvent);
                }
            }

            adapter = new HomeEventsCellAdapter(getActivity(), getListView(), userEvents, userEventsFull);
            setListAdapter(adapter);
        }
    }

    public void filterMyEvents() {

        if (userEventsFull != null) {

            userEvents.clear();

            for (UserEventModel userEvent : userEventsFull) {
                if (userEvent.getIsAttending()) {
                    userEvents.add(userEvent);
                }
            }

            adapter = new HomeEventsCellAdapter(getActivity(), getListView(), userEvents, userEventsFull);
            setListAdapter(adapter);
        }
    }

    public void resetToFull() {
        Gson gson = new Gson();
        String userEventsJson = gson.toJson(userEventsFull);
        userEvents = gson.fromJson(userEventsJson, new TypeToken<List<UserEventModel>>() {
        }.getType());
        adapter = new HomeEventsCellAdapter(getActivity(), getListView(), userEvents, userEventsFull);
        setListAdapter(adapter);
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

                UserEventModel changedUserEvent = userEvents.get(requestCode);
                int attendeeCount = changedUserEvent.event.getAttendeeCount();
                changedUserEvent.setIsAttending(!changedUserEvent.getIsAttending());
                changedUserEvent.event.setAttendeeCount(changedUserEvent.getIsAttending() ? attendeeCount + 1 : attendeeCount - 1);
                for (UserEventModel userEventFromFull : userEventsFull) {
                    if (userEventFromFull.event.getEventId() == changedUserEvent.event.getEventId()) {
                        userEventFromFull.setIsAttending(changedUserEvent.getIsAttending());
                        userEventFromFull.event.setAttendeeCount(changedUserEvent.event.getAttendeeCount());
                        break;
                    }
                }
                adapter = new HomeEventsCellAdapter(getActivity(), getListView(), userEvents, userEventsFull);
                setListAdapter(adapter);
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
            Gson gson = new Gson();
            String userEventsJson = gson.toJson(userEvents);
            this.userEvents = gson.fromJson(userEventsJson, new TypeToken<List<UserEventModel>>() {
            }.getType());
            this.userEventsFull = gson.fromJson(userEventsJson, new TypeToken<List<UserEventModel>>() {
            }.getType());
            ((HomeActivity) this.getActivity()).userEvents = gson.fromJson(userEventsJson, new TypeToken<List<UserEventModel>>() {
            }.getType());
            adapter = new HomeEventsCellAdapter(getActivity(), getListView(), userEvents, userEventsFull);
            setListAdapter(adapter);
        } else {
            Toast.makeText(this.getContext(), R.string.msg_no_event, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void backGroundErrorHandler(Exception ex) {

        if (progressDialog != null) progressDialog.dismiss();

        // Print stack trace...may be add logging in future releases
        ex.printStackTrace();

        // display error message
        Toast.makeText(this.getContext(), R.string.error_something_wrong, Toast.LENGTH_SHORT).show();
    }

    /*
        Helper class for implementing the OnRefreshListener for swipe refresh layout and
        IAsycEventResponse to handle the refresh request.
     */
    private class SwipeRefreshLayoutListener implements SwipeRefreshLayout.OnRefreshListener, IAsyncEventResponse {

        @Override
        public void postUserEvents(List<UserEventModel> refreshedEvents) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            if (refreshedEvents != null && refreshedEvents.size() > 0) {
                Gson gson = new Gson();
                String userEventsJson = gson.toJson(refreshedEvents);
                userEvents = gson.fromJson(userEventsJson, new TypeToken<List<UserEventModel>>() {
                }.getType());
                userEventsFull = gson.fromJson(userEventsJson, new TypeToken<List<UserEventModel>>() {
                }.getType());
                ((HomeActivity) getActivity()).userEvents = gson.fromJson(userEventsJson, new TypeToken<List<UserEventModel>>() {
                }.getType());
                adapter = new HomeEventsCellAdapter(getActivity(), getListView(), userEvents, userEventsFull);
                setListAdapter(adapter);
            } else {
                Toast.makeText(getContext(), R.string.msg_no_event, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void backGroundErrorHandler(Exception ex) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            // Print stack trace...may be add logging in future releases
            ex.printStackTrace();

            // display error message
            Toast.makeText(getContext(), R.string.error_something_wrong, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRefresh() {

            // Reload events
            UserService userService = new UserService();
            userService.getUserEventsByUserId(authenticatedUser.getUserId(), this);
        }
    }
}
