package com.yiqulius.hellocode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import java.util.Random;

public class WelcomeActivity extends Activity {

    private static final String TAG = "WelcomeActivity";

    private static final int[] images = {R.drawable.welcomimg1, R.drawable.welcomimg2, R.drawable.welcomimg3, R.drawable.welcomimg4, R.drawable.welcomimg5, R.drawable.welcomimg6, R.drawable.welcomimg7, R.drawable.welcomimg8, R.drawable.welcomimg9, R.drawable.welcomimg10, R.drawable.welcomimg11, R.drawable.welcomimg12};

    private static final int ANIM_TIME = 2000;
    private static final float SCALE_END = 1.15F;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        Log.d(TAG, "onCreate() returned: " + isFirstOpen);
        if (isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_welcome);
        imageView = findViewById(R.id.iv_entry);
        startMainActivity();
    }

    private void startMainActivity() {
        Random random = new Random(SystemClock.elapsedRealtime());
        //SystemClock.elapsedRealtime() 从开机到现在的毫秒数（手机睡眠(sleep)的时间也包括在内）

        imageView.setImageResource(images[random.nextInt(images.length)]);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                startAnim();
            }
        }, 1000);

    }

    private void startAnim() {

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, SCALE_END);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIM_TIME).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                WelcomeActivity.this.finish();
            }
        });
    }

    /**
     * 屏蔽物理返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
