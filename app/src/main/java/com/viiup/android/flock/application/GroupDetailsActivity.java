package com.viiup.android.flock.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joanzapata.iconify.Iconify;
import com.viiup.android.flock.helpers.CommonHelper;
import com.viiup.android.flock.models.UserGroupModel;
import com.viiup.android.flock.services.IAsyncPutRequestResponse;
import com.viiup.android.flock.services.UserService;

public class GroupDetailsActivity extends AppCompatActivity {

    private static class ItemsViewHolder {
        TextView textViewSecondaryBar;
        TextView textViewMembersCount;
        TextView textViewEventsCount;
        TextView textViewGroupDescription;
        ImageView imageViewGroup;
        Switch switchMembership;
    }

    private Context context;
    private ItemsViewHolder itemsViewHolder;
    private UserGroupModel userGroup;
    private String membershipStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = this;

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        setContentView(R.layout.group_details_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Gson gson = new Gson();
        String userGroupJson = getIntent().getStringExtra("userGroupJson");
        userGroup = gson.fromJson(userGroupJson, UserGroupModel.class);

        itemsViewHolder = new ItemsViewHolder();
        itemsViewHolder.textViewSecondaryBar = (TextView) findViewById(R.id.secondaryBar);
        itemsViewHolder.textViewMembersCount = (TextView) findViewById(R.id.textViewMembersCount);
        itemsViewHolder.textViewEventsCount = (TextView) findViewById(R.id.textViewEventsCount);
        itemsViewHolder.textViewGroupDescription = (TextView) findViewById(R.id.textViewGroupDescription);
        itemsViewHolder.imageViewGroup = (ImageView) findViewById(R.id.imageViewGroup);
        itemsViewHolder.switchMembership = (Switch) findViewById(R.id.switchMembership);

        itemsViewHolder.textViewSecondaryBar.setText(userGroup.group.getGroupName().toUpperCase());
        itemsViewHolder.textViewMembersCount.setText(Integer.toString(userGroup.group.getActiveMemberCount()) + " joined");
        itemsViewHolder.textViewEventsCount.setText(Integer.toString(userGroup.group.getUpcomingEventCount()) + " upcoming");
        itemsViewHolder.textViewGroupDescription.setText(userGroup.group.getGroupDescription());
        itemsViewHolder.imageViewGroup.setImageDrawable(CommonHelper.getIconDrawableByGroupCategory(this, userGroup.group.getGroupCategory()));
        itemsViewHolder.switchMembership.setChecked(!userGroup.getGroupMembershipStatus().equals("I"));
        if (userGroup.getGroupMembershipStatus().equals("A")) {
            itemsViewHolder.switchMembership.setTextOn(Iconify.compute(context, context.getResources().getString(R.string.fa_icon_on)));
        }
        if (userGroup.getGroupMembershipStatus().equals("P")) {
            itemsViewHolder.switchMembership.setTextOn(Iconify.compute(context, context.getResources().getString(R.string.fa_icon_pending)));
            itemsViewHolder.switchMembership.setEnabled(false);
        } else {
            itemsViewHolder.switchMembership.setEnabled(true);
        }
        itemsViewHolder.switchMembership.setOnCheckedChangeListener(new SwitchMembershipOnCheckedChangeListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            // up button
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("membershipStatus", membershipStatus);
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

    private class SwitchMembershipOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener, IAsyncPutRequestResponse {

        private boolean isMember;

        @Override
        public void putRequestResponse(String response) {

            if (response.equalsIgnoreCase("OK")) {
                if (isMember) {
                    Toast.makeText(context, "your join request has been sent for approval", Toast.LENGTH_SHORT).show();
                    int pendingMemberCount = userGroup.group.getPendingMemberCount();
                    userGroup.setGroupMembershipStatus("P");
                    userGroup.group.setPendingMemberCount(pendingMemberCount + 1);
                    itemsViewHolder.switchMembership.setEnabled(false);
                } else {
                    int activeMemberCount = userGroup.group.getActiveMemberCount();
                    userGroup.setGroupMembershipStatus("I");
                    userGroup.group.setActiveMemberCount(activeMemberCount - 1);
                    itemsViewHolder.textViewMembersCount.setText(userGroup.group.getActiveMemberCount() + " joined");
                }

                membershipStatus = userGroup.getGroupMembershipStatus();
            } else {
                Toast.makeText(context, "your membership could not be processed, please try again later", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void backGroundErrorHandler(Exception ex) {
            // Print stack trace...may be add logging in future releases
            ex.printStackTrace();

            // display error message
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isOn) {

            this.isMember = isOn;
            UserService userService = new UserService();
            userService.setUserGroupMembership(userGroup.getUserId(), userGroup.group.getGroupId(), isOn, this);
        }
    }
}
