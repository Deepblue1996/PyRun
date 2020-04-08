package com.deep.pyrun.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.deep.pyrun.broadcast.ScreenBroadcastReceive;
import com.deep.pyrun.view.MainScreen;

import java.nio.ByteBuffer;


public class RecordService extends Service {
    private MediaProjection mediaProjection;
    //private MediaRecorder mediaRecorder;
    private VirtualDisplay virtualDisplay;

    private boolean running;

    private int width = 1080;
    private int height = 2160;

    private ImageReader mImageReader;

    private Handler mHandler;

    private BitmapScreenListener bitmapScreenListener;
    private ScreenListener screenListener;

    public ScreenBroadcastReceive screenBroadcastReceive;

    @Override
    public IBinder onBind(Intent intent) {
        return new RecordBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread serviceThread = new HandlerThread("service_thread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        serviceThread.start();
        running = false;
        mHandler = new Handler();
        //mediaRecorder = new MediaRecorder();

        screenBroadcastReceive = new ScreenBroadcastReceive();
        //注册广播接收,注意：要监听这个系统广播就必须用这种方式来注册，不能再xml中注册，否则不能生效
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        registerReceiver(screenBroadcastReceive, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenBroadcastReceive);
    }

    public void setMediaProject(MediaProjection project) {
        mediaProjection = project;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean startRecord() {
        if (mediaProjection == null || running) {
            return false;
        }

        createVirtualDisplay();

        initRecorder();

        running = true;
        return true;
    }


    public boolean stopRecord() {
        if (!running) {
            return false;
        }
        running = false;

        mImageReader.close();

        virtualDisplay.release();
        //mediaProjection.stop();

        return true;
    }

    private void createVirtualDisplay() {

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        int dpi = metrics.densityDpi;

        mImageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 3);

        virtualDisplay = mediaProjection.createVirtualDisplay("MainScreen", width, height, dpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.getSurface(), null, null);
    }

    private long nowTime = 0;

    private void initRecorder() {
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                // 控制速度
//                if (nowTime != 0 && System.currentTimeMillis() - nowTime < 500) {
//                    Lag.i("出错");
//                    return;
//                }
//                nowTime = System.currentTimeMillis();
//                Lag.i("跑进来");
                Image image;
                try {
                    image = reader.acquireLatestImage();
                    if (image != null) {
                        final Image.Plane[] planes = image.getPlanes();
                        if (planes.length > 0) {
                            final ByteBuffer buffer = planes[0].getBuffer();
                            int pixelStride = planes[0].getPixelStride();
                            int rowStride = planes[0].getRowStride();
                            int rowPadding = rowStride - pixelStride * width;

                            int dw = width + rowPadding / pixelStride;
                            int dh = height;

                            // create bitmap
                            Bitmap bmp = Bitmap.createBitmap(dw, dh, Bitmap.Config.ARGB_8888);
                            bmp.copyPixelsFromBuffer(buffer);

                            Bitmap croppedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height);

                            if (bitmapScreenListener != null && croppedBitmap != null) {
                                bitmapScreenListener.screenShow(croppedBitmap);
                            }

//                            if (croppedBitmap != null) {
//                                croppedBitmap.recycle();
//                            }
                            if (!bmp.isRecycled()) {
                                // 回收并且置为null
                                bmp.recycle();
                            }

                            System.gc();
                        }
                    }
                    if (image != null) {
                        image.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, mHandler);
    }

    public void setBitmapScreenListener(BitmapScreenListener bitmapScreenListener) {
        this.bitmapScreenListener = bitmapScreenListener;
    }

    public interface BitmapScreenListener {
        void screenShow(Bitmap bitmap);
    }

    public interface ScreenListener {
        void has();
    }

    public class RecordBinder extends Binder {
        public RecordService getRecordService() {
            return RecordService.this;
        }
    }

}