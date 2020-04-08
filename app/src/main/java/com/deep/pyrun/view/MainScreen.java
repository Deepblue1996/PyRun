package com.deep.pyrun.view;

import android.app.AppOpsManager;
import android.app.usage.UsageEvents;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deep.dpwork.annotation.DpLayout;
import com.deep.dpwork.annotation.DpStatus;
import com.deep.dpwork.itface.RunUi;
import com.deep.dpwork.util.Lag;
import com.deep.dpwork.util.ToastUtil;
import com.deep.dpwork.weight.StatusBarPaddingView;
import com.deep.pyrun.R;
import com.deep.pyrun.base.TBaseScreen;
import com.deep.pyrun.bean.ScreenState;
import com.deep.pyrun.broadcast.ScreenBroadcastReceive;
import com.deep.pyrun.service.AutoClickService;
import com.deep.pyrun.service.RecordService;
import com.deep.pyrun.util.DoModeUtil;
import com.deep.pyrun.util.PackageName;
import com.deep.pyrun.util.ScreenDeUtil;
import com.deep.pyrun.util.ScreenRunUtil;
import com.deep.pyrun.util.ScreenSeeUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.BindView;

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

    @BindView(R.id.statusView)
    StatusBarPaddingView statusView;

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

    public static AutoClickService autoClickService;

    @Override
    public void init() {

        // 悬浮窗创建
        screen = createView(R.layout.screen_layout);

        screenImg = screen.findViewById(R.id.screenImg);
        contentText = screen.findViewById(R.id.contentText);
        contentText2 = screen.findViewById(R.id.contentText2);

        if (checkFloatPermission(getContext())) {
            createToucher(screen);
        } else {

            String androidSDK = Build.VERSION.SDK;
            if (Integer.parseInt(androidSDK) >= 23 && !Settings.canDrawOverlays(_dpActivity)) {
                Intent intent2 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivityForResult(intent2, 1065);
            }

        }

        // 绑定服务
        Intent intent = new Intent(_dpActivity, RecordService.class);
        _dpActivity.bindService(intent, connection, BIND_AUTO_CREATE);

        // 绑定服务
        Intent intent2 = new Intent(_dpActivity, AutoClickService.class);
        _dpActivity.bindService(intent2, connection2, BIND_AUTO_CREATE);

        // 申请录制屏幕，不做保存
        projectionManager = (MediaProjectionManager) _dpActivity.getSystemService(MEDIA_PROJECTION_SERVICE);

        Intent captureIntent = projectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, RECORD_REQUEST_CODE);

    }

    private boolean checkFloatPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                Class cls = Class.forName("android.content.Context");
                Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                if (!(obj instanceof String)) {
                    return false;
                }
                String str2 = (String) obj;
                obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
                cls = Class.forName("android.app.AppOpsManager");
                Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                declaredField2.setAccessible(true);
                Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
                return result == declaredField2.getInt(cls);
            } catch (Exception e) {
                return false;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                if (appOpsMgr == null)
                    return false;
                int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                        .getPackageName());
                return mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
            } else {
                return Settings.canDrawOverlays(context);
            }
        }
    }

    @Override
    public void onBack() {
        return;
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

    private ServiceConnection connection2 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    @Override
    public void onDestroy() {
        recordService.stopSelf();
        autoClickService.stopSelf();
        ScreenRunUtil.get().recycle();
        super.onDestroy();
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

        if (requestCode == 1065) {
            if (checkFloatPermission(getContext())) {
                ToastUtil.showSuccess("悬浮窗权限申请成功...");
                createToucher(screen);
            } else {
                ToastUtil.showError("悬浮窗权限申请失败...");
            }
        } else if (requestCode == RECORD_REQUEST_CODE && resultCode == RESULT_OK) {
            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            recordService.setMediaProject(mediaProjection);
            recordService.startRecord();

            recordService.setBitmapScreenListener(new RecordService.BitmapScreenListener() {

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
                            screen.setVisibility(View.VISIBLE);
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
