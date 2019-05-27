package com.example.modulemain.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.modulecommon.common.ARouteContants;
import com.example.modulecommon.utils.FileUtils;
import com.example.modulecommon.utils.GetPathFromUri;
import com.example.modulecommon.utils.StringUtil;
import com.example.modulecommon.widget.dialog.widget.CustomProgressDialog;
import com.example.modulemain.R;
import com.example.modulemain.inter.CompressListener;
import com.example.modulemain.inter.InitListener;
import com.example.modulemain.utils.Compressor;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.modulecommon.common.Contants.Common.IMG_PATH;
import static com.example.modulecommon.common.Contants.Common.VIDEO_PATH;
import static com.example.modulecommon.utils.GetPathFromUri.getImgFileName;

/**
 * 相机调用界面
 */
@Route(path = ARouteContants.ModuleMain.CAMERA_ACTIVITY)
public class WatchActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnWatchPhoto;
    private Button mBtnWatchShoot;
    private Button mBtnFFmpeg;
    private TextView mTvFFmpeg;
    private Button mBtnmp4praser;
    private Button mBtnMediaCodec;
    private String currentOutputVideoPath = "";//压缩后的视频地址
    private Compressor mCompressor;
    private CustomProgressDialog mProcessingDialog;
    private String videoTime = "";//获取视频时长
    private int videoWidth = 0;//获取视频的宽度
    private int videoHeight = 0;//获取视频的高度
    private int videoGotation = 0;//获取视频的角度
    private Double videoLength = 0.00;//视频时长 s
    private String mVideoPath = "";//原视频地址
    private String mStrFFmpeg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        getPermissions();
        initView();
        initListener();
        initFile();
        initVideo();
    }

    private void initView() {
        mBtnWatchPhoto = findViewById(R.id.watch_photo);
        mBtnWatchShoot = findViewById(R.id.watch_shoot);
        mBtnFFmpeg = findViewById(R.id.ffmpeg);
        mTvFFmpeg = findViewById(R.id.tv_ffmpeg);
        mBtnmp4praser = findViewById(R.id.mp4praser);
        mBtnMediaCodec = findViewById(R.id.MediaCodec);
    }

    private void initListener() {
        mBtnWatchPhoto.setOnClickListener(this);
        mBtnWatchShoot.setOnClickListener(this);
        mBtnFFmpeg.setOnClickListener(this);
        mBtnmp4praser.setOnClickListener(this);
        mBtnMediaCodec.setOnClickListener(this);
    }

    private void initFile() {
        if(!FileUtils.makeRootDirectory(VIDEO_PATH) || !FileUtils.makeRootDirectory(IMG_PATH)){
            Toast.makeText(this,"文件夹创建失败",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.watch_photo) {
            Intent intent = new Intent();
            // 指定开启系统相机的Action
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        } else if (i == R.id.watch_shoot) {
            Intent intent = new Intent();
            // 指定开启系统相机的Action
            intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
            //设置视频录制最大时长
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(intent, 1001);
        } else if (i == R.id.ffmpeg) {
            startCompress(mVideoPath);
        } else if (i == R.id.mp4praser) {
        } else if (i == R.id.MediaCodec) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1001) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                //获取文件路径
                mVideoPath = GetPathFromUri.getPath(WatchActivity.this, uri);
                //获取视频时长显示进度条
                getVideoTime(mVideoPath);
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
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
                mTvFFmpeg.setText(mStrFFmpeg);
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
     * @dec 一句代码搞定权限问题
     * @author apeng
     * @date 2018/10/31 10:54
     */
    public void getPermissions() {
        XXPermissions.with(this)
                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .permission("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")
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
