package com.viiup.android.flock.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viiup.android.flock.models.EventModel;
import com.viiup.android.flock.models.UserEventModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Random;

/**
 * Created by AbdullahMoyeen on 4/13/16.
 */
public class UserService {

    public List<UserEventModel> getUserEventsByUserId(int userId){

        String userEventsJson = getDummyUserEvents();
        Gson gson = new Gson();

        return gson.fromJson(userEventsJson, new TypeToken<List<UserEventModel>>(){}.getType());
    }

    // Remove this method once getUserEventsByUserId() is ready
    private String getDummyUserEvents() {

        List<UserEventModel> userEvents = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            UserEventModel userEvent = new UserEventModel();
            userEvent.event = new EventModel();

            if (i == 2 || i == 5 || i == 7)
                userEvent.event.setEventCategory("Sports");
            else if (i == 1 || i == 4 || i == 8)
                userEvent.event.setEventCategory("Music");
            else if (i == 0 || i == 6)
                userEvent.event.setEventCategory("Movie");
            else if (i == 3 || i == 9)
                userEvent.event.setEventCategory("Other");

            userEvent.event.setEventName(userEvent.event.getEventCategory() + " Event");
            userEvent.event.setGroupName(userEvent.event.getEventCategory() + " Group");
            userEvent.event.setEventStartDatetime(new Date());
            userEvent.event.setEventDescription("Enjoy shopping in Historic Downtown Grapevine as well as a variety of Artisan and Marketplace vendors throughout the festival. Take your kids to the museum exhibits, or to KidCave for exciting activities and shows, and don't forget about the Carnival & Midway! Enjoy non-stop live entertainment on multiple stages throughout the festival, as well as a variety of craft brew experiences and wine pavilions.");
            userEvent.event.setAttendeeCount(random.nextInt(200));
            userEvent.event.setEventAddressLine1("800 W Campbell Rd");
            userEvent.event.setEventCity("Richardson");
            userEvent.event.setEventStateCode("TX");
            userEvent.event.setEventPostalCode("75080");

            userEvents.add(userEvent);
        }

        Gson gson = new Gson();

        return gson.toJson(userEvents);
    }
}
