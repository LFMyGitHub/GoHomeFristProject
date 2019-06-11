package com.example.modulebase.http;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.modulebase.R;
import com.example.modulebase.utils.GlideCircleTransform;
import com.youth.banner.loader.ImageLoader;

/**
 * moduleMain banner
 * 重写图片加载器
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context.getApplicationContext())
                .load(path)
                .into(imageView);
    }

    /**
     * 显示圆形图片
     * @param context
     * @param path
     * @param imageView
     */
    public static void displayCircleImage(Context context, Object path, ImageView imageView) {
        Glide.with(context.getApplicationContext())
                .load(path)
                .transform(new GlideCircleTransform(context))
                .crossFade()
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

    //displayRoundCornersImageNoCenterCrop
}
