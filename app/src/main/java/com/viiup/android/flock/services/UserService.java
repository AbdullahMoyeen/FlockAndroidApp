package com.viiup.android.flock.services;

import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viiup.android.flock.models.EventModel;
import com.viiup.android.flock.models.UserEventModel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AbdullahMoyeen on 4/13/16.
 */
public class UserService {

    public List<UserEventModel> getUserEventsByUserId(int userId){

        String urlString = "http://localhost:8080/api/user/events?userId=1";
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
        }

        if(in != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            try {
                resultToDisplay = br.readLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        Gson gson = new Gson();

        List<UserEventModel> userEvents = gson.fromJson(resultToDisplay, new TypeToken<List<UserEventModel>>(){}.getType());

        return userEvents;
    }

//    // Remove this method once getUserEventsByUserId() is ready
//    private String getDummyUserEvents() {
//
//        List<UserEventModel> userEvents = new ArrayList<UserEventModel> ();
//
//        for (int i = 0; i < 10; i++) {
//            UserEventModel userEvent = new UserEventModel();
//            userEvent.event = new EventModel();
//            userEvent.event.setEventDescription("Enjoy shopping in Historic Downtown Grapevine as well as a variety of Artisan and Marketplace vendors throughout the festival. Take your kids to the museum exhibits, or to KidCave for exciting activities and shows, and don't forget about the Carnival & Midway! Enjoy non-stop live entertainment on multiple stages throughout the festival, as well as a variety of craft brew experiences and wine pavilions.");
//            if (i == 2 || i == 5 || i == 7)
//                userEvent.event.setEventCategory("Sports");
//            else if (i == 1 || i == 4 || i == 8)
//                userEvent.event.setEventCategory("Music");
//            else if (i == 0 || i == 6)
//                userEvent.event.setEventCategory("Movie");
//            else if (i == 3 || i == 9)
//                userEvent.event.setEventCategory("Other");
//            userEvent.event.setEventName(userEvent.event.getEventCategory() + " Event");
//            userEvents.add(userEvent);
//        }
//
//        Gson gson = new Gson();
//        String userEventsJson = gson.toJson(userEvents);
//
//        return userEventsJson;
//    }
}
