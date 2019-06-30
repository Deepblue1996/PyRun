package com.deep.pyrun.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

import com.deep.pyrun.R;
import com.deep.pyrun.bean.MatXy;

/**
 * 界面分析
 * Created by Administrator on 2019/6/29 0029.
 */

public class ScreenFindUtil {

    public static Bitmap decodeResouce(Resources resources, int id) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        options.inTargetDensity = value.density;//float scale = targetDensity / (float)density;//缩放比例
        return BitmapFactory.decodeResource(resources, id, options);
    }

    /**
     * 查找任务面板
     * @param res
     * @param bitmapSrc
     * @return
     */
    public static MatXy findRenWuMianBan(Resources res, Bitmap bitmapSrc) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapSrc, 1784, 184, bitmapSrc.getWidth() - 1784, 96);
        Bitmap b = decodeResouce(res, R.mipmap.renwumianban);
        return ImageFilter.find(bitmap, b);
    }

    /**
     * 查找队伍面板
     * @param res
     * @param bitmapSrc
     * @return
     */
    public static MatXy findRenDuiWuBan(Resources res, Bitmap bitmapSrc) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapSrc, 1784, 184, bitmapSrc.getWidth() - 1784, 96);
        Bitmap b = decodeResouce(res, R.mipmap.duwumianban);

        return ImageFilter.find(bitmap, b);
    }

    /**
     * 查找活动日历
     * @param res
     * @param bitmapSrc
     * @return
     */
    public static MatXy findHuoDongRiLi(Resources res, Bitmap bitmapSrc) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapSrc, 250, 0, 484, 188);
        Bitmap b = decodeResouce(res, R.mipmap.huodongrili);

        return ImageFilter.find(bitmap, b);
    }

    /**
     * 查找主界面
     * @param res
     * @param bitmapSrc
     * @return
     */
    public static MatXy findZhuJieMain(Resources res, Bitmap bitmapSrc) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapSrc, 494, 0, 120, 134);
        Bitmap b = decodeResouce(res, R.mipmap.dingburenwu);

        return ImageFilter.find(bitmap, b);
    }

    /**
     * 查找战斗界面
     * @param res
     * @param bitmapSrc
     * @return
     */
    public static MatXy findZhanDouMain(Resources res, Bitmap bitmapSrc) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapSrc, 26, 46, 82, 94);
        Bitmap b = decodeResouce(res, R.mipmap.zhandoujiemain);

        return ImageFilter.find(bitmap, b);
    }

    /**
     * 查找活动日志驱魔
     * @param res
     * @param bitmapSrc
     * @return
     */
    public static MatXy findHuoDongQuMo(Resources res, Bitmap bitmapSrc) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapSrc, 578, 202, 226, 168);
        Bitmap b = decodeResouce(res, R.mipmap.qumorenwu);

        return ImageFilter.find(bitmap, b);
    }

    /**
     * 查找活动日志驱魔点击
     * @param res
     * @param bitmapSrc
     * @return
     */
    public static MatXy findHuoDongQuMoDianJi(Resources res, Bitmap bitmapSrc) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapSrc, 1678, 602, 374, 100);
        Bitmap b = decodeResouce(res, R.mipmap.qumorenwudianji);

        return ImageFilter.find(bitmap, b);
    }

    /**
     * 查找是否出现了驱魔抽奖
     * @param res
     * @param bitmapSrc
     * @return
     */
    public static MatXy findQuMoChouJiang(Resources res, Bitmap bitmapSrc) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapSrc, 1678, 602, 374, 100);
        Bitmap b = decodeResouce(res, R.mipmap.qumochoujiang);

        return ImageFilter.find(bitmap, b);
    }

    /**
     * 查找是否出现了驱魔继续
     * @param res
     * @param bitmapSrc
     * @return
     */
    public static MatXy findQuMoJiXu(Resources res, Bitmap bitmapSrc) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapSrc, 1678, 602, 374, 100);
        Bitmap b = decodeResouce(res, R.mipmap.jixuqumo);

        return ImageFilter.find(bitmap, b);
    }

}
