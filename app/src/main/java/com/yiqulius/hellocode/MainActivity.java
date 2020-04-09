package com.yiqulius.hellocode;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends Activity implements MainContract.View{
    private static final String TAG = "MainActivity";

    MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

        new MainPresenter(new MainModel(),this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick() returned: " + "test!");
                presenter.loadData(1);
            }
        });
    }

    @Override
    public void showView(String data) {
        Log.d(TAG, "showView() returned: " + "test!");

    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        Log.d(TAG, "setPresenter() returned: " + "test!");
        this.presenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() returned: ");
        SharedPreferencesUtil.putBoolean(MainActivity.this, SharedPreferencesUtil.FIRST_OPEN, true);
    }
}
