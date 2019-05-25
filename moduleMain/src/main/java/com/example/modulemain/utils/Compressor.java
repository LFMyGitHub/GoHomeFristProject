package com.example.modulemain.utils;

import android.app.Activity;
import android.util.Log;

import com.example.modulecommon.utils.libffmpeg.ExecuteBinaryResponseHandler;
import com.example.modulecommon.utils.libffmpeg.FFmpeg;
import com.example.modulecommon.utils.libffmpeg.LoadBinaryResponseHandler;
import com.example.modulecommon.utils.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.example.modulecommon.utils.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.example.modulemain.inter.CompressListener;
import com.example.modulemain.inter.InitListener;

/**
 *  @dec  
 *  @author apeng
 *  @date  2018/10/31 11:44
 */
public class Compressor {
    protected String TAG = "COMPRESSOR";//日志输出标志
    public Activity a;
    public FFmpeg ffmpeg;

    public Compressor(Activity activity) {
        a = activity;
        ffmpeg = FFmpeg.getInstance(a);
    }

    public void loadBinary(final InitListener mListener) {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onStart() {
                }

                @Override
                public void onFailure() {
                    mListener.onLoadFail("incompatible with this device");
                }

                @Override
                public void onSuccess() {
                    mListener.onLoadSuccess();
                }

                @Override
                public void onFinish() {

                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void execCommand(String cmd, final CompressListener mListener) {
        try {
            String[] cmds = cmd.split(" ");
            ffmpeg.execute(cmds, new ExecuteBinaryResponseHandler() {
                @Override
                public void onStart() {
                }

                @Override
                public void onProgress(String message) {
                    mListener.onExecProgress(message);
                }

                @Override
                public void onFailure(String message) {
                    mListener.onExecFail(message);
                }

                @Override
                public void onSuccess(String message) {
                    mListener.onExecSuccess(message);
                }

                @Override
                public void onFinish() {
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }

    }


    public void destory() {
        if (ffmpeg != null) {
            if (ffmpeg.isFFmpegCommandRunning()) {
                Log.i(TAG, "killRunningProcesses");
                ffmpeg.killRunningProcesses();
                Log.i(TAG, "killProcesses");
            }
        }
    }

}
