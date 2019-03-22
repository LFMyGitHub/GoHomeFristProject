package com.example.modulemain;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.modulebase.http.GlideImageLoader;
import com.example.modulecommon.base.BaseActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.AccordionTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity implements OnBannerListener {
    private Banner mViewBanner;
    //banner广告图片地址集合
    public static List<?> images = new ArrayList<>();
    //banner广告标题集合
    public static List<String> titles = new ArrayList<>();
    private RelativeLayout mGrideLayout;
    private ViewPager mGrideViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initViewBanner(this);
    }

    /**
     * 模拟数据
     */
    private void initData() {
        String[] urls = getResources().getStringArray(R.array.url);
        String[] tips = getResources().getStringArray(R.array.title);
        List list = Arrays.asList(urls);
        images = new ArrayList(list);
        List list1 = Arrays.asList(tips);
        titles = new ArrayList(list1);
    }

    /**
     * 初始化banner
     */
    private void initViewBanner(Context context) {
        //项目中可将banner添加到listView的header中
        //listView.addHeaderView(banner);
        mViewBanner = findViewById(R.id.view_banner);
        //设置banner长宽比为12:5
        mViewBanner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (context.getResources().getDisplayMetrics().widthPixels / 2.5f)));
        mViewBanner.setImages(images)//设置图片URL
                .setImageLoader(new GlideImageLoader())//设置图片加载器
                .setOnBannerListener(this)//设置点击监听
                .setBannerAnimation(AccordionTransformer.class)//设置滑动动画
                .setBannerTitles(titles)//设置底部banner标题
                .setIndicatorGravity(BannerConfig.LEFT)//设置指示器位置与标题同时设置不起作用
                .start();
        //设置banner底部指示器样式，必须设置底部标题，否则报错
        mViewBanner.updateBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //可直接在xml中设置指示器样式
    }

    /**
     * banner点击事件
     *
     * @param position
     */
    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getApplicationContext(), "你点击了：" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        mViewBanner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        mViewBanner.stopAutoPlay();
    }
}
