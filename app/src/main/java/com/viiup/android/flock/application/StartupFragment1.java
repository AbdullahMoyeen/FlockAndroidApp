package com.viiup.android.flock.application;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jni.bitmap_operations.JniBitmapHolder;

/**
 * Created by AbdullahMoyeen on 4/11/16.
 */
public class StartupFragment1 extends Fragment {

    public StartupFragment1() {
    }

    public static StartupFragment1 newInstance() {
        return new StartupFragment1();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.startup_fragment_1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        Bitmap startupImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_startup_1);
        final JniBitmapHolder bitmapHolder = new JniBitmapHolder(startupImageBitmap);
        startupImageBitmap.recycle();

        ImageView startupImage = (ImageView) getView().findViewById(R.id.startup_image);
        startupImage.setImageBitmap(bitmapHolder.getBitmapAndFree());
    }
}
