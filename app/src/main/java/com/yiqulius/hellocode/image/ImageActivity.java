package com.yiqulius.hellocode.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.yiqulius.hellocode.R;
import com.yiqulius.hellocode.image.load.ImageLoader;
import com.yiqulius.hellocode.image.photowall.PhotoWallActivity;
import com.yiqulius.hellocode.image.util.MyBitmapLoadUtil;

import androidx.appcompat.app.AppCompatActivity;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private ImageView iv_main;
    private byte[] bytes;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_image);
        findViewById(R.id.btn_load_normal).setOnClickListener(this);
        findViewById(R.id.btn_load_efficient).setOnClickListener(this);
        findViewById(R.id.btn_photo_wall).setOnClickListener(this);
        iv_main = (ImageView) findViewById(R.id.iv_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_load_normal:
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_big_pic);
                iv_main.setImageBitmap(bitmap);
                break;
            case R.id.btn_load_efficient:
                // 检测自定义加载图片工具类是否好使
                Bitmap bitmap2 = MyBitmapLoadUtil.decodeFixedSizeForResource(getResources(), R.mipmap.ic_big_pic, 600, 300);
                iv_main.setImageBitmap(bitmap2);
                break;
            case R.id.btn_photo_wall:
                // 实现照片墙
                Intent intent = new Intent(getApplicationContext(), PhotoWallActivity.class);
                startActivity(intent);
                break;
        }
    }


}
