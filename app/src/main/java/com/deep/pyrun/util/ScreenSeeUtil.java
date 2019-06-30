package com.deep.pyrun.util;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.deep.pyrun.bean.MatXy;
import com.intelligence.dpwork.util.Lag;

/**
 * 当前界面状态分析
 * Created by Administrator on 2019/6/29 0029.
 */

public class ScreenSeeUtil {

    private Bitmap bitmapTemp;
    private Resources resources;

    private static ScreenSeeUtil screenSeeUtil = null;

    public static void init(Resources resources, Bitmap bitmap) {
        if (screenSeeUtil == null) {
            screenSeeUtil = new ScreenSeeUtil();
        }
        screenSeeUtil.resources = resources;

        // 线程备用
        screenSeeUtil.bitmapTemp = bitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    public static ScreenSeeUtil get() {
        if (screenSeeUtil.bitmapTemp == null || screenSeeUtil.bitmapTemp.isRecycled() || screenSeeUtil == null) {
            Lag.i("出错");
            return null;
        }
        return screenSeeUtil;
    }

    public void recycle() {

        if (!bitmapTemp.isRecycled()) {
            // 回收并且置为null
            bitmapTemp.recycle();
        }

        System.gc();

        screenSeeUtil = null;
    }

    /**
     * 任务面板状态
     *
     * @return
     */
    public int isOpenReWu() {

        long runTime;
        int haveOpen = 0;

        Lag.i("开始分析任务");
        runTime = System.currentTimeMillis();

        final MatXy matXy = ScreenFindUtil.findRenWuMianBan(resources, bitmapTemp);
        final MatXy matXy2 = ScreenFindUtil.findRenDuiWuBan(resources, bitmapTemp);

        if (matXy.have) {
            haveOpen = 1;
        } else if (matXy2.have) {
            haveOpen = 2;
        }

        Lag.i("耗时:" + (System.currentTimeMillis() - runTime));

        return haveOpen;
    }

    /**
     * 活动日历
     *
     * @return
     */
    public int isHuoDongRiLi() {
        long runTime;
        int haveOpen = 0;

        Lag.i("开始分析任务");
        runTime = System.currentTimeMillis();

        final MatXy matXy = ScreenFindUtil.findHuoDongRiLi(resources, bitmapTemp);

        if (matXy.have) {
            haveOpen = 1;
        }

        Lag.i("耗时:" + (System.currentTimeMillis() - runTime));

        return haveOpen;
    }

    /**
     * 主界面
     *
     * @return
     */
    public int isZhuJieMain() {
        long runTime;
        int haveOpen = 0;

        Lag.i("开始分析任务");
        runTime = System.currentTimeMillis();

        final MatXy matXy = ScreenFindUtil.findZhuJieMain(resources, bitmapTemp);

        if (matXy.have) {
            haveOpen = 1;
        }

        Lag.i("耗时:" + (System.currentTimeMillis() - runTime));

        return haveOpen;
    }

    /**
     * 战斗界面
     *
     * @return
     */
    public int isZhanDouMain() {
        long runTime;
        int haveOpen = 0;

        Lag.i("开始分析任务");
        runTime = System.currentTimeMillis();

        final MatXy matXy = ScreenFindUtil.findZhanDouMain(resources, bitmapTemp);

        if (matXy.have) {
            haveOpen = 1;
        }

        Lag.i("耗时:" + (System.currentTimeMillis() - runTime));

        return haveOpen;
    }

    /**
     * 驱魔界面
     *
     * @return
     */
    public int isHuoDongRiLiQuMo() {
        long runTime;
        int haveOpen = 0;

        Lag.i("开始分析任务");
        runTime = System.currentTimeMillis();

        final MatXy matXy = ScreenFindUtil.findHuoDongQuMo(resources, bitmapTemp);

        if (matXy.have) {
            haveOpen = 1;
        }

        Lag.i("耗时:" + (System.currentTimeMillis() - runTime));

        return haveOpen;
    }

    /**
     * 活动日历驱魔点击
     *
     * @return
     */
    public int isHuoDongRiLiQuMoDianJi() {
        long runTime;
        int haveOpen = 0;

        Lag.i("开始分析任务");
        runTime = System.currentTimeMillis();

        final MatXy matXy = ScreenFindUtil.findHuoDongQuMoDianJi(resources, bitmapTemp);

        if (matXy.have) {
            haveOpen = 1;
        }

        Lag.i("耗时:" + (System.currentTimeMillis() - runTime));

        return haveOpen;
    }

    /**
     * 驱魔抽奖界面
     *
     * @return
     */
    public int isQuMoChouJiang() {
        long runTime;
        int haveOpen = 0;

        Lag.i("开始分析任务");
        runTime = System.currentTimeMillis();

        final MatXy matXy = ScreenFindUtil.findQuMoChouJiang(resources, bitmapTemp);

        if (matXy.have) {
            haveOpen = 1;
        }

        Lag.i("耗时:" + (System.currentTimeMillis() - runTime));

        return haveOpen;
    }

    /**
     * 继续驱魔界面
     *
     * @return
     */
    public int isJiXuQuMo() {
        long runTime;
        int haveOpen = 0;

        Lag.i("开始分析任务");
        runTime = System.currentTimeMillis();

        final MatXy matXy = ScreenFindUtil.findQuMoJiXu(resources, bitmapTemp);

        if (matXy.have) {
            haveOpen = 1;
        }

        Lag.i("耗时:" + (System.currentTimeMillis() - runTime));

        return haveOpen;
    }
}
