package com.example.modulecommon.utils;

import android.util.Log;

public class StringUtil {
    /**
     * FFmpeg压缩并添加文字水印指令
     *
     * @param srcUrl
     * @param markUrl
     * @param targetUrl
     * @param type
     * @return FFmpeg指令
     * -y：输出时覆盖输出目录已存在的同名文件
     * -i：input路径，传入视频文件的路径
     * -filter_complex：水印
     * overlay：水印位置
     * -vcodec：编码格式，一般都是指定libx264
     * -preset：转码速度，ultrafast，superfast，veryfast，faster，fast，medium，slow，slower，veryslow和placebo。
     * -crf： 编码质量，取值范围是0-51
     * -acodec：音频编码，一般采用libmp3lame
     * -ar 设定采样率
     * -ac 设定声音的Channel数
     * -b 设定视频流量，默认为200Kbit/s
     * -s 设定画面的宽与高
     * -aspect 设定画面的比例
     */
    public static String cmdVideo(String srcUrl, String markUrl, String targetUrl, int type) {
        String waterMarkCmd = "";
        switch (type) {
            case 1:
                waterMarkCmd = "-y -i %s -i %s -filter_complex overlay=0:0 -strict -2 -vcodec libx264 -preset superfast " +
                        "-crf 23 -acodec libmp3lame -ar 44100 -ac 2 -b:a 96k -s 480x800 -aspect 9:16 %s";
                break;
            case 2:
                waterMarkCmd = "-y -i %s -i %s -filter_complex overlay=0:0 -strict -2 -vcodec libx264 -preset superfast " +
                        "-crf 23 -acodec libmp3lame -ar 44100 -ac 2 -b:a 96k -s 800x480 -aspect 16:9 %s";
                break;
            default:
                break;
        }
        waterMarkCmd = String.format(waterMarkCmd, srcUrl, markUrl, targetUrl);
        Log.e("TAG", waterMarkCmd);
        return waterMarkCmd;
    }
}
