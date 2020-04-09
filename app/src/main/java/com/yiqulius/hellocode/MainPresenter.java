package com.yiqulius.hellocode;

import android.util.Log;

/**
 * @author yiqulius
 */
public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = "MainPresenter";

    MainModel model;

    MainContract.View view;

    MainPresenter(MainModel model, MainContract.View view){
        this.model = model;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void loadData(int position) {
        Log.d(TAG, "loadData() returned: " + "test!");
        view.showView(model.getData());
    }
}
