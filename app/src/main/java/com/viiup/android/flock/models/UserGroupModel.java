package com.viiup.android.flock.models;

/**
 * Created by AbdullahMoyeen on 4/16/16.
 */
public class UserGroupModel {

    private int userId;
    private String groupMembershipStatus;
    public GroupModel group;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getGroupMembershipStatus() {
        return groupMembershipStatus;
    }

    public void setGroupMembershipStatus(String groupMembershipStatus) {
        this.groupMembershipStatus = groupMembershipStatus;
    }
}
