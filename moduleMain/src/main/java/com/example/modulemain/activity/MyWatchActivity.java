package com.example.modulemain.activity;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.modulecommon.common.ARouteContants;
import com.example.modulemain.R;

import java.util.List;

@Route(path = ARouteContants.ModuleMain.MY_CAMERA_ACTIVITY)
public class MyWatchActivity extends AppCompatActivity {
    private Camera mCamera;
    private SurfaceView mPreview;
    private SurfaceHolder mHolder;
    //声明cameraId属性，ID为1调用前置摄像头，为0调用后置摄像头。
    private int cameraId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_watch);
        mPreview = findViewById(R.id.preview);
        mHolder = mPreview.getHolder();
        startRecord();
    }

    public void startRecord() {
        // 创建mediarecorder对象
        MediaRecorder mediarecorder = new MediaRecorder();
        mCamera = getCameraInstance();
        Camera.Parameters parameters = mCamera.getParameters();
        mCamera.autoFocus(null);
        // 解锁camera
        mCamera.setDisplayOrientation(90);
        mCamera.unlock();
        mediarecorder.setCamera(mCamera);
        List<android.hardware.Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for (int i = 0; i < supportedPreviewSizes.size(); i++) {
            Log.v("startRecord", "width=" + supportedPreviewSizes.get(i).width + ";height=" + supportedPreviewSizes.get(i).height);
        }
        // 设置录制视频源为Camera(相机)
        // mediarecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //start实现录像静音
        mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediarecorder.setVideoSize(1280, 720);
        //设置编码比特率,不设置会使视频图像模糊
        mediarecorder.setVideoEncodingBitRate(900 * 1024); //较为清晰，且文件大小为3.26M(30秒)
        mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264); //H263的貌似有点不清晰
        mediarecorder.setPreviewDisplay(mHolder.getSurface());
        // 设置视频文件输出的路径
        mediarecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/data/" + System.currentTimeMillis() + ".mp4");
        try {
            // 准备录制
            mediarecorder.prepare();
            // 开始录制
            mediarecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Camera getCameraInstance() {
        Camera c = null;
        try {
            CameraManager manager = (CameraManager) MyWatchActivity.this.getSystemService(Context.CAMERA_SERVICE);
            manager.openCamera();
            c = Camera.open(cameraId); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}
