package com.example.modulemain.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.modulecommon.bean.modulemain.BannerBean;
import com.example.modulecommon.common.Contants;

import java.util.List;

public class BannerAdapter extends PagerAdapter {
    //页卡ImageView
    private List<ImageView> mListViews;
    //后台获取得到banner对象
    private List<BannerBean> mBannerBeans;
    private Context context;

    public BannerAdapter(Context context, List<ImageView> mListViews, List<BannerBean> bannerBeans) {
        this.context = context;
        this.mListViews = mListViews;
        this.mBannerBeans = bannerBeans;
    }

    /**
     * 删除页卡
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mListViews.get(position));
    }

    /**
     * 实例化页卡
     *
     * @param container
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final ImageView imageView = mListViews.get(position);
        final BannerBean bannerBean = mBannerBeans.get(position);
        //判断图片URL地址是否为空，是加载本地图片，否网络下载
        if (!TextUtils.isEmpty(bannerBean.getImageUrl())) {

        } else {
            imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),
                    Contants.AppIcon.banners[position]));
        }

        return super.instantiateItem(container, position);
    }

    /**
     * @return 返回页卡数量
     */
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
