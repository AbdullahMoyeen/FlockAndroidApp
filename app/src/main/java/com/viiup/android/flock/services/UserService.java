package com.viiup.android.flock.services;

import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.viiup.android.flock.models.UserEventModel;
import com.viiup.android.flock.models.UserGroupModel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by AbdullahMoyeen on 4/13/16.
 */
public class UserService {

    public void getUserEventsByUserId(int userId, IAsyncEventResponse callback) {
        // Call Events rest API
        try {
            AsyncEventsRESTAPICaller asyncEventsRESTAPICaller = new AsyncEventsRESTAPICaller();
            asyncEventsRESTAPICaller.setDelegate(callback);
            asyncEventsRESTAPICaller.execute(userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getUserGroupsByUserId(int userId, IAsyncGroupResponse callback) {
        // Call Events rest API
        try {
            AsyncGroupsRESTAPICaller asyncGroupsRESTAPICaller = new AsyncGroupsRESTAPICaller();
            asyncGroupsRESTAPICaller.setDelegate(callback);
            asyncGroupsRESTAPICaller.execute(userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setUserEventRsvp(int userId, int eventId, boolean isAttending) {

    }

    public void setUserGroupMemberShip(int userId, int groupId, boolean isMember) {

    }

    /*
        Service method that makes API call and returns JSON string
     */
    private String makeAPICall(String urlToCall) {
        String urlString = urlToCall;
        String resultToDisplay = "";
        InputStream in = null;

        // HTTP Get
        try {

            URL url = new URL(urlString);

            HttpURLConnection myURLConnection = (HttpURLConnection) url.openConnection();
            String userCredentials = "aam065000@utdallas.edu:Abcd1234";
            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));
            myURLConnection.setRequestProperty("Authorization", basicAuth);
            myURLConnection.setRequestMethod("GET");

            in = new BufferedInputStream(myURLConnection.getInputStream());

        } catch (Exception e) {

            System.out.println(e.getMessage());

            return e.getMessage();

        }

        if (in != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            try {
                resultToDisplay = br.readLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return e.getMessage();
            }
        }
        return resultToDisplay;
    }

    /*
        Service method for building Gson from Gsonbuilder to parse dates properly.
     */
    private Gson getGson() {
        try {
            // Creates the json object which will manage the information received
            GsonBuilder builder = new GsonBuilder();

            // Register an adapter to manage the date types as long values
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                        throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            // Get the Gson from builder
            Gson gson = builder.create();

            return gson;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Exception....
        return null;
    }

    /*
    Async task for calling the REST API from application on background thread.
    The task accepts the user Id as integer and returns the list of user group models.
 */
    private class AsyncGroupsRESTAPICaller extends AsyncTask<Integer, String, List<UserGroupModel>> {

        // Call back interface for communicating with caller
        IAsyncGroupResponse delegate = null;

        public void setDelegate(IAsyncGroupResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected List<UserGroupModel> doInBackground(Integer... params) {
            // Make API call
            try {
                String urlToCall = "http://192.168.0.5:8080/api/user/groups?userId=" + params[0];
                String resultsToParse = makeAPICall(urlToCall);

                // Parse the JSON
                Gson gson = getGson();
                if (gson != null) {
                    return gson.fromJson(resultsToParse, new TypeToken<List<UserGroupModel>>() {
                    }.getType());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<UserGroupModel> userGroupModels) {
            try {
                // Post the data
                delegate.postUserGroups(userGroupModels);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /*
        Async task for calling the REST API from application on background thread.
        The task accepts the user Id as integer and returns the list of user event models.
     */
    private class AsyncEventsRESTAPICaller extends AsyncTask<Integer, String, List<UserEventModel>> {

        // Call back interface for communicating with caller
        IAsyncEventResponse delegate = null;

        // Setter for the delegate to return response
        public void setDelegate(IAsyncEventResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected List<UserEventModel> doInBackground(Integer... params) {

            // Make API call
            try {
                String urlString = "http://192.168.0.5:8080/api/user/events?userId=" + params[0];
                String resultToDisplay = makeAPICall(urlString);

                // Parse and return event list

                Gson gson = getGson();
                if (gson != null) {
                    return gson.fromJson(resultToDisplay, new TypeToken<List<UserEventModel>>() {
                    }.getType());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            // Exception case...
            return null;
        }

        @Override
        protected void onPostExecute(List<UserEventModel> userEventModelList) {
            try {
                // Return the events list to the caller
                delegate.postUserEvents(userEventModelList);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
