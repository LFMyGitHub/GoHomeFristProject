package com.example.modulebase.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.util.Util;

import java.security.MessageDigest;

/**
 * Glide显示为圆形图片
 */
public class GlideCircleTransform extends BitmapTransformation {
    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    public String getId() {
        return getClass().getName();
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }
        //得到最小边
        int size = Math.min(source.getWidth(), source.getHeight());
        //计算图片起点
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        //创建新的Bitmap
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        //得到Glide中BitmapPool的bitmap位图对象
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        //设置TileMode的样式 CLAMP 拉伸 REPEAT 重复  MIRROR 镜像
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        //画圆
        canvas.drawCircle(r, r, r, paint);
        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(getId().getBytes(CHARSET));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BitmapTransformation) {
            return this == obj;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(getId().hashCode());
    }
}
