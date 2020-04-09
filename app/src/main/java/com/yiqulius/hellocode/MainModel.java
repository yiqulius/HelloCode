package com.yiqulius.hellocode;

import android.util.Log;

/**
 * @author yiqulius
 */
public class MainModel {

    private static final String TAG = "MainModel";

    String getData(){
        Log.d(TAG, "getData() returned: " + "test!");
        return "1";
    }
}
