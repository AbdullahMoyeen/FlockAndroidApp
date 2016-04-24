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
import com.viiup.android.flock.models.UserModel;
import com.viiup.android.flock.models.UserPasswordChangeModel;

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
    final String WEBAPIENDPOINT = "http://flockapi.7uputd.com";
    final String BASICAUTH = "Basic " + new String(Base64.encode("viiup.utd.emse@gmail.com:7UpRocks".getBytes(), Base64.DEFAULT));

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
                                 IAsyncRequestResponse delegate) {
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
                                       IAsyncRequestResponse delegate) {
        // Build URL
        String urlToCall = WEBAPIENDPOINT + "/api/user/groups/membership?userId=" + userId +
                "&groupId=" + groupId + "&isMember=" + isMember;

        // Call the URL
        AsyncPUTRequest asyncPUTRequest = new AsyncPUTRequest();
        asyncPUTRequest.setDelegate(delegate);
        asyncPUTRequest.execute(urlToCall);
    }

    public void signUpUser(UserModel userModel, IAsyncRequestResponse delegate) {

        // Post URL for sign up
        String urlToPost = WEBAPIENDPOINT + "/api/user/signup";

        // Get the JSON for user model
        Gson gson = new Gson();
        String newUserJSON = gson.toJson(userModel);

        // Make POST call on background
        AsyncPostRequest asyncPostRequest = new AsyncPostRequest();
        asyncPostRequest.setDelegate(delegate);
        asyncPostRequest.execute(urlToPost, newUserJSON);
    }

    public void changeUserPassword(UserPasswordChangeModel userPassword, IAsyncRequestResponse delegate) {
        // Post URL for sign up
        String urlToPost = WEBAPIENDPOINT + "/api/user/signup";

        // Get the JSON for user password
        Gson gson = new Gson();
        String newPasswordJSON = gson.toJson(userPassword);

        // Make POST call on background
        AsyncPostRequest asyncPostRequest = new AsyncPostRequest();
        asyncPostRequest.setDelegate(delegate);
        asyncPostRequest.execute(urlToPost, newPasswordJSON);
    }

    /*
        Service method for calling REST API with "POST" request, with JSON payload.
     */
    private String makePostAPICall(String urlToCall, String jsonPayload) throws IOException {
        String urlString = urlToCall;
        HttpURLConnection postRequestConnection = null;
        OutputStreamWriter outputStreamWriter;
        String postResults = null;
        InputStream in = null;

        // HTTP POST
        try {
            // Construct URL from string
            URL url = new URL(urlString);

            // Open HTTP connection and provide authentication information.
            postRequestConnection = (HttpURLConnection) url.openConnection();
            postRequestConnection.setRequestProperty("Content-Type", "application/json");
            postRequestConnection.setRequestProperty("Accept", "application/json");
            postRequestConnection.setRequestProperty("Content-length", jsonPayload.getBytes().length + "");
            postRequestConnection.setUseCaches(false);

            // Provide authentication
            postRequestConnection.setRequestProperty("Authorization", BASICAUTH);
            postRequestConnection.setRequestMethod("POST");

            // Make the HTTP POST call
            outputStreamWriter = new OutputStreamWriter(postRequestConnection.getOutputStream(), "UTF-8");
            outputStreamWriter.write(jsonPayload);
            outputStreamWriter.close();

            // Get the response
            in = new BufferedInputStream(postRequestConnection.getInputStream());

            // Read the data from input stream
            if (in != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                postResults = br.readLine();
            } else return null;

            // Return response
            return postResults;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            // release the connection if made
            if (postRequestConnection != null) postRequestConnection.disconnect();
            if (in != null) in.close();
        }
    }

    /*
        Service method for calling REST API with "Put" request
     */
    private String makePutAPICall(String urlToCall) throws IOException {
        String urlString = urlToCall;
        HttpURLConnection putRequestConnection = null;
        OutputStreamWriter outputStreamWriter = null;

        // HTTP Put
        try {
            // Construct URL from string
            URL url = new URL(urlString);

            // Open HTTP connection and provide authentication information.
            putRequestConnection = (HttpURLConnection) url.openConnection();
            putRequestConnection.setRequestProperty("Authorization", BASICAUTH);
            putRequestConnection.setRequestMethod("PUT");

            // Make the HTTP Put call
            outputStreamWriter = new OutputStreamWriter(putRequestConnection.getOutputStream());
            outputStreamWriter.flush();
            outputStreamWriter.close();

            // Get the response
            int respCode = putRequestConnection.getResponseCode();
            String respMsg = putRequestConnection.getResponseMessage();

            // Return response
            return respMsg;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
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
            myURLConnection.setRequestProperty("Authorization", BASICAUTH);
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
        Helper class for making Async post request using JSON as payload.
     */
    private class AsyncPostRequest extends AsyncTask<String, Void, String> {
        // Delegate to represent caller
        private IAsyncRequestResponse delegate = null;

        // Exception to throw from PostExecute
        Exception backGrounndException = null;

        // Set the delegate i.e. activity that requested async task to post response
        public void setDelegate(IAsyncRequestResponse delegate) {
            this.delegate = delegate;
        }

        // HTTP Post request
        @Override
        protected String doInBackground(String... params) {
            String urlToCall = params[0];
            String jsonPayload = params[1];
            String responseJSON = null;

            // Make POST API call
            try {
                responseJSON = makePostAPICall(urlToCall, jsonPayload);
                return responseJSON;
            } catch (Exception e) {
                backGrounndException = e;
                e.printStackTrace();
            }

            // Exception case..
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String postResponse) {
            // Post the results if there is no exception
            if (backGrounndException != null)
                delegate.responseHandler(postResponse);
            else
                delegate.backGroundErrorHandler(backGrounndException);
        }
    }

    /*
    Async task for calling the REST API from application on background thread.
    The task makes PUT request for setting up RSVP status for event.
    */
    private class AsyncPUTRequest extends AsyncTask {
        // Delegate to represent caller
        private IAsyncRequestResponse delegate = null;

        // Exception to throw from PostExecute
        Exception backGrounndException = null;

        // Set the delegate i.e. activity that requested async task to post response
        public void setDelegate(IAsyncRequestResponse delegate) {
            this.delegate = delegate;
        }

        // HTTP PUT call
        @Override
        protected Object doInBackground(Object[] params) {

            // Get the URL to call
            String urlToCall = params[0].toString();

            // Return value
            Object retVal = null;

            // Call the PUT API
            try {
                retVal = makePutAPICall(urlToCall);
            } catch (IOException e) {
                e.printStackTrace();
                // Set the exception
                backGrounndException = e;
            }

            // Return
            return retVal;
        }

        @Override
        protected void onPostExecute(Object o) {

            if (backGrounndException != null) {
                delegate.backGroundErrorHandler(backGrounndException);
            }

            // No exception, hence handle the results
            delegate.responseHandler(o.toString());
        }
    }

    /*
    Async task for calling the REST API from application on background thread.
    The task accepts the user Id as integer and returns the list of user group models.
 */
    private class AsyncGroupsRESTAPICaller extends AsyncTask<Integer, String, List<UserGroupModel>> {

        // Exception thrown by background thread
        Exception backGroundException = null;

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
                backGroundException = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<UserGroupModel> userGroupModels) {

            // If there is an exception in background thread, call error handler
            if (backGroundException != null)
                delegate.backGroundErrorHandler(backGroundException);

            // Post the data
            delegate.postUserGroups(userGroupModels);
        }
    }

    /*
        Async task for calling the REST API from application on background thread.
        The task accepts the user Id as integer and returns the list of user event models.
     */
    private class AsyncEventsRESTAPICaller extends AsyncTask<Integer, String, List<UserEventModel>> {

        // Call back interface for communicating with caller
        IAsyncEventResponse delegate = null;

        // Exception during background process
        Exception backGroundException = null;

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
                backGroundException = e;
            }

            // Exception case...
            return null;
        }

        @Override
        protected void onPostExecute(List<UserEventModel> userEventModelList) {

            if (backGroundException != null)
                delegate.backGroundErrorHandler(backGroundException);

            // Return the events list to the caller
            delegate.postUserEvents(userEventModelList);
        }
    }
}
