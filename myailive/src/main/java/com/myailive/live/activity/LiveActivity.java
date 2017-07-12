package com.myailive.live.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.myailive.live.R;
import com.ucloud.ulive.UCameraProfile;
import com.ucloud.ulive.UEasyStreaming;
import com.ucloud.ulive.UStreamingContext;
import com.ucloud.ulive.UStreamingProfile;
import com.ucloud.ulive.filter.video.gpu.USkinBeautyGPUFilter;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

public class LiveActivity extends Activity implements View.OnClickListener {


    String roomID;
    private UEasyStreaming mEasyStreaming;
    private String mRtmpAddress;
    private USkinBeautyGPUFilter mSkinBeautyGPUFilter;
    private CheckBox camFilter;
    private Button btnSwichCam;
    private Button btnStartLive;
    private TextureView textureView;
    private RelativeLayout rl_live_info;
    private boolean streamIsPrepare=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        Intent intent = getIntent();
        roomID = intent.getStringExtra("roomId");
        initView();

        //设置地址
        mRtmpAddress = "rtmp://publish3.cdn.ucloud.com.cn/ucloud/" + roomID;
        mSkinBeautyGPUFilter = new USkinBeautyGPUFilter();
        // level1: 磨皮；level2: 美白；level3: 红润；
        mSkinBeautyGPUFilter.setFilterLevel(90, 76, 34);
        pullStreaming();
        initTextureView();
    }

    private void initView() {
        camFilter = (CheckBox) findViewById(R.id.filter);
        btnSwichCam = (Button) findViewById(R.id.btn_swich_cam);
        btnStartLive = (Button) findViewById(R.id.btn_start_live);
        btnStartLive.setOnClickListener(this);
        btnSwichCam.setOnClickListener(this);
        textureView = (TextureView) findViewById(R.id.textureView);
        rl_live_info = (RelativeLayout) findViewById(R.id.rl_live_info);
    }

    /**
     * 直播推流
     */
    void pullStreaming() {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();

                //参数设置类，可设置类型包括音频、视频、摄像头、滤镜
                UCameraProfile cameraProfile = new UCameraProfile();
                //设置为后置摄像头
                cameraProfile.setCameraIndex(UCameraProfile.CAMERA_FACING_BACK);
                UStreamingProfile mStreamingProfile =
                        new UStreamingProfile.Builder()
                                .setCameraProfile(cameraProfile)
                                .setRtmpUrl(mRtmpAddress).build();
                //获取推流对象
                mEasyStreaming = UEasyStreaming.Factory.newInstance();
                //准备好推流
                streamIsPrepare = mEasyStreaming.prepare(mStreamingProfile);
                //启动推流
//                mEasyStreaming.startRecording();
                Looper.loop();
            }
        }.start();

    }

    void initTextureView() {
        textureView.setKeepScreenOn(true);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(final SurfaceTexture surface, final int width, final int height) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!streamIsPrepare) {
                            handler.postDelayed(this, 500);
                        } else {
                            mEasyStreaming.startPreview(surface, width, height);
                            rl_live_info.setVisibility(View.VISIBLE);
                            camFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        mEasyStreaming.setVideoGPUFilter(mSkinBeautyGPUFilter);
                                    } else {
                                        mEasyStreaming.setVideoGPUFilter(null);
                                    }
                                }
                            });
                        }
                    }
                }, 500);

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_swich_cam:
                if (mEasyStreaming != null) {
                    mEasyStreaming.switchCamera();
                }
                break;
            case R.id.btn_start_live:
                if (mEasyStreaming != null) {
                    if (mEasyStreaming.isRecording()) {
                        mEasyStreaming.stopRecording();
                        btnStartLive.setText("开始直播");
                    } else {
                        mEasyStreaming.startRecording();
                        btnStartLive.setText("结束直播");
                    }
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mEasyStreaming != null) {
            mEasyStreaming.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mEasyStreaming != null) {
            mEasyStreaming.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        if (mEasyStreaming != null) {
            mEasyStreaming.onDestroy();
        }
        super.onDestroy();
    }


}
