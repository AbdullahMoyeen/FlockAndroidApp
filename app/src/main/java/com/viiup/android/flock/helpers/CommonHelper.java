package com.viiup.android.flock.helpers;

import android.content.Context;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.viiup.android.flock.application.R;

/**
 * Created by AbdullahMoyeen on 4/13/16.
 */
public class CommonHelper {
    
    public static IconDrawable getIconDrawableByEventCategory(Context context, String eventCategory){

        IconDrawable iconDrawable;

        switch (eventCategory) {

            case "Sports":
                iconDrawable = new IconDrawable(context, FontAwesomeIcons.fa_soccer_ball_o).colorRes(R.color.colorContentIcon);
                break;
            case "Music":
                iconDrawable = new IconDrawable(context, FontAwesomeIcons.fa_music).colorRes(R.color.colorContentIcon);
                break;
            case "Movie":
                iconDrawable = new IconDrawable(context, FontAwesomeIcons.fa_film).colorRes(R.color.colorContentIcon);
                break;
            default:
                iconDrawable = new IconDrawable(context, FontAwesomeIcons.fa_calendar).colorRes(R.color.colorContentIcon);
                break;
        }

        return iconDrawable;
    }
}
