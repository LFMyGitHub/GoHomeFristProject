package com.example.modulemain.activity;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.modulecommon.common.ARouteContants;
import com.example.modulecommon.utils.FileUtils;
import com.example.modulecommon.utils.GetPathFromUri;
import com.example.modulecommon.utils.StringUtil;
import com.example.modulecommon.widget.dialog.widget.CircleButtonView;
import com.example.modulecommon.widget.dialog.widget.CustomProgressDialog;
import com.example.modulemain.R;
import com.example.modulemain.inter.CompressListener;
import com.example.modulemain.inter.InitListener;
import com.example.modulemain.utils.Compressor;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.modulecommon.common.Contants.Common.IMG_PATH;
import static com.example.modulecommon.common.Contants.Common.VIDEO_PATH;
import static com.example.modulecommon.utils.GetPathFromUri.getImgFileName;

@Route(path = ARouteContants.ModuleMain.MY_CAMERA_ACTIVITY)
public class MyWatchActivity extends AppCompatActivity implements SurfaceHolder.Callback, CircleButtonView.OnLongClickListener {
    private static final String TAG = "MainActivity";
    //预览SurfaceView
    private SurfaceView mSurfaceView;
    private Camera mCamera;
    //底部"按住拍"按钮
    private CircleButtonView mStartButton;
    //录制视频
    private MediaRecorder mMediaRecorder;
    private SurfaceHolder mSurfaceHolder;
    //判断是否正在录制
    private boolean isRecording;
    //段视频保存的目录
    private File mTargetFile;
    //手势处理, 主要用于变焦 (双击放大缩小)
    private GestureDetector mDetector;
    //是否放大
    private boolean isZoomIn = false;
    //压缩后的视频地址
    private String currentOutputVideoPath = "";
    private Compressor mCompressor;
    private CustomProgressDialog mProcessingDialog;
    //获取视频时长
    private String videoTime = "";
    //获取视频的宽度
    private int videoWidth = 0;
    //获取视频的高度
    private int videoHeight = 0;
    //获取视频的角度
    private int videoGotation = 0;
    //视频时长 s
    private Double videoLength = 0.00;
    private String mVideoPath = "";//原视频地址
    private String mStrFFmpeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_my_watch);
        getPermissions();
        initFile();
        initVideo();
        initView();

    }

    private void initFile() {
        if (!FileUtils.makeRootDirectory(VIDEO_PATH) || !FileUtils.makeRootDirectory(IMG_PATH)) {
            Toast.makeText(this, "文件夹创建失败", Toast.LENGTH_SHORT).show();
        }
        currentOutputVideoPath = VIDEO_PATH + GetPathFromUri.getVideoFileName();
    }

    private void initVideo() {
        mProcessingDialog = new CustomProgressDialog(this);
        mProcessingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mCompressor != null) {
                    mCompressor.destory();
                }
            }
        });
        mCompressor = new Compressor(this);
        mCompressor.loadBinary(new InitListener() {
            @Override
            public void onLoadSuccess() {
            }

            @Override
            public void onLoadFail(String reason) {
            }
        });
    }

    private void initView() {
        mSurfaceView = findViewById(R.id.main_surface_view);
        mDetector = new GestureDetector(this, new ZoomGestureListener());
        /**
         * 单独处理mSurfaceView的双击事件
         */
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });

        mSurfaceHolder = mSurfaceView.getHolder();
        //设置屏幕分辨率
        //mSurfaceHolder.setFixedSize(videoWidth, videoHeight);
        mSurfaceHolder.addCallback(this);
        mStartButton = findViewById(R.id.main_press_control);
        mStartButton.setOnLongClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        startPreView(holder);
    }

    /**
     * 开启预览
     *
     * @param holder
     */
    private void startPreView(SurfaceHolder holder) {
        if (mCamera == null) {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        if (mCamera != null) {
            mCamera.setDisplayOrientation(90);
            try {
                mCamera.setPreviewDisplay(holder);
                Camera.Parameters parameters = mCamera.getParameters();
                //实现Camera自动对焦
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes != null) {
                    for (String mode : focusModes) {
                        mode.contains("continuous-video");
                        parameters.setFocusMode("continuous-video");
                    }
                }
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", e.getMessage());
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            Log.d(TAG, "surfaceDestroyed: ");
            //停止预览并释放摄像头资源
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    /**
     * 开始录制
     */
    private void startRecord() {
        isRecording = true;
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }

        if (mCamera == null) {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }

        Camera.Parameters parameters = mCamera.getParameters();
        mCamera.autoFocus(null);
        mMediaRecorder.setCamera(mCamera);
        // 解锁camera
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for (int i = 0; i < supportedPreviewSizes.size(); i++) {
            Log.v("startRecord", "width=" + supportedPreviewSizes.get(i).width + ";height=" + supportedPreviewSizes.get(i).height);
        }
        //从相机采集视频
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //从麦克采集音频信息
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置视频格式
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if (supportedPreviewSizes.size() > 0) {
            mMediaRecorder.setVideoSize(supportedPreviewSizes.get(0).width, supportedPreviewSizes.get(0).height);
        }
        //编码格式
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
        // 设置帧频率，然后就清晰了
        mMediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        File targetDir = Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        mTargetFile = new File(targetDir,
                SystemClock.currentThreadTimeMillis() + ".mp4");
        // 设置视频文件输出的路径
        mMediaRecorder.setOutputFile(mTargetFile.getAbsolutePath());
        mMediaRecorder.setOrientationHint(90);
        try {
            // 准备录制
            mMediaRecorder.prepare();
            // 开始录制
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录制 并且保存
     */
    private void stopRecordSave() {
        if (isRecording) {
            if (mMediaRecorder != null) {
                try {
                    mMediaRecorder.stop();
                } catch (IllegalStateException e) {
                    // TODO 如果当前java状态和jni里面的状态不一致
                    mMediaRecorder = null;
                    mMediaRecorder = new MediaRecorder();
                }
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
            isRecording = false;
            //FFmpeg压缩视频
            getVideoTime(mTargetFile.getAbsolutePath());
            startCompress(mTargetFile.getAbsolutePath());
        }
    }

    /**
     * 停止录制, 不保存
     */
    private void stopRecordUnSave() {
        if (isRecording) {
            if (mMediaRecorder != null) {
                try {
                    mMediaRecorder.stop();
                } catch (IllegalStateException e) {
                    // TODO 如果当前java状态和jni里面的状态不一致
                    mMediaRecorder = null;
                    mMediaRecorder = new MediaRecorder();
                }
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
            isRecording = false;
            if (mTargetFile.exists()) {
                //不保存直接删掉
                mTargetFile.delete();
            }
        }
    }

    /**
     * 相机变焦
     *
     * @param zoomValue
     */
    public void setZoom(int zoomValue) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.isZoomSupported()) {//判断是否支持
                int maxZoom = parameters.getMaxZoom();
                if (maxZoom == 0) {
                    return;
                }
                if (zoomValue > maxZoom) {
                    zoomValue = maxZoom;
                }
                parameters.setZoom(zoomValue);
                mCamera.setParameters(parameters);
            }
        }

    }

    @Override
    public void onLongClick() {
        //开始录制
        Toast.makeText(this, "开始录制", Toast.LENGTH_SHORT).show();
        startRecord();
    }

    @Override
    public void onNoMinRecord(int currentTime) {
        Toast.makeText(this, "最短录制时长三秒", Toast.LENGTH_SHORT).show();
        stopRecordUnSave();
    }

    @Override
    public void onRecordFinishedListener() {
        stopRecordSave();
    }

    class ZoomGestureListener extends GestureDetector.SimpleOnGestureListener {
        //双击手势事件
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            super.onDoubleTap(e);
            Log.d(TAG, "onDoubleTap: 双击事件");
            if (mMediaRecorder == null) {
                mMediaRecorder = new MediaRecorder();
            }
            if (!isZoomIn) {
                setZoom(20);
                isZoomIn = true;
            } else {
                setZoom(0);
                isZoomIn = false;
            }
            return true;
        }
    }

    /**
     * @dec 一句代码搞定权限问题
     * @author apeng
     * @date 2018/10/31 10:54
     */
    public void getPermissions() {
        XXPermissions.with(this)
                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .permission("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA", "android.permission.RECORD_AUDIO")
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {

                    }
                });
    }

    /**
     * 视频压缩开始
     */
    private void startCompress(String videoPath) {
        try {
            if (TextUtils.isEmpty(videoPath)) {
                Toast.makeText(this, "请重新选择视频", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                File file = new File(currentOutputVideoPath);
                if (file.exists()) {
                    file.delete();
                }
                String cmd = "";
                String textPath = IMG_PATH + getImgFileName();
                boolean result = FileUtils.textToPicture(textPath, "20191120091020", "444444444444444444,555555555555555555", this);
                if (result) {
                    if (videoGotation == 90 || videoGotation == 270) {//之前以为和旋转的角度有关系，原来
                        cmd = StringUtil.cmdVideo(videoPath, textPath, currentOutputVideoPath, 1);
                    } else {
                        if (videoWidth > videoHeight) {
                            cmd = StringUtil.cmdVideo(videoPath, textPath, currentOutputVideoPath, 2);
                        } else {
                            cmd = StringUtil.cmdVideo(videoPath, textPath, currentOutputVideoPath, 1);
                        }
                    }
                } else {
                }
                mProcessingDialog.show();
                mProcessingDialog.setProgress(0);
                execCommand(cmd);
            }
        } catch (Exception e) {
            Log.i("TAG", "startCompress=e=" + e.getMessage());
        }
    }

    private void execCommand(final String cmd) {
        File mFile = new File(currentOutputVideoPath);
        if (mFile.exists()) {
            mFile.delete();
        }
        mCompressor.execCommand(cmd, new CompressListener() {
            @Override
            public void onExecSuccess(String message) {
                mProcessingDialog.dismiss();
                mStrFFmpeg = "原始视频大小：" + FileUtils.getFileSize(mVideoPath) + "==>压缩后大小：" + FileUtils.getFileSize(currentOutputVideoPath);
                Log.d("TAG", mStrFFmpeg);
            }

            @Override
            public void onExecFail(String reason) {
                mProcessingDialog.dismiss();
                finish();
            }

            @Override
            public void onExecProgress(String message) {
                try {
                    double switchNum = getProgress(message);
                    if (switchNum == 10000) {
                        //如果找不到压缩的片段，返回为10000
                        mProcessingDialog.setProgress(0);
                    } else {
                        mProcessingDialog.setProgress((int) (getProgress(message) / 10));
                    }
                } catch (Exception e) {
                    mProcessingDialog.dismiss();
                }
            }
        });
    }

    /**
     * 进度条，只能是整形，所以max为1000，最少为0
     *
     * @param source
     * @return
     */
    double getProgressNum = 0.0;

    private double getProgress(String source) {
        if (source.contains("too large")) {//当文件过大的时候，会会出现 Past duration x.y too large
            return getProgressNum;
        }
        Pattern p = Pattern.compile("00:\\d{2}:\\d{2}");
        Matcher m = p.matcher(source);
        if (m.find()) {
            //00:00:00
            String result = m.group(0);
            String temp[] = result.split(":");
            double seconds = Double.parseDouble(temp[1]) * 60 + Double.parseDouble(temp[2]);
            if (0 != videoLength) {
                getProgressNum = seconds / videoLength * 1000;
                return seconds / videoLength * 1000;
            }
            if (seconds == videoLength) {
                return 1000;
            }
        }
        return 10000;//出现异常的时候，返回为10000
    }

    /**
     * 获取视频的时长
     */
    void getVideoTime(String videoPath) {
        try {
            MediaMetadataRetriever retr = new MediaMetadataRetriever();
            retr.setDataSource(videoPath);
            videoTime = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);//获取视频时长
            videoWidth = Integer.valueOf(retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));//获取视频的宽度
            videoHeight = Integer.valueOf(retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));//获取视频的高度
            videoGotation = Integer.valueOf(retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));//获取视频的角度
            videoLength = Double.parseDouble(videoTime) / 1000.00;
        } catch (Exception e) {
            e.printStackTrace();
            videoLength = 0.00;
            finish();
        }
    }
}