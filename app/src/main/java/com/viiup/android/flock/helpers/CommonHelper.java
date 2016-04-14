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

        if (eventCategory.equals("Sports"))
            iconDrawable = new IconDrawable(context, FontAwesomeIcons.fa_soccer_ball_o).colorRes(R.color.colorListCellIcon);
        else if (eventCategory.equals("Music"))
            iconDrawable = new IconDrawable(context, FontAwesomeIcons.fa_music).colorRes(R.color.colorListCellIcon);
        else if (eventCategory.equals("Movie"))
            iconDrawable = new IconDrawable(context, FontAwesomeIcons.fa_film).colorRes(R.color.colorListCellIcon);
        else
            iconDrawable = new IconDrawable(context, FontAwesomeIcons.fa_calendar).colorRes(R.color.colorListCellIcon);

        return iconDrawable;
    }
}
