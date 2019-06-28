package com.deep.pyrun.view;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deep.pyrun.R;
import com.deep.pyrun.base.TBaseScreen;
import com.deep.pyrun.util.CmdSend;
import com.deep.pyrun.util.ImageFilter;
import com.deep.pyrun.util.RecordService;
import com.deep.pyrun.util.TextFilter;
import com.intelligence.dpwork.annotation.DpLayout;
import com.intelligence.dpwork.annotation.DpStatus;
import com.intelligence.dpwork.util.DoubleUtil;
import com.intelligence.dpwork.util.Lag;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.MEDIA_PROJECTION_SERVICE;

/**
 * Class -
 * <p>
 * Created by Deepblue on 2019/6/25 0025.
 */
@DpStatus(blackFont = true)
@DpLayout(R.layout.activity_main)
public class MainScreen extends TBaseScreen {

    private ImageView screenImg;
    private ImageView screenImg2;
    private TextView contentText;

    @BindView(R.id.title)
    TextView title;

    private static final int RECORD_REQUEST_CODE = 101;

    private MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;
    private RecordService recordService;

    @Override
    public void init() {

        View screen = createView(R.layout.screen_layout);

        screenImg = screen.findViewById(R.id.screenImg);
        screenImg2 = screen.findViewById(R.id.screenImg2);
        contentText = screen.findViewById(R.id.contentText);

        createToucher(screen);

        Intent intent = new Intent(_dpActivity, RecordService.class);
        _dpActivity.bindService(intent, connection, BIND_AUTO_CREATE);

        projectionManager = (MediaProjectionManager) _dpActivity.getSystemService(MEDIA_PROJECTION_SERVICE);

        Intent captureIntent = projectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, RECORD_REQUEST_CODE);

        initCmd();
    }

    private void initCmd() {
        //_dpActivity.moveTaskToBack(false);
        //kaiQiQuMo();
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            DisplayMetrics metrics = new DisplayMetrics();
            _dpActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            RecordService.RecordBinder binder = (RecordService.RecordBinder) service;
            recordService = binder.getRecordService();
            recordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Timer timer;

    private int buZhou = 0;

    private void quXiaoQuMo() {
        buZhou = 0;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void kaiQiQuMo() {
        quXiaoQuMo();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                switch (buZhou) {
                    case 0:
                        CmdSend.dos(5);
                        buZhou = 1;
                        break;
                    case 1:
                        CmdSend.dos(6);
                        buZhou = 2;
                        break;
                    case 2:
                        CmdSend.dos(7);
                        buZhou = 3;
                        break;
                    case 3:
                        CmdSend.dos(8);
                        buZhou = 0;
                        quXiaoQuMo();
                        break;
                }
            }
        }, 0, 3000);
    }

    public void haveNewBit(final Bitmap bitmap) {

        float bi = 2;
        float w = 1080;
        float h = 540;

        float x = 0;
        float y = 2160 / 2 - h / 2;

        final float xx = x + (float) DoubleUtil.divide(1796, 2, 2);
        final float xy = y + (float) DoubleUtil.divide(60, 2, 2);
        final float xw = (float) DoubleUtil.divide(364, 2, 2);
        final float xh = (float) DoubleUtil.divide(80, 2, 2);

        Bitmap map1 = Bitmap.createBitmap(bitmap, (int) xx, (int) xy, (int) xw, (int) xh);
        Bitmap renwu = BitmapFactory.decodeResource(_dpActivity.getResources(), R.mipmap.renwu, null);
        //Bitmap duiwu = BitmapFactory.decodeResource(_dpActivity.getResources(), R.mipmap.duiwu, null);

        ImageFilter.isHaveInBitmap(map1, renwu, new ImageFilter.BitmapTouchXy() {
            @Override
            public void touch(int si, int x, int y) {
                if (si > 10) {
                    if (x < 10) {
                        Lag.i("找到任务面板");
                        contentText.setText("找到任务面板");
                        //CmdSend.dos(4);

                        // 检测内容
                        Bitmap map2 = Bitmap.createBitmap(bitmap, (int) xx, (int) xy, (int) xw, (int) (6 * xh));

                        String str = TextFilter.bitmap2Text(map2);
                        if (!str.contains("驱")) {
                            kaiQiQuMo();
                        }
                    } else {
                        Lag.i("找到队伍面板");
                        contentText.setText("找到队伍面板");
                        CmdSend.dos(2);
                    }
                    //renWuMainBan(false);
                } else {
                    Lag.i("没有找到任务面板");
                    contentText.setText("没有找到任务面板");
                    CmdSend.dos(0);
                }
            }
        });

        screenImg2.setImageBitmap(map1);
    }

    private long nowSystem = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECORD_REQUEST_CODE && resultCode == RESULT_OK) {
            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            recordService.setMediaProject(mediaProjection);
            recordService.startRecord();

            recordService.setBitmapScreenListener(new RecordService.BitmapScreenListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
                @Override
                public void screenShow(Bitmap bitmap) {
                    screenImg.setImageBitmap(bitmap);
                    if (nowSystem == 0 || System.currentTimeMillis() - nowSystem > 4000) {
                        haveNewBit(bitmap);
                        nowSystem = System.currentTimeMillis();
                    }
                }
            });
        }
    }

}
