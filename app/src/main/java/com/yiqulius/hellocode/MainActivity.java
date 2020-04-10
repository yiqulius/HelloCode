package com.yiqulius.hellocode;

import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements MainContract.View{
    private static final String TAG = "MainActivity";

    MainContract.Presenter presenter;

    private ViewPager mViewPager;
    private RadioGroup mTabRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MainPresenter(new MainModel(),this);

        initView();
    }

    private void initView() {
        mViewPager = findViewById(R.id.fragment_vp);
        mTabRadioGroup = findViewById(R.id.tabs_rg);

        List<Fragment> mFragments = new ArrayList<>(4);

        mFragments.add(BlankFragment.newInstance("今日"));
        mFragments.add(BlankFragment.newInstance("记录"));
        mFragments.add(BlankFragment.newInstance("通讯录"));
        mFragments.add(BlankFragment.newInstance("设置"));

        FragmentPagerAdapter mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mViewPager.setOffscreenPageLimit(3);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    private static class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mList;

        MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            return this.mList == null ? null : this.mList.get(position);
        }

        @Override
        public int getCount() {
            return this.mList == null ? 0 : this.mList.size();
        }
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(position);
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    };

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

    @Override
    public void onBackPressed() {
            doubleClickExit();
    }
    private static Boolean mIsExit = false;
    //再点一次退出程序
    private void doubleClickExit() {
        Timer exitTimer = null;
        if (!mIsExit) {
            mIsExit = true;
            Toast.makeText(getApplicationContext(), "再点一次退出应用", Toast.LENGTH_SHORT).show();
            exitTimer = new Timer();
            exitTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mIsExit = false;
                }
            }, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

}
