package com.deep.pyrun.util;

import android.graphics.Bitmap;

/**
 * 竖屏裁剪横屏（低效率）
 * Created by Administrator on 2019/6/29 0029.
 */

public class Screen2ScreenUtil {
    public static Bitmap screen(Bitmap bitmap) {

        int w = 1080;
        int h = 540;
        int x = 0;
        int y = 2030 / 2 - h / 2;

        return Bitmap.createBitmap(bitmap, x, y, w, h);
    }
}
