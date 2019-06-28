package com.deep.pyrun.view;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deep.pyrun.R;
import com.deep.pyrun.base.TBaseScreen;
import com.deep.pyrun.util.ImageFilter;
import com.deep.pyrun.util.RecordService;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.intelligence.dpwork.annotation.DpLayout;
import com.intelligence.dpwork.annotation.DpStatus;
import com.intelligence.dpwork.util.DoubleUtil;
import com.intelligence.dpwork.util.Lag;
import com.intelligence.dpwork.util.ToastUtil;
import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;

import butterknife.BindView;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.MEDIA_PROJECTION_SERVICE;
import static com.deep.pyrun.core.WorkCore.DATAPATH;
import static com.deep.pyrun.core.WorkCore.DEFAULT_LANGUAGE;

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

    long now = 0;

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

    private void runText(Bitmap bitmap) {

        now = System.currentTimeMillis();

        TessBaseAPI tessBaseAPI = new TessBaseAPI();

        tessBaseAPI.init(DATAPATH, DEFAULT_LANGUAGE);

        tessBaseAPI.setImage(bitmap);
        final String text = tessBaseAPI.getUTF8Text();
        Lag.i(text);
        Lag.i("耗时: " + (System.currentTimeMillis() - now));

        contentText.setText(text);

        if (text.contains("任务") || text.contains("主") || text.contains("晋") || text.contains("支") || text.contains("队伍")) {
            //renWuMainBan(false);
            ToastUtil.showSuccess("文字分析结果: " + text);
        } else {
            //renWuMainBan(true);
            ToastUtil.showError("文字分析结果: " + text);
        }

        tessBaseAPI.end();
    }

    private void renWuMainBan(boolean open) {
        // adb shell input tap 540 1104
        if (open) {
            CommandResult result = Shell.SU.run("input tap 2125 236");
            if (result.isSuccessful()) {
                Lag.i("打开任务板 成功" + result.getStdout());
                // Example output on a rooted device:
                // uid=0(root) gid=0(root) groups=0(root) context=u:r:init:s0
            }
            //RootCmd.execRootCmd("input tap 2125 236");
        } else {
            CommandResult result = Shell.SU.run("input tap 1750 236");
            if (result.isSuccessful()) {
                Lag.i("关闭任务板 成功" + result.getStdout());
                // Example output on a rooted device:
                // uid=0(root) gid=0(root) groups=0(root) context=u:r:init:s0
            }
            //RootCmd.execRootCmd("input tap 1750 236");
        }
    }

    private void renWuDuiWu(boolean open) {
        // adb shell input tap 540 1104
        if (open) {
            CommandResult result = Shell.SU.run("input tap 1850 236");
            if (result.isSuccessful()) {
                Lag.i("打开面板任务板 成功" + result.getStdout());
                // Example output on a rooted device:
                // uid=0(root) gid=0(root) groups=0(root) context=u:r:init:s0
            }
            //RootCmd.execRootCmd("input tap 2125 236");
        } else {
            CommandResult result = Shell.SU.run("input tap 2125 236");
            if (result.isSuccessful()) {
                Lag.i("关闭面板队伍板 成功" + result.getStdout());
                // Example output on a rooted device:
                // uid=0(root) gid=0(root) groups=0(root) context=u:r:init:s0
            }
            //RootCmd.execRootCmd("input tap 1750 236");
        }
    }

    public Bitmap zoomImage(Bitmap bgimage, double newWidth,
                            double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    private Bitmap biF(Bitmap bitmap) {
        //x=0;
        // 1080:2030
        // 2030:1080
        float bi = (float) 0.532;
        float w = 1080;
        float h = 1080 * bi;
        float x = 0;
        float y = 2030 / 2 - h / 2;
        return Bitmap.createBitmap(bitmap, (int) x, (int) y, (int) w, (int) h);
    }

    private boolean isRun = false;

    public void haveNewBit(Bitmap bitmap) {

        float bi = 2;
        float w = 1080;
        float h = 540;

        float x = 0;
        float y = 2160 / 2 - h / 2;

        float xx = x + (float) DoubleUtil.divide(1796, 2, 2);
        float xy = y + (float) DoubleUtil.divide(60, 2, 2);
        float xw = (float) DoubleUtil.divide(364, 2, 2);
        float xh = (float) DoubleUtil.divide(80, 2, 2);

        //2160/2030

        //1080 2030 1796
        // 1796 189 364 555
        //bitmapRightNow = Bitmap.createBitmap(bitmap, 1796, 189, 364, 555);

        Bitmap map1 = Bitmap.createBitmap(bitmap, (int) xx, (int) xy, (int) xw, (int) xh);
        Bitmap renwu = BitmapFactory.decodeResource(_dpActivity.getResources(), R.mipmap.renwu, null);

        //Bitmap map2 = ImageFilter.grayScale(map1);
        //Bitmap map3 = ImageFilter.grayScale(map2);

        //runText(map2);
        ImageFilter.isHaveInBitmap(map1, renwu, new ImageFilter.BitmapTouchXy() {
            @Override
            public void touch(int si, int x, int y) {
                if (si > 10) {
                    if (x < 10) {
                        Lag.i("找到任务面板");
                        contentText.setText("找到任务面板");
                        renWuDuiWu(false);
                    } else {
                        Lag.i("找到队伍面板");
                        contentText.setText("找到队伍面板");
                        renWuDuiWu(true);
                    }
                    //renWuMainBan(false);
                } else {
                    Lag.i("没有找到任务面板");
                    contentText.setText("没有找到任务面板");
                    renWuMainBan(true);
                }
            }
        });

        screenImg2.setImageBitmap(map1);

        //runText(map1);
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
                @Override
                public void screenShow(Bitmap bitmap) {
                    screenImg.setImageBitmap(bitmap);
                    if (nowSystem == 0 || System.currentTimeMillis() - nowSystem > 2000) {
                        //haveNewBit(zoomImage(biF(bitmap), 2160, 1080));
                        haveNewBit(bitmap);
                        nowSystem = System.currentTimeMillis();
                    }
                }
            });
        }
    }

}
