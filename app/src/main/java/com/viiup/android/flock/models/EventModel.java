package com.viiup.android.flock.models;

import java.util.Date;

/**
 * Created by AbdullahMoyeen on 4/13/16.
 */
public class EventModel {

    private int eventId;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;
    private int groupId;
    private String groupName;
    private String eventName;
    private String eventDescription;
    private String eventCategory;
    private Date eventStartDatetime;
    private Date eventEndDatetime;
    private String eventAddressLine1;
    private String eventAddressLine2;
    private String eventCity;
    private String eventStateCode;
    private String eventPostalCode;
    private String eventKeywords;
    private float eventLatitude;
    private float eventLongitude;
    private boolean privateEvent;
    private int attendeeCount;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public Date getEventStartDatetime() {
        return eventStartDatetime;
    }

    public void setEventStartDatetime(Date eventStartDatetime) {
        this.eventStartDatetime = eventStartDatetime;
    }

    public Date getEventEndDatetime() {
        return eventEndDatetime;
    }

    public void setEventEndDatetime(Date eventEndDatetime) {
        this.eventEndDatetime = eventEndDatetime;
    }

    public String getEventAddressLine1() {
        return eventAddressLine1;
    }

    public void setEventAddressLine1(String eventAddressLine1) {
        this.eventAddressLine1 = eventAddressLine1;
    }

    public String getEventAddressLine2() {
        return eventAddressLine2;
    }

    public void setEventAddressLine2(String eventAddressLine2) {
        this.eventAddressLine2 = eventAddressLine2;
    }

    public String getEventCity() {
        return eventCity;
    }

    public void setEventCity(String eventCity) {
        this.eventCity = eventCity;
    }

    public String getEventStateCode() {
        return eventStateCode;
    }

    public void setEventStateCode(String eventStateCode) {
        this.eventStateCode = eventStateCode;
    }

    public String getEventPostalCode() {
        return eventPostalCode;
    }

    public void setEventPostalCode(String eventPostalCode) {
        this.eventPostalCode = eventPostalCode;
    }

    public String getEventKeywords() {
        return eventKeywords;
    }

    public void setEventKeywords(String eventKeywords) {
        this.eventKeywords = eventKeywords;
    }

    public float getEventLatitude() {
        return eventLatitude;
    }

    public void setEventLatitude(float eventLatitude) {
        this.eventLatitude = eventLatitude;
    }

    public float getEventLongitude() {
        return eventLongitude;
    }

    public void setEventLongitude(float eventLongitude) {
        this.eventLongitude = eventLongitude;
    }

    public boolean isPrivateEvent() {
        return privateEvent;
    }

    public void setPrivateEvent(boolean privateEvent) {
        this.privateEvent = privateEvent;
    }

    public int getAttendeeCount() {
        return attendeeCount;
    }

    public void setAttendeeCount(int attendeeCount) {
        this.attendeeCount = attendeeCount;
    }
}
