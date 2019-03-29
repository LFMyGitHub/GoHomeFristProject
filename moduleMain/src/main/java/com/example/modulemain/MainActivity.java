package com.example.modulemain;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.modulebase.http.GlideImageLoader;
import com.example.modulecommon.base.BaseActivity;
import com.example.modulecommon.bean.modulemain.AppBean;
import com.example.modulecommon.common.ARouteContants;
import com.example.modulecommon.common.Contants;
import com.example.modulecommon.utils.ListUtil;
import com.example.modulecommon.widget.MyViewPager;
import com.example.modulemain.adapter.ViewPagerAdapter;
import com.example.modulemain.adapter.ViewPagerItemAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.AccordionTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(path = ARouteContants.ModuleMain.MAIN_ACTIVITY)
public class MainActivity extends BaseActivity implements OnBannerListener {
    //当前viewpager页码
    private static final int DEFAULT_PAGE = 0;

    private Banner mViewBanner;
    //banner广告图片地址集合
    public static List<?> images = new ArrayList<>();
    //banner广告标题集合
    public static List<String> titles = new ArrayList<>();
    //grideview应用集合
    private List<AppBean> defaultAppModels = new ArrayList<>();
    //app数据分页后集合
    private List<List<AppBean>> appItems = new ArrayList<>();
    //gridView分页集合
    private List<View> appPages = new ArrayList<>();
    private MyViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    //指示器
    private LinearLayout mLinearLayout;
    //指示器集合
    private List<ImageView> mImageViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initViewBanner(this);
        initViewPage();
    }

    private void initView() {
        mViewBanner = findViewById(R.id.view_banner);
        mViewPager = findViewById(R.id.module_main_view_pager);
        mLinearLayout = findViewById(R.id.module_main_indicator_layout);
    }

    /**
     * 模拟数据
     */
    private void initData() {
        //初始化banners数据
        String[] urls = getResources().getStringArray(R.array.url);
        String[] tips = getResources().getStringArray(R.array.title);
        List list = Arrays.asList(urls);
        images = new ArrayList(list);
        List list1 = Arrays.asList(tips);
        titles = new ArrayList(list1);

        //初始化viewpager数据
        String[] applicationPackagesName = this.getResources().getStringArray(R.array.default_application_package);
        String[] applicationTitle = this.getResources().getStringArray(R.array.default_application_title);
        String[] applicationActions = this.getResources().getStringArray(R.array.default_application_action);
        for (int i = 0; i < applicationPackagesName.length; i++) {
            AppBean appBean = new AppBean();
            appBean.setPackageName(applicationPackagesName[i]);
            appBean.setTitle(applicationTitle[i]);
            appBean.setAction(applicationActions[i]);
            defaultAppModels.add(appBean);
        }
        appItems = ListUtil.getSubList(defaultAppModels, Contants.ModuleMain.GRIDE_VIEW_NUMBER);
        appPages = new ArrayList<>();
    }

    /**
     * 初始化banner
     */
    private void initViewBanner(Context context) {
        //项目中可将banner添加到listView的header中
        //listView.addHeaderView(banner);
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
     * 初始化ViewPage
     */
    private void initViewPage() {
        for (List<AppBean> appModelList : appItems) {
            //创建一个GridView
            GridView gridView = new GridView(this);
            //设置GridView显示的列数
            gridView.setNumColumns(Contants.ModuleMain.GRIDE_VIEW_COLUMN);
            //清除选择效果
            gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            ViewPagerItemAdapter viewPagerItemAdapter = new ViewPagerItemAdapter(this);
            viewPagerItemAdapter.setData(appModelList);
            gridView.setAdapter(viewPagerItemAdapter);
            //将gridview添加到views
            appPages.add(gridView);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //刷新viewPage指示器
                refreshIndicatorFocused(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mViewPagerAdapter = new ViewPagerAdapter(appPages);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(DEFAULT_PAGE);
        createIndicator(appPages.size());
    }

    /**
     * 创建viewpager指示器
     *
     * @param num
     */
    private void createIndicator(int num) {
        //只有一页不显示指示器
        if (num == 1) return;
        for (int i = 0; i < num; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            params.gravity = Gravity.CENTER;
            imageView.setLayoutParams(params);
            mImageViews.add(imageView);
            mLinearLayout.addView(imageView);
        }
        refreshIndicatorFocused(DEFAULT_PAGE);
    }

    /**
     * 刷新当前页码标示
     */
    private void refreshIndicatorFocused(int curIndex) {
        for (int i = 0; i < mImageViews.size(); i++) {
            if (curIndex == i) {//当前页
                mImageViews.get(i).setImageResource(R.drawable.circle_blue);
            } else {//非当前页
                mImageViews.get(i).setImageResource(R.drawable.circle_gray);
            }
        }
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
