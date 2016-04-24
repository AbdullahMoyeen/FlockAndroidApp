package com.viiup.android.flock.models;

/**
 * Created by AbdullahMoyeen on 4/24/16.
 */
public class UserPasswordChangeModel {

    private int userId;
    private String emailAddress;
    private String newPassword;
    private String reEnteredPassword;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getReEnteredPassword() {
        return reEnteredPassword;
    }

    public void setReEnteredPassword(String reEnteredPassword) {
        this.reEnteredPassword = reEnteredPassword;
    }
}
