package com.example.modulecommon.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    /**
     * 没有文件夹。创建文件夹
     *
     * @param filePath
     */
    public static boolean makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                return file.mkdirs();
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取文件大小
     *
     * @param path
     * @return
     */
    public static String getFileSize(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return "0 MB";
        } else {
            long size = f.length();
            return (size / 1024f) / 1024f + "MB";
        }
    }

    /**
     * 文字转图片
     *
     * @param timeText     第一行文字
     * @param locationText 第二行文字
     * @param textSize
     * @return
     */
    public static Bitmap textAsBitmap(String timeText, String locationText, float textSize) {
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.YELLOW);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        Bitmap bitmap = Bitmap.createBitmap(((int) getFontWidth(locationText, textSize)), getFontHeight(textSize) * 2, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawText(timeText, 0, getFontHeight(textSize - 2), paint);
        canvas.drawText(locationText, 0, getFontHeight(textSize - 2) * 2, paint);
        canvas.save();
        canvas.restore();
        return bitmap;
    }

    /**
     * 获取文字高度
     *
     * @param fontSize
     * @return
     */
    public static int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }

    /**
     * 获取文本宽度
     *
     * @param text     文本
     * @param fontSize 字体大小
     * @return
     */
    public static float getFontWidth(String text, float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        return paint.measureText(text, 0, text.length());
    }


    /**
     * 文字生成图片是否成功
     *
     * @param filePath filePath
     * @param context  context
     * @return 生成图片是否成功
     */
    public static boolean textToPicture(String filePath, String text1, String text2, Context context) {
        Bitmap bitmap = textAsBitmap(text1, text2, 18);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(filePath);
            //设置PNG生成图片为透明背景
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
