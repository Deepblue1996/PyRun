package com.deep.pyrun.core;

import android.Manifest;
import android.os.Environment;

import com.deep.dpwork.DpWorkCore;
import com.deep.dpwork.annotation.DpPermission;
import com.deep.dpwork.util.Lag;
import com.deep.pyrun.util.PackageName;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Class - 框架入口
 * <p>
 * Created by Deepblue on 2018/9/29 0029.
 */
@DpPermission({
        Manifest.permission.BIND_ACCESSIBILITY_SERVICE,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.SYSTEM_ALERT_WINDOW,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE})
public class WorkCore extends DpWorkCore {

    /**
     * TessBaseAPI初始化用到的第一个参数，是个目录。
     */
    public static final String DATAPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    /**
     * 在DATAPATH中新建这个目录，TessBaseAPI初始化要求必须有这个目录。
     */
    public static final String tessdata = DATAPATH + File.separator + "tessdata";
    /**
     * TessBaseAPI初始化测第二个参数，就是识别库的名字不要后缀名。
     */
    public static final String DEFAULT_LANGUAGE = "chi_sim";
    /**
     * assets中的文件名
     */
    public static final String DEFAULT_LANGUAGE_NAME = DEFAULT_LANGUAGE + ".traineddata";
    /**
     * 保存到SD卡中的完整文件名
     */
    public static final String LANGUAGE_PATH = tessdata + File.separator + DEFAULT_LANGUAGE_NAME;


    /**
     * 框架初始化，并设置第一页面
     */
    @Override
    protected void initCore() {
        PackageName.checkUsageStateAccessPermission(getApplicationContext());
        initOpenCV();
        // copyToSD(LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
    }

    public void initOpenCV() {
        LoaderCallbackInterface mLoaderCallback = new BaseLoaderCallback(getApplicationContext()) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS: {
                        Lag.i("OpenCV loaded successfully");
                    }
                    break;
                    default: {
                        super.onManagerConnected(status);
                    }
                    break;
                }
            }

            @Override
            public void onPackageInstall(int operation, InstallCallbackInterface callback) {

            }
        };
        if (!OpenCVLoader.initDebug()) {
            Lag.i("Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, getApplicationContext(), mLoaderCallback);
        } else {
            Lag.i("OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void permissionComplete(boolean b) {
        if (b) {
            copyToSD(LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
        }
    }

    /**
     * 将assets中的识别库复制到SD卡中
     *
     * @param path 要存放在SD卡中的 完整的文件名。这里是"/storage/emulated/0//tessdata/chi_sim.traineddata"
     * @param name assets中的文件名 这里是 "chi_sim.traineddata"
     */
    public void copyToSD(String path, String name) {
        Lag.i("copyToSD: " + path);
        Lag.i("copyToSD: " + name);

        //如果存在就删掉
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        if (!f.exists()) {
            File p = new File(f.getParent());
            if (!p.exists()) {
                p.mkdirs();
            }
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = this.getAssets().open(name);
            File file = new File(path);
            os = new FileOutputStream(file);
            byte[] bytes = new byte[2048];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
