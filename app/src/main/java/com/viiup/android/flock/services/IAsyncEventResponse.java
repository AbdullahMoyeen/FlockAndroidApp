package com.viiup.android.flock.services;

import com.viiup.android.flock.models.UserEventModel;

import java.util.List;

/**
 * Created by Niranjan on 4/16/2016.
 * Concrete interface for providing communication for events.
 */
public interface IAsyncEventResponse {
    void postUserEvents(List<UserEventModel> userEvents);
}
