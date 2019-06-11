package com.example.modulebase.http;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.modulebase.utils.GlideCircleTransform;
import com.youth.banner.loader.ImageLoader;

/**
 * moduleMain banner
 * 重写图片加载器
 */
public class GlideImageLoader extends ImageLoader {
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context.getApplicationContext())
                .load(path)
                .into(imageView);
    }

    /**
     * 显示圆形图片
     *
     * @param context
     * @param path
     * @param errResID
     * @param imageView
     * @param type
     */
    public static void displayCircleImage(Context context, Object path, int errResID, ImageView imageView, int type) {
        switch (type) {
            case 1:
                //加载网络图片path不做处理
                break;
            case 2:
                //加载app资源文件
                path = resourceIdToUr(context, (int) path);
                break;
            case 3:
                //加载sd卡图片File
                break;
            default:
                break;
        }
        Glide.with(context.getApplicationContext())
                .load(path)
                .transform(new GlideCircleTransform(context))
                .crossFade()
                .error(errResID)
                .into(imageView);
    }

    public static Uri resourceIdToUr(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }
}
