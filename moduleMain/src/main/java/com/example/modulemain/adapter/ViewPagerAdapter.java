package com.example.modulemain.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> mListViews;

    public ViewPagerAdapter(List<View> list) {
        this.mListViews = list;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //实例化页卡
        View view = mListViews.get(position);
        container.addView(view, 0);
        return mListViews.get(position);
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //删除页卡
        container.removeView((View) object);
    }
}
