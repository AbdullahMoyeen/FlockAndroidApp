package com.viiup.android.flock.models;

import java.util.Date;

/**
 * Created by AbdullahMoyeen on 4/11/16.
 */
public class UserEventModel {

    private int userId;
    private boolean isAttending;
    public EventModel event;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean getIsAttending() {
        return isAttending;
    }

    public void setIsAttending(boolean isAttending) {
        this.isAttending = isAttending;
    }
}
