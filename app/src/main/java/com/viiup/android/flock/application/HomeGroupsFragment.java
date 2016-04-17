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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.viiup.android.flock.models.UserGroupModel;
import com.viiup.android.flock.models.UserModel;
import com.viiup.android.flock.services.UserService;

import java.util.List;

/**
 * Created by AbdullahMoyeen on 4/11/16.
 */
public class HomeGroupsFragment extends ListFragment implements AdapterView.OnItemClickListener {

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
        userGroups = userService.getUserGroupsByUserId(loggedInUser.getUserId());

        adapter = new HomeGroupsCellAdapter(getActivity(), getListView(), userGroups);
        setListAdapter(adapter);
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
            boolean isMember = data.getBooleanExtra("isMember", userGroups.get(requestCode).isMember());
            userGroups.get(requestCode).setMember(isMember);
            this.getListView().setAdapter(this.getListView().getAdapter());
        }
    }
}
