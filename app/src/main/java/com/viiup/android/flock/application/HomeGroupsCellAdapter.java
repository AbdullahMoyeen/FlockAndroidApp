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
import com.viiup.android.flock.models.UserGroupModel;
import com.viiup.android.flock.services.UserService;

import java.util.List;

/**
 * Created by AbdullahMoyeen on 4/12/16.
 */
public class HomeGroupsCellAdapter extends BaseAdapter {

    private static class CellItemsViewHolder {
        ImageView imageViewGroup;
        TextView textViewGroupCategory;
        TextView textViewGroupName;
        TextView textViewGroupMembersCount;
        TextView textViewGroupDescription;
        Switch switchMembership;
    }

    Context context;
    ListView listView;
    List<UserGroupModel> userGroups;

    HomeGroupsCellAdapter(Context context, ListView listView, List<UserGroupModel> userGroups) {
        this.context = context;
        this.listView = listView;
        this.userGroups = userGroups;
    }

    @Override
    public int getCount() {
        return userGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return userGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CellItemsViewHolder cellItemsViewHolder;
        final UserGroupModel userGroup = userGroups.get(position);

        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.home_groups_cell, null);

            cellItemsViewHolder = new CellItemsViewHolder();
            cellItemsViewHolder.imageViewGroup = (ImageView) convertView.findViewById(R.id.imageViewGroup);
            cellItemsViewHolder.textViewGroupCategory = (TextView) convertView.findViewById(R.id.textViewGroupCategory);
            cellItemsViewHolder.textViewGroupName = (TextView) convertView.findViewById(R.id.textViewGroupName);
            cellItemsViewHolder.textViewGroupMembersCount = (TextView) convertView.findViewById(R.id.textViewGroupMembersCount);
            cellItemsViewHolder.textViewGroupDescription = (TextView) convertView.findViewById(R.id.textViewGroupDescription);
            cellItemsViewHolder.switchMembership = (Switch) convertView.findViewById(R.id.switchMembership);

            convertView.setTag(cellItemsViewHolder);
        } else {
            cellItemsViewHolder = (CellItemsViewHolder) convertView.getTag();
        }

        if (userGroup != null) {

            cellItemsViewHolder.imageViewGroup.setImageDrawable(CommonHelper.getIconDrawableByGroupCategory(this.context, userGroup.group.getGroupCategory()));
            cellItemsViewHolder.textViewGroupCategory.setText(userGroup.group.getGroupCategory());
            cellItemsViewHolder.textViewGroupName.setText(userGroup.group.getGroupName());
            cellItemsViewHolder.textViewGroupMembersCount.setText(Integer.toString(userGroup.group.getActiveMemberCount()) + " members");
            cellItemsViewHolder.textViewGroupDescription.setText(userGroup.group.getGroupDescription());
            cellItemsViewHolder.switchMembership.setOnCheckedChangeListener(null);
            cellItemsViewHolder.switchMembership.setChecked(userGroup.isMember());
            cellItemsViewHolder.switchMembership.setOnCheckedChangeListener(switchRsvpOnCheckedChangeListener);
        }

        return convertView;
    }

    private CompoundButton.OnCheckedChangeListener switchRsvpOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isOn) {

            final int position = listView.getPositionForView(buttonView);

//            Toast.makeText(context, "changed event " + userEvents.get(position).event.getEventId() + " to " + isOn, Toast.LENGTH_SHORT).show();

            UserService userService = new UserService();
            userService.setUserGroupMemberShip(userGroups.get(position).getUserId(), userGroups.get(position).group.getGroupId(), isOn);

            userGroups.get(position).setMember(isOn);
        }
    };
}
