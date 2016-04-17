package com.viiup.android.flock.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.viiup.android.flock.helpers.CommonHelper;
import com.viiup.android.flock.models.UserEventModel;
import com.viiup.android.flock.models.UserGroupModel;
import com.viiup.android.flock.services.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class GroupDetailsActivity extends AppCompatActivity {

    private UserGroupModel userGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        setContentView(R.layout.group_details_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Gson gson = new Gson();
        String userGroupJson = getIntent().getStringExtra("userGroupJson");
        userGroup = gson.fromJson(userGroupJson, UserGroupModel.class);

        TextView textViewSecondaryBar = (TextView) findViewById(R.id.secondaryBar);
        TextView textViewMembersCount = (TextView) findViewById(R.id.textViewMembersCount);
        TextView textViewEventsCount = (TextView) findViewById(R.id.textViewEventsCount);
        TextView textViewGroupDescription = (TextView) findViewById(R.id.textViewGroupDescription);
        ImageView imageViewGroup = (ImageView) findViewById(R.id.imageViewGroup);
        Switch switchMembership = (Switch) findViewById(R.id.switchMembership);

        textViewSecondaryBar.setText(userGroup.group.getGroupName().toUpperCase());
        textViewMembersCount.setText(Integer.toString(userGroup.group.getActiveMemberCount()) + " members");
        textViewEventsCount.setText(Integer.toString(userGroup.group.getUpcomingEventCount()) + " upcoming events");
        textViewGroupDescription.setText(userGroup.group.getGroupDescription());
        imageViewGroup.setImageDrawable(CommonHelper.getIconDrawableByGroupCategory(this, userGroup.group.getGroupCategory()));
        switchMembership.setChecked(userGroup.isMember());
        switchMembership.setOnCheckedChangeListener(switchMembershipOnCheckedChangeListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            // up button
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("isMember", userGroup.isMember());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    private CompoundButton.OnCheckedChangeListener switchMembershipOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isOn) {

//            Toast.makeText(buttonView.getContext(), "changed event " + userGroup.event.getEventId() + " to " + isOn, Toast.LENGTH_SHORT).show();

            UserService userService = new UserService();
            userService.setUserGroupMemberShip(userGroup.getUserId(), userGroup.group.getGroupId(), isOn);

            userGroup.setMember(isOn);
        }
    };
}
