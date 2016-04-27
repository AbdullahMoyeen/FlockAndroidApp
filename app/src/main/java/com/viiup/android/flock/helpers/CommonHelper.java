package com.viiup.android.flock.helpers;

import android.content.Context;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.viiup.android.flock.application.R;

/**
 * Created by AbdullahMoyeen on 4/13/16.
 */
public class CommonHelper {

    public static boolean isEmailValid(Context context, String email) {

        boolean isValid = true;

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            isValid = false;
        if (email.replace(context.getString(R.string.fmt_email_domain), "").trim().equals(""))
            isValid = false;

        return isValid;
    }

    public static IconDrawable getIconDrawableByEventCategory(Context context, String eventCategory){

        FontAwesomeIcons faIcon;

        switch (eventCategory) {

            case "Biking":
                faIcon = FontAwesomeIcons.fa_bicycle;
                break;
            case "DIY":
                faIcon = FontAwesomeIcons.fa_cogs;
                break;
            case "Drones":
                faIcon = FontAwesomeIcons.fa_plane;
                break;
            case "Fashion":
                faIcon = FontAwesomeIcons.fa_star;
                break;
            case "Fitness":
                faIcon = FontAwesomeIcons.fa_heartbeat;
                break;
            case "Food & Drink":
                faIcon = FontAwesomeIcons.fa_cutlery;
                break;
            case "Games":
                faIcon = FontAwesomeIcons.fa_gamepad;
                break;
            case "Hacking":
                faIcon = FontAwesomeIcons.fa_file_code_o;
                break;
            case "Learning":
                faIcon = FontAwesomeIcons.fa_graduation_cap;
                break;
            case "Lego":
                faIcon = FontAwesomeIcons.fa_building_o;
                break;
            case "Movies":
                faIcon = FontAwesomeIcons.fa_film;
                break;
            case "Music":
                faIcon = FontAwesomeIcons.fa_music;
                break;
            case "Outdoor":
                faIcon = FontAwesomeIcons.fa_tree;
                break;
            case "Photography":
                faIcon = FontAwesomeIcons.fa_camera_retro;
                break;
            case "Reading":
                faIcon = FontAwesomeIcons.fa_book;
                break;
            case "Sports":
                faIcon = FontAwesomeIcons.fa_soccer_ball_o;
                break;
            default:
                faIcon = FontAwesomeIcons.fa_calendar;
                break;
        }

        return new IconDrawable(context, faIcon).colorRes(R.color.colorContentIcon);
    }

    public static IconDrawable getIconDrawableByGroupCategory(Context context, String groupCategory){

        IconDrawable iconDrawable;
        FontAwesomeIcons faIcon;

        switch (groupCategory) {

            case "Adventure":
                faIcon = FontAwesomeIcons.fa_map;
                break;
            case "Book Club":
                faIcon = FontAwesomeIcons.fa_book;
                break;
            case "Fitness":
                faIcon = FontAwesomeIcons.fa_bicycle;
                break;
            case "Games":
                faIcon = FontAwesomeIcons.fa_gamepad;
                break;
            case "Geeks":
                faIcon = FontAwesomeIcons.fa_flask;
                break;
            case "Literature":
                faIcon = FontAwesomeIcons.fa_bookmark;
                break;
            case "Music":
                faIcon = FontAwesomeIcons.fa_music;
                break;
            case "Social":
                faIcon = FontAwesomeIcons.fa_comment_o;
                break;
            case "Sports":
                faIcon = FontAwesomeIcons.fa_soccer_ball_o;
                break;
            default:
                faIcon = FontAwesomeIcons.fa_users;
                break;
        }

        return new IconDrawable(context, faIcon).colorRes(R.color.colorContentIcon);
    }
}
