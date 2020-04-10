package com.yiqulius.hellocode;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.stx.xhb.xbanner.XBanner;
import com.yiqulius.hellocode.image.ImageActivity;
import com.yiqulius.hellocode.image.load.ImageLoader;
import com.yiqulius.hellocode.image.photowall.PhotoWallActivity;
import com.yiqulius.hellocode.image.util.NetWorkUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import okhttp3.Call;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PictureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PictureFragment extends Fragment {

    private static final String TAG = "PictureFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SHOW_TEXT = "text";

    private String mContentText;
    private static boolean mCanLoadForPhoneNet;
    private ImageAdapter imageAdapter;
    private static boolean mIsGridViewIdle;

    GridView gv_main;

    public PictureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BlankFragment.
     */
    public static PictureFragment newInstance(String param1) {
        PictureFragment fragment = new PictureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SHOW_TEXT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContentText = getArguments().getString(ARG_SHOW_TEXT);
        }
    }

    //    /**
    //     * 初始化XBanner
    //     */
    //    private void initBanner(XBanner banner) {
    //        //加载广告图片
    //        banner.loadImage(new XBanner.XBannerAdapter() {
    //            @Override
    //            public void loadBanner(XBanner banner, Object model, View view, int position) {
    //                //此处适用Fresco加载图片，可自行替换自己的图片加载框架
    //                SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view;
    //                TuchongEntity.FeedListBean.EntryBean listBean = ((TuchongEntity.FeedListBean.EntryBean) model);
    //                String url = "https://photo.tuchong.com/" + listBean.getImages().get(0).getUser_id() + "/f/" + listBean.getImages().get(0).getImg_id() + ".jpg";
    //                simpleDraweeView.setImageURI(Uri.parse(url));
    //
    //                //                加载本地图片展示
    //                //                ((ImageView)view).setImageResource(((LocalImageInfo) model).getXBannerUrl());
    //            }
    //        });
    //    }
    List<TuchongEntity.FeedListBean.EntryBean> data = null;

    /**
     * 初始化数据
     */
    private void initData() {
        //加载网络图片资源
        String url = "https://api.tuchong.com/2/wall-paper/app";
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(), "加载广告数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                TuchongEntity advertiseEntity = new Gson().fromJson(response, TuchongEntity.class);
                List<TuchongEntity.FeedListBean> others = advertiseEntity.getFeedList();
                data = new ArrayList<>();
                for (int i = 0; i < others.size(); i++) {
                    TuchongEntity.FeedListBean feedListBean = others.get(i);
                    if ("post".equals(feedListBean.getType())) {
                        data.add(feedListBean.getEntry());
                    }
                }
                ArrayList<String> urls = new ArrayList<>();

                Log.d(TAG, "onResponse() returned: " + data.size());
                for (int i = 0; i < data.size(); i++) {
                    TuchongEntity.FeedListBean.EntryBean listBean = data.get(i);
                    String url = "https://photo.tuchong.com/" + listBean.getImages().get(0).getUser_id() + "/f/" + listBean.getImages().get(0).getImg_id() + ".jpg";
                    urls.add(url);
                }
                imageAdapter = new ImageAdapter(getContext(), urls);
                gv_main.setAdapter(imageAdapter);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_photo_wall, container, false);
        // 根据连接网络的情况判断是否加载图片
        if (!NetWorkUtil.isWifi(getContext())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("首次使用会从手机网络下载图片, 是否确认下载?").setTitle("友情提示").setPositiveButton("好的.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mCanLoadForPhoneNet = true;
                    imageAdapter.notifyDataSetChanged();
                }
            }).setNegativeButton("不行!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "瞅你扣那样!!!", Toast.LENGTH_LONG).show();
                }
            }).show();
        } else {
            mCanLoadForPhoneNet = true;
        }

        gv_main = (GridView) rootView.findViewById(R.id.gv_main);
        // 监听GridView的滑动状态
        gv_main.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    mIsGridViewIdle = true;
                    // 并触发更新adapter
                    //                        imageAdapter.notifyDataSetChanged();
                } else {
                    mIsGridViewIdle = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        initData();

        return rootView;
    }

    /**
     * 给GridView创建一个适配器
     */
    private static class ImageAdapter extends BaseAdapter {

        private final ArrayList<String> mUrls;
        private Context mContext;
        private final ImageLoader mImageLoader;

        public ImageAdapter(Context context, ArrayList<String> mUrls) {
            mContext = context;
            this.mUrls = mUrls;
            mImageLoader = ImageLoader.build(context);
            Log.e("lalala", "@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        }

        @Override
        public int getCount() {
            return mUrls.size();
        }

        @Override
        public String getItem(int position) {
            return mUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageAdapter.ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_photo_wall, null);
                holder = new ImageAdapter.ViewHolder();
                holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_square);
                convertView.setTag(holder);
            } else {
                holder = (ImageAdapter.ViewHolder) convertView.getTag();
            }

            // 设置默认图片
            ImageView mImageView = holder.mImageView;
            mImageView.setImageResource(android.R.drawable.screen_background_dark_transparent);

            // 检测是否wifi 和 是否是滑动状态
            if (mCanLoadForPhoneNet) {
                //            if (mCanLoadForPhoneNet && mIsGridViewIdle){
                // 加载图片
                mImageLoader.setImageView(mImageView).url(mUrls.get(position));
            }

            return convertView;
        }

        class ViewHolder {
            private ImageView mImageView;
        }
    }

}
