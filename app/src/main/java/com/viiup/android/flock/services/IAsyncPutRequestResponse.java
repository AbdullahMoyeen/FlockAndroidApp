package com.viiup.android.flock.services;

/**
 * Created by Niranjan on 4/18/2016.
 * This interface is used as call back to communicate PUT request response back to caller.
 */
public interface IAsyncPutRequestResponse {
    void putRequestResponse(String response);
}
