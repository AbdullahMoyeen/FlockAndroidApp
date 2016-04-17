package com.viiup.android.flock.services;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viiup.android.flock.models.EventModel;
import com.viiup.android.flock.models.GroupModel;
import com.viiup.android.flock.models.UserEventModel;
import com.viiup.android.flock.models.UserGroupModel;

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

    public void setUserEventRsvp(int userId, int eventId, boolean isAttending){

    }

    public List<UserGroupModel> getUserGroupsByUserId(int userId){

        String userGroupsJson = getDummyUserGroups();
        Gson gson = new Gson();

        return gson.fromJson(userGroupsJson, new TypeToken<List<UserGroupModel>>(){}.getType());
    }

    public void setUserGroupMemberShip(int userId, int groupId, boolean isMember){

    }

    // Remove this method once getUserEventsByUserId() is ready
    private String getDummyUserEvents() {

        List<UserEventModel> userEvents = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            UserEventModel userEvent = new UserEventModel();
            userEvent.event = new EventModel();

            if ((i%10) == 2 || (i%10) == 5 || (i%10) == 7)
                userEvent.event.setEventCategory("Sports");
            else if ((i%10) == 1 || (i%10) == 4 || (i%10) == 8)
                userEvent.event.setEventCategory("Music");
            else if ((i%10) == 0 || (i%10) == 6)
                userEvent.event.setEventCategory("Movie");
            else if ((i%10) == 3 || (i%10) == 9)
                userEvent.event.setEventCategory("Other");

            userEvent.event.setEventId(i+1);
            userEvent.event.setEventName(userEvent.event.getEventCategory() + " Event");
            userEvent.event.setGroupName(userEvent.event.getEventCategory() + " Group");
            userEvent.event.setEventStartDatetime(new Date());
            userEvent.event.setEventDescription("Enjoy shopping in Historic Downtown Grapevine as well as a variety of Artisan and Marketplace vendors throughout the festival. Take your kids to the museum exhibits, or to KidCave for exciting activities and shows, and don't forget about the Carnival & Midway! Enjoy non-stop live entertainment on multiple stages throughout the festival, as well as a variety of craft brew experiences and wine pavilions.");
            userEvent.setIsAttending(random.nextBoolean());
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

    // Remove this method once getUserGroupssByUserId() is ready
    private String getDummyUserGroups() {

        List<UserGroupModel> userGroups = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            UserGroupModel userGroup = new UserGroupModel();
            userGroup.group = new GroupModel();

            if ((i%10) == 2 || (i%10) == 5 || (i%10) == 7)
                userGroup.group.setGroupCategory("Sports");
            else if ((i%10) == 1 || (i%10) == 4 || (i%10) == 8)
                userGroup.group.setGroupCategory("Music");
            else if ((i%10) == 0 || (i%10) == 6)
                userGroup.group.setGroupCategory("Movie");
            else if ((i%10) == 3 || (i%10) == 9)
                userGroup.group.setGroupCategory("Other");

            userGroup.group.setGroupId(i + 1);
            userGroup.group.setGroupName(userGroup.group.getGroupCategory() + " Group");
            userGroup.group.setGroupDescription("Enjoy shopping in Historic Downtown Grapevine as well as a variety of Artisan and Marketplace vendors throughout the festival. Take your kids to the museum exhibits, or to KidCave for exciting activities and shows, and don't forget about the Carnival & Midway! Enjoy non-stop live entertainment on multiple stages throughout the festival, as well as a variety of craft brew experiences and wine pavilions.");
            userGroup.group.setActiveMemberCount(random.nextInt(200));
            userGroup.group.setUpcomingEventCount(random.nextInt(10));
            userGroup.setMember(random.nextBoolean());


            userGroups.add(userGroup);
        }

        Gson gson = new Gson();

        return gson.toJson(userGroups);
    }
}
