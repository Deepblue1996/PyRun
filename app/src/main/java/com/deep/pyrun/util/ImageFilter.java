package com.deep.pyrun.util;

/**
 * Class -
 * <p>
 * Created by Deepblue on 2019/6/27 0027.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

import com.intelligence.dpwork.util.Lag;

/**
 * 图片灰度化和二值化处理
 * Created by kaifa on 2016/2/14.
 */
public class ImageFilter {

    /**
     * 黑白
     *
     * @param switchBitmap
     * @return
     */
    public Bitmap switchBlackNWhiteColor(Bitmap switchBitmap) {
        int width = switchBitmap.getWidth();
        int height = switchBitmap.getHeight();

        // Turn the picture black and white
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(switchBitmap, new Matrix(), new Paint());

        int current_color, red, green, blue, alpha, avg = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //get the color of each bit
                current_color = switchBitmap.getPixel(i, j);
                //achieve  three-primary color
                red = Color.red(current_color);
                green = Color.green(current_color);
                blue = Color.blue(current_color);
                alpha = Color.alpha(current_color);
                avg = (red + green + blue) / 3;// RGB average


                if (avg >= 126) {
                    newBitmap.setPixel(i, j, Color.argb(alpha, 255, 255, 255));// white
                } else if (avg < 126 && avg >= 115) {
                    newBitmap.setPixel(i, j, Color.argb(alpha, 108, 108, 108));//grey
                } else {
                    newBitmap.setPixel(i, j, Color.argb(alpha, 0, 0, 0));// black
                }


            }
        }
        return newBitmap;
    }

    /**
     * 图片灰度处理
     *
     * @return
     */
    public static Bitmap grayScale(Bitmap bitmap) {
        int width, height;
        height = bitmap.getHeight();
        width = bitmap.getWidth();
        Bitmap bmpGray = null;
        bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGray);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bitmap, 0, 0, paint);

        return bmpGray;
    }

    /**
     * 图片二值化处理
     *
     * @param bitmap
     * @return
     */
    public static Bitmap binaryzation(Bitmap bitmap) {

        //得到图形的宽度和长度
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //创建二�?化图像 �?
        Bitmap binarymap = null;
        binarymap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        //依次循环，对图像的像素进行处理 �?
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //得到当前像素的�?
                int col = binarymap.getPixel(i, j);
                //得到alpha通道的�?
                int alpha = col & 0xFF000000;
                //得到图像的像素RGB的�?
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
                int gray = (int) ((float) red * 0.3 + (float) green * 0.59 +
                        (float) blue * 0.11);
                //对图像进行二值化处理
                if (gray <= 95) {
                    gray = 0;
                } else {
                    gray = 255;
                }
                // 新的ARGB
                int newColor = alpha | (gray << 16) | (gray << 8) | gray;
                //设置新图像的当前像素值 �?
                binarymap.setPixel(i, j, newColor);
            }
        }
        return binarymap;
    }

    public static int whDuiBiBitmap(Bitmap src, Bitmap res) {

        int someNum = 0;

        if (src.getWidth() * src.getHeight() != res.getWidth() * res.getHeight()) {
            return someNum;
        }
        for (int i = 0; i < src.getWidth(); i++) {
            for (int j = 0; j < src.getHeight(); j++) {
                if (src.getPixel(i, j) == res.getPixel(i, j)) {
                    someNum++;
                }
            }
        }

        return someNum / (res.getWidth() * res.getHeight() / 100);
    }

    public static void isHaveInBitmap(Bitmap src, Bitmap res, BitmapTouchXy bitmapTouchXy) {

        int someNum = 0;
        int lastX = 0;
        int lastY = 0;

        for (int i = 0; i < src.getWidth(); i++) {
            for (int j = 0; j < src.getHeight(); j++) {
                if (src.getPixel(i, j) == res.getPixel(0, 0)) {
                    // 保证不超出
                    if (i + res.getWidth() < src.getWidth() && j + res.getHeight() < src.getHeight()) {

                        Bitmap srx = Bitmap.createBitmap(src, i, j, res.getWidth(), res.getHeight());

                        int someNumTemp = whDuiBiBitmap(srx, res);

                        if (someNumTemp > someNum) {
                            lastX = i;
                            lastY = j;
                            someNum = someNumTemp;
                        }
                    }
                }
            }
        }
        bitmapTouchXy.touch(someNum, lastX, lastY);
        Lag.i("存在符合 " + someNum + "% 坐标 x:" + lastX + "y:" + lastY);
    }

    public interface BitmapTouchXy {
        void touch(int si, int x, int y);
    }
}