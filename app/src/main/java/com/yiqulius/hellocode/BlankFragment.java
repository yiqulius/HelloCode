package com.yiqulius.hellocode;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.stx.xhb.xbanner.XBanner;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import okhttp3.Call;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SHOW_TEXT = "text";

    private String mContentText;


    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BlankFragment.
     */
    public static BlankFragment newInstance(String param1) {
        BlankFragment fragment = new BlankFragment();
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

    /**
     * 初始化XBanner
     */
    private void initBanner(XBanner banner) {
        //设置广告图片点击事件
        banner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, Object model, View view, int position) {
                Toast.makeText(getActivity(), "点击了第" + (position + 1) + "图片", Toast.LENGTH_SHORT).show();
            }
        });
        //加载广告图片
        banner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                //此处适用Fresco加载图片，可自行替换自己的图片加载框架
                SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view;
                TuchongEntity.FeedListBean.EntryBean listBean = ((TuchongEntity.FeedListBean.EntryBean) model);
                String url = "https://photo.tuchong.com/" + listBean.getImages().get(0).getUser_id() + "/f/" + listBean.getImages().get(0).getImg_id() + ".jpg";
                simpleDraweeView.setImageURI(Uri.parse(url));

                //                加载本地图片展示
                //                ((ImageView)view).setImageResource(((LocalImageInfo) model).getXBannerUrl());
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //加载网络图片资源
        String url = "https://api.tuchong.com/2/wall-paper/app";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "加载广告数据失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        TuchongEntity advertiseEntity = new Gson().fromJson(response, TuchongEntity.class);
                        List<TuchongEntity.FeedListBean> others = advertiseEntity.getFeedList();
                        List<TuchongEntity.FeedListBean.EntryBean> data = new ArrayList<>();
                        for (int i = 0; i < others.size(); i++) {
                            TuchongEntity.FeedListBean feedListBean = others.get(i);
                            if ("post".equals(feedListBean.getType())) {
                                data.add(feedListBean.getEntry());
                            }
                        }

                        //刷新数据之后，需要重新设置是否支持自动轮播
                        mBanner.setAutoPlayAble(data.size() > 1);
                        mBanner.setIsClipChildrenMode(true);
                        //老方法，不推荐使用
                        mBanner.setBannerData(R.layout.layout_fresco_imageview, data);

                    }
                });
    }
    XBanner mBanner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        TextView contentTv = rootView.findViewById(R.id.content_tv);
        contentTv.setText(mContentText);

        mBanner = rootView.findViewById(R.id.banner);
        initBanner(mBanner);
        initData();
        return rootView;
    }

}
