package com.deep.pyrun.util;

import android.graphics.Bitmap;

import com.deep.dpwork.util.Lag;
import com.deep.pyrun.view.MainScreen;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

/**
 * 是否在运动
 * Created by Administrator on 2019/6/29 0029.
 */

public class ScreenRunUtil {

    public static boolean peopleIsRun = false;
    public static long peopleIsRunUpdate = 0;

    private static Bitmap bitmapQ;
    private static Bitmap bitmapH;
    private static Mat bitmapQMat;
    private static Mat bitmapHMat;

    private long qTime = 0;
    private long hTime = 0;

    private boolean haveUpdate = false;

    private static ScreenRunUtil screenRunUtil = null;

    public static ScreenRunUtil get() {
        if (screenRunUtil == null) {
            screenRunUtil = new ScreenRunUtil();
        }
        return screenRunUtil;
    }

    public void addBitmap(Bitmap bitmap) {
        if (bitmapQ == null) {
            bitmapQ = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            bitmapQMat = new Mat();
            Utils.bitmapToMat(bitmapQ, bitmapQMat);
            qTime = System.currentTimeMillis();
            Lag.i("添加前");
        } else if (bitmapH == null) {
            bitmapH = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            bitmapHMat = new Mat();
            Utils.bitmapToMat(bitmapH, bitmapHMat);
            hTime = System.currentTimeMillis();
            Lag.i("添加后");
            haveUpdate = true;
        } else {
            if (System.currentTimeMillis() - hTime > 2000) {
                recycleSmall();
                bitmapQ = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                qTime = System.currentTimeMillis();
                bitmapQMat = new Mat();
                Utils.bitmapToMat(bitmapQ, bitmapQMat);
                Lag.i("添加后前");
                haveUpdate = false;
            }
        }
    }

    /**
     * 是否运动
     *
     * @return
     */
    public boolean isRun() {

        if (!haveUpdate) {
            return false;
        }

        if (bitmapQ == null || bitmapH == null) {
            return false;
        }

        Mat tmp = new Mat();
        // 两个Mat对象差分求值
        Core.absdiff(bitmapQMat, bitmapHMat, tmp);
        // 计算矩阵元素之和
        Scalar scalar = Core.sumElems(tmp);

        int sc = (int) scalar.val[0];
        Lag.i("值:" + sc);

        if (sc > 10000000) {
            Lag.i("值: 人物在移动");
            // 判断上一次命令是否相同
            if(!peopleIsRun) {
                peopleIsRunUpdate = System.currentTimeMillis();
                MainScreen.scriptName = "人物在移动";
            }
            peopleIsRun = true;
            return true;
        } else {
            Lag.i("值: 人物没有在移动");
            // 判断上一次命令是否相同
            if(peopleIsRun) {
                peopleIsRunUpdate = System.currentTimeMillis();
                MainScreen.scriptName = "人物没有在移动";
            }
            peopleIsRun = false;
            return false;
        }
    }

    private void recycleSmall() {

        if (bitmapQ == null || bitmapH == null) {
            return;
        }

        if (!bitmapQ.isRecycled()) {
            // 回收并且置为null
            bitmapQ.recycle();
            bitmapQ = null;
        }
        if (!bitmapH.isRecycled()) {
            // 回收并且置为null
            bitmapH.recycle();
            bitmapH = null;
        }

        System.gc();
    }

    public void recycle() {

        if (bitmapQ == null || bitmapH == null) {
            screenRunUtil = null;
            return;
        }

        if (!bitmapQ.isRecycled()) {
            // 回收并且置为null
            bitmapQ.recycle();
            bitmapQ = null;
        }
        if (!bitmapH.isRecycled()) {
            // 回收并且置为null
            bitmapH.recycle();
            bitmapH = null;
        }

        System.gc();

        screenRunUtil = null;
    }
}
