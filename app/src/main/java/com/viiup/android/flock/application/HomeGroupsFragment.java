package com.viiup.android.flock.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.viiup.android.flock.models.UserGroupModel;
import com.viiup.android.flock.models.UserModel;
import com.viiup.android.flock.services.IAsyncGroupResponse;
import com.viiup.android.flock.services.UserService;

import java.util.List;

/**
 * Created by AbdullahMoyeen on 4/11/16.
 */
public class HomeGroupsFragment extends ListFragment implements AdapterView.OnItemClickListener,
        IAsyncGroupResponse {

    HomeGroupsCellAdapter adapter;
    private List<UserGroupModel> userGroups;

    public HomeGroupsFragment() {
    }

    public static HomeGroupsFragment newInstance() {
        return new HomeGroupsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_groups_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        SharedPreferences mPref = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        String loggedInUserJson = mPref.getString("loggedInUserJson", null);

        Gson gson = new Gson();
        UserModel loggedInUser = gson.fromJson(loggedInUserJson, UserModel.class);

        UserService userService = new UserService();
        userService.getUserGroupsByUserId(loggedInUser.getUserId(), this);

        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Gson gson = new Gson();
        UserGroupModel userGroup = userGroups.get(position);
        String userGroupJson = gson.toJson(userGroup);

        Intent groupDetailsIntent = new Intent(this.getContext(), GroupDetailsActivity.class);
        groupDetailsIntent.putExtra("userGroupJson", userGroupJson);
        startActivityForResult(groupDetailsIntent, position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            String membershipStatus = data.getStringExtra("membershipStatus");
            if (membershipStatus != null) {
                userGroups.get(requestCode).setGroupMembershipStatus(membershipStatus);
                if (membershipStatus.equals("P")) {
                    int pendingMemberCount = userGroups.get(requestCode).group.getPendingMemberCount();
                    userGroups.get(requestCode).group.setPendingMemberCount(pendingMemberCount + 1);
                } else if (membershipStatus.equals("I")) {
                    int activeMemberCount = userGroups.get(requestCode).group.getActiveMemberCount();
                    userGroups.get(requestCode).group.setActiveMemberCount(activeMemberCount - 1);
                }

                this.getListView().setAdapter(this.getListView().getAdapter());
            }
        }
    }

    @Override
    public void postUserGroups(List<UserGroupModel> userGroups) {
        if (userGroups != null && userGroups.size() > 0) {
            this.userGroups = userGroups;
            adapter = new HomeGroupsCellAdapter(getActivity(), getListView(), userGroups);
            setListAdapter(adapter);
        } else {
            Toast.makeText(this.getContext(), "No groups are available.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void backGroundErrorHandler(Exception ex) {
        // Print stack trace...may be add logging in future releases
        ex.printStackTrace();

        // display error message
        Toast.makeText(this.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
    }
}
