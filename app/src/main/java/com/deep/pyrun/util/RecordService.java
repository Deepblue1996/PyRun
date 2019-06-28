package com.deep.pyrun.util;


import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;


public class RecordService extends Service {
    private MediaProjection mediaProjection;
    //private MediaRecorder mediaRecorder;
    private VirtualDisplay virtualDisplay;

    private boolean running;
    private int width = 720;
    private int height = 1080;
    private int dpi;

    private ImageReader mImageReader;

    private Handler mHandler;

    private BitmapScreenListener bitmapScreenListener;

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
        WindowManager windowManager = (WindowManager) getSystemService("window");
        width = windowManager.getDefaultDisplay().getWidth();
        height = windowManager.getDefaultDisplay().getHeight();
        //mediaRecorder = new MediaRecorder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setMediaProject(MediaProjection project) {
        mediaProjection = project;
    }

    public boolean isRunning() {
        return running;
    }

    public void setConfig(int width, int height, int dpi) {
        this.width = width;
        this.height = height;
        this.dpi = dpi;
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
        virtualDisplay.release();
        mediaProjection.stop();

        return true;
    }

    private void createVirtualDisplay() {

        mImageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 3);

        virtualDisplay = mediaProjection.createVirtualDisplay("MainScreen", width, height, dpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.getSurface(), null, null);
    }

    private void initRecorder() {
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
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

                            // create bitmap
                            Bitmap bmp = Bitmap.createBitmap(width + rowPadding / pixelStride,
                                    height, Bitmap.Config.ARGB_8888);
                            bmp.copyPixelsFromBuffer(buffer);

                            Bitmap croppedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height);

                            if (bitmapScreenListener != null && croppedBitmap != null) {
                                bitmapScreenListener.screenShow(croppedBitmap);
                            }

//                            if (croppedBitmap != null) {
//                                croppedBitmap.recycle();
//                            }
                            bmp.recycle();
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

    public class RecordBinder extends Binder {
        public RecordService getRecordService() {
            return RecordService.this;
        }
    }
}