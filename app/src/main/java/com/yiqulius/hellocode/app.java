package com.yiqulius.hellocode;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * @author yiqulius
 */
public class app extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
    }
}
