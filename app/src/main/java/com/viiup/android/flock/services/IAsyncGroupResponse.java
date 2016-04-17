package com.viiup.android.flock.services;

import com.viiup.android.flock.models.UserEventModel;
import com.viiup.android.flock.models.UserGroupModel;

import java.util.List;

/**
 * Created by Niranjan on 4/16/2016. The interface is used for communicating between the
 * async task and user interface calling it. This is base interface.
 */
public interface IAsyncGroupResponse {
    void postUserGroups(List<UserGroupModel> userGroups);
}
