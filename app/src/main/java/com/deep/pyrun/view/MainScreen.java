package com.deep.pyrun.view;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deep.pyrun.R;
import com.deep.pyrun.base.TBaseScreen;
import com.deep.pyrun.bean.ScreenState;
import com.deep.pyrun.broadcast.ScreenBroadcastReceive;
import com.deep.pyrun.service.RecordService;
import com.deep.pyrun.util.DoModeUtil;
import com.deep.pyrun.util.PackageName;
import com.deep.pyrun.util.ScreenDeUtil;
import com.deep.pyrun.util.ScreenRunUtil;
import com.deep.pyrun.util.ScreenSeeUtil;
import com.intelligence.dpwork.annotation.DpLayout;
import com.intelligence.dpwork.annotation.DpStatus;
import com.intelligence.dpwork.itface.RunUi;
import com.intelligence.dpwork.util.Lag;
import com.intelligence.dpwork.util.ToastUtil;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.MEDIA_PROJECTION_SERVICE;

/**
 * Class - 主
 * <p>
 * Created by Deepblue on 2019/6/25 0025.
 */
@DpStatus(blackFont = false)
@DpLayout(R.layout.activity_main)
public class MainScreen extends TBaseScreen {

    private static final String appName = "com.tencent.ft";
    public static String scriptName = "";

    private View screen;
    private ImageView screenImg;
    private TextView contentText;
    private TextView contentText2;

    private static final int RECORD_REQUEST_CODE = 101;

    private MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;
    private RecordService recordService;

    @Override
    public void init() {

        // 悬浮窗创建
        screen = createView(R.layout.screen_layout);

        screenImg = screen.findViewById(R.id.screenImg);
        contentText = screen.findViewById(R.id.contentText);
        contentText2 = screen.findViewById(R.id.contentText2);

        createToucher(screen);

        // 绑定服务
        Intent intent = new Intent(_dpActivity, RecordService.class);
        _dpActivity.bindService(intent, connection, BIND_AUTO_CREATE);

        // 申请录制屏幕，不做保存
        projectionManager = (MediaProjectionManager) _dpActivity.getSystemService(MEDIA_PROJECTION_SERVICE);

        Intent captureIntent = projectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, RECORD_REQUEST_CODE);

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            RecordService.RecordBinder binder = (RecordService.RecordBinder) service;
            recordService = binder.getRecordService();

            recordService.screenBroadcastReceive.setScreenListener(new ScreenBroadcastReceive.ScreenListener() {
                @Override
                public void has() {
                    //int rotaion = _dpActivity.getWindowManager().getDefaultDisplay().getRotation() * 90;
                    recordService.stopRecord();
                    recordService.startRecord();
                    hasShowToast = false;
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        ScreenRunUtil.get().recycle();
    }

    private boolean hasShowToast = false;
    private boolean hasShowInit = false;

    // 判断ft是否在运行
    private boolean isRun = false;
    // 判断是否正在分析界面
    private boolean canFenXi = false;

    @SuppressWarnings("all")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECORD_REQUEST_CODE && resultCode == RESULT_OK) {
            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            recordService.setMediaProject(mediaProjection);
            recordService.startRecord();

            recordService.setBitmapScreenListener(new RecordService.BitmapScreenListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
                @Override
                public void screenShow(final Bitmap bitmap) {
                    if (PackageName.isRunApp(getContext(), appName) == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        if (!hasShowToast) {
                            screen.setVisibility(View.VISIBLE);
                            hasShowToast = true;
                            isRun = true;
                            // 开启驱魔脚本
                            DoModeUtil.get().start();
                            Lag.i("分析: Ft进入");
                        }
                    } else {
                        if (!hasShowToast) {
                            screen.setVisibility(View.GONE);
                            hasShowToast = true;
                            isRun = false;
                            Lag.i("分析: Ft未进入");
                        }
                    }
                    if (!hasShowInit) {
                        ToastUtil.showSuccess("初始化引擎成功，请进入游戏");
                        hasShowInit = true;
                    }
                    if (isRun) {

                        Lag.i("分析: Ft进入");

                        if (!canFenXi) {
                            canFenXi = true;

                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    ScreenSeeUtil.init(_dpActivity.getResources(), bitmap);

                                    if (ScreenSeeUtil.get() == null) {
                                        return;
                                    }

                                    try {
                                        // 界面判断分析
                                        final ScreenState screenState = ScreenDeUtil.runs();

                                        // 给予界面的分析结果
                                        DoModeUtil.get().update(screenState);

                                        // 显示
                                        runTextShow(bitmap, screenState);

                                        // 释放
                                        ScreenSeeUtil.get().recycle();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    canFenXi = false;
                                }
                            }).start();

                        }
                    }
                    screenImg.setImageBitmap(bitmap);
                }
            });
        }
    }

    private void runTextShow(final Bitmap bitmap, final ScreenState screenState) {

        runUi(new RunUi() {
            @Override
            public void run() {
                contentText2.setText(scriptName);
                contentText.setText("还没做分析");

                if (screenState.zhuJieMainType == 1) {
                    if (screenState.huoDongRiLiQuMoDianJiType == 1) {
                        contentText.setText("活动日历驱魔点击界面");
                    } else {

                        // 判断人物是否在跑
                        ScreenRunUtil.get().addBitmap(bitmap);
                        ScreenRunUtil.get().isRun();

                        if (screenState.renWuLanType == 1) {
                            contentText.setText("右侧任务栏任务已打开");
                        } else if (screenState.renWuLanType == 2) {
                            contentText.setText("右侧任务栏队伍已打开");
                        } else {
                            contentText.setText("右侧任务栏未打开");
                        }
                    }
                } else {
                    if (screenState.zhanDouJieMainType == 1) {
                        contentText.setText("战斗界面");
                    } else if (screenState.huoDongRiLiType == 1) {
                        contentText.setText("活动日历界面");
                        if (screenState.huoDongRiLiQuMoType == 1) {
                            contentText.setText("活动日历驱魔界面");
                        }
                    }
                }
            }
        });
    }
}
