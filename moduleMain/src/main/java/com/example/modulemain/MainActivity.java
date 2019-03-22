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
import com.youth.banner.listener.OnBannerListener;

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
        mViewBanner.setImages(images)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .start();
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
}
