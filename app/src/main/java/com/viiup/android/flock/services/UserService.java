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
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by AbdullahMoyeen on 4/13/16.
 */
public class UserService {

    // Constant for holding the Web API end point used in URL
    private final String WEBAPIENDPOINT = "http://192.168.0.5:8080";

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

    public void setUserEventRsvp(int userId, int eventId, boolean isAttending,
                                 IAsyncPutRequestResponse delegate) {
        try {
            // Build the URL
            String urlToCall = WEBAPIENDPOINT + "/api/user/events/rsvp?userId=" + userId + "&eventId=" + eventId + "&isAttending=" + isAttending;

            // Call the URL
            AsyncPUTRequest asyncPUTRequest = new AsyncPUTRequest();
            asyncPUTRequest.setDelegate(delegate);
            asyncPUTRequest.execute(urlToCall);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setUserGroupMembership(int userId, int groupId, boolean isMember,
                                       IAsyncPutRequestResponse delegate) {
        // Build URL
        String urlToCall = WEBAPIENDPOINT + "/api/user/groups/membership?userId=" + userId + "&groupId=" + groupId + "&isMember=" + isMember;

        // Call the URL
        AsyncPUTRequest asyncPUTRequest = new AsyncPUTRequest();
        asyncPUTRequest.setDelegate(delegate);
        asyncPUTRequest.execute(urlToCall);
    }

    /*
        Service method for calling REST API with "Put" request
     */
    private String makePutAPICall(String urlToCall) {
        String urlString = urlToCall;
        HttpURLConnection putRequestConnection = null;

        // HTTP Put
        try {
            // Construct URL from string
            URL url = new URL(urlString);

            // Open HTTP connection and provide authentication information.
            putRequestConnection = (HttpURLConnection) url.openConnection();
            String userCredentials = "aam065000@utdallas.edu:Abcd1234";
            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));
            putRequestConnection.setRequestProperty("Authorization", basicAuth);
            putRequestConnection.setRequestMethod("PUT");

            // Make the HTTP Put call
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(putRequestConnection.getOutputStream());
            outputStreamWriter.flush();
            outputStreamWriter.close();

            // Get the response
            int respCode = putRequestConnection.getResponseCode();
            String respMsg = putRequestConnection.getResponseMessage();

            // Return response
            return respMsg;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        } finally {
            // release the connection if made
            if (putRequestConnection != null) putRequestConnection.disconnect();
        }
    }

    /*
        Service method that makes API call and returns JSON string
     */
    private String makeGetAPICall(String urlToCall) {
        String urlString = urlToCall;
        String resultToDisplay = "";
        InputStream in = null;
        HttpURLConnection myURLConnection = null;

        // HTTP Get
        try {

            // Construct URL from string
            URL url = new URL(urlString);

            //Construct and fill up HttpURLConnection instance
            myURLConnection = (HttpURLConnection) url.openConnection();
            String userCredentials = "aam065000@utdallas.edu:Abcd1234";
            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));
            myURLConnection.setRequestProperty("Authorization", basicAuth);
            myURLConnection.setRequestMethod("GET");

            // Make the call to the server
            in = new BufferedInputStream(myURLConnection.getInputStream());

            // Read the data from input stream
            if (in != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                resultToDisplay = br.readLine();
            }
        } catch (Exception e) {

            System.out.println(e.getMessage());
            return e.getMessage();
        } finally {
            // Release the connection if created..
            if (myURLConnection != null) myURLConnection.disconnect();
        }

        // Return the response - JSON in this case.
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
    The task makes PUT request for setting up RSVP status for event.
    */
    private class AsyncPUTRequest extends AsyncTask {
        // Delegate to represent caller
        private IAsyncPutRequestResponse delegate = null;

        public void setDelegate(IAsyncPutRequestResponse delegate) {
            this.delegate = delegate;
        }

        // HTTP PUT call
        @Override
        protected Object doInBackground(Object[] params) {

            // Get the URL to call
            String urlToCall = params[0].toString();

            // Call the PUT API
            return makePutAPICall(urlToCall);
        }

        @Override
        protected void onPostExecute(Object o) {
            delegate.putRequestResponse(o.toString());
        }
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
                String urlToCall = WEBAPIENDPOINT + "/api/user/groups?userId=" + params[0];
                String resultsToParse = makeGetAPICall(urlToCall);

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
                String urlString = WEBAPIENDPOINT + "/api/user/events?userId=" + params[0];
                String resultToDisplay = makeGetAPICall(urlString);

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
