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

import com.deep.dpwork.util.DoubleUtil;
import com.deep.pyrun.bean.MatXy;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

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
        //创建
        Bitmap binarymap = null;
        binarymap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        //依次循环，对图像的像素进行处理 �?
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //得到当前像素的
                int col = binarymap.getPixel(i, j);
                //得到alpha通道的
                int alpha = col & 0xFF000000;
                //得到图像的像素RGB的
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
                //设置新图像的当前像素值
                binarymap.setPixel(i, j, newColor);
            }
        }
        return binarymap;
    }

    public static void isHaveInBitmap(Bitmap src, Bitmap res, BitmapTouchXy bitmapTouchXy) {

        // 从左到右
        int colors[] = new int[25];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                colors[(i + 1) * (j + 1) - 1] = res.getPixel(res.getWidth() / 2 - 1 + j, res.getHeight() / 2 - 1 + i);
            }
        }

        int si = 0;
        int x = 0;
        int y = 0;

        for (int i = 0; i < src.getWidth(); i++) {
            for (int j = 0; j < src.getHeight(); j++) {
                // 最低要求
                if (i < src.getWidth() - 5 && j < src.getHeight() - 5) {
                    int num = 0;
                    for (int k = 0; k < 5; k++) {
                        for (int l = 0; l < 5; l++) {
                            if (src.getPixel(i + (k + 1), j + (l + 1)) == colors[(k + 1) * (l + 1) - 1]) {
                                num++;
                            }
                        }
                    }
                    if (si < num) {
                        si = num;
                        x = i;
                        y = j;
                    }
                    if (num == 24) {
                        bitmapTouchXy.touch(24, i, j);
                        return;
                    }
                }
            }
        }
        bitmapTouchXy.touch(si, x, y);
    }

    public static MatXy find(Bitmap src, Bitmap res) {
        Mat mat1 = new Mat();
        Utils.bitmapToMat(src, mat1);
        Mat mat2 = new Mat();
        Utils.bitmapToMat(res, mat2);
        MatXy matXy = runs(mat1, mat2);
        Mat mat3 = matXy.mat;
        Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight());
        Utils.matToBitmap(mat3, bitmap);

        Bitmap bitmap2 = Bitmap.createBitmap(src, matXy.x, matXy.y, matXy.w, matXy.h);
        Mat mat4 = new Mat();
        Utils.bitmapToMat(bitmap2, mat4);

        Mat mat11 = new Mat();
        Mat mat22 = new Mat();
        Imgproc.cvtColor(mat2, mat11, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(mat4, mat22, Imgproc.COLOR_BGR2GRAY);

        double st = ImageFilter.comPareHist(mat11, mat22);

        matXy.si = (float) DoubleUtil.round(st, 2);

        if (st > 0.5) {
            matXy.have = true;
        } else {
            matXy.have = false;
        }
        matXy.bitmap = bitmap;
        matXy.bitmapRec = bitmap2;

        src.recycle();
        res.recycle();

        return matXy;
    }

    /**
     * 比较来个矩阵的相似度
     *
     * @param srcMat
     * @param desMat
     */
    public static double comPareHist(Mat srcMat, Mat desMat) {
        srcMat.convertTo(srcMat, CvType.CV_32F);
        desMat.convertTo(desMat, CvType.CV_32F);
        return Imgproc.compareHist(srcMat, desMat, Imgproc.CV_COMP_CORREL);
    }

    public static MatXy runs(Mat img, Mat templ) {

        int result_rows = img.rows() - img.rows() + 1;
        int result_cols = templ.cols() - templ.cols() + 1;
        Mat g_result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
        Imgproc.matchTemplate(img, templ, g_result, Imgproc.TM_CCOEFF); // 归一化平方差匹配法
        // Imgproc.matchTemplate(g_src, g_tem, g_result,
        // Imgproc.TM_CCOEFF_NORMED); // 归一化相关系数匹配法

        // Imgproc.matchTemplate(g_src, g_tem, g_result, Imgproc.TM_CCOEFF);
        // //
        // 相关系数匹配法：1表示完美的匹配；-1表示最差的匹配。

        // Imgproc.matchTemplate(g_src, g_tem, g_result, Imgproc.TM_CCORR); //
        // 相关匹配法

        // Imgproc.matchTemplate(g_src, g_tem, g_result,Imgproc.TM_SQDIFF); //
        // 平方差匹配法：该方法采用平方差来进行匹配；最好的匹配值为0；匹配越差，匹配值越大。

        // Imgproc.matchTemplate(g_src, g_tem,g_result,Imgproc.TM_CCORR_NORMED);
        // // 归一化相关匹配法
        Core.normalize(g_result, g_result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        org.opencv.core.Point matchLocation = new org.opencv.core.Point();
        Core.MinMaxLocResult mmlr = Core.minMaxLoc(g_result);

        matchLocation = mmlr.maxLoc; // 此处使用maxLoc还是minLoc取决于使用的匹配算法
        Imgproc.rectangle(img, matchLocation,
                new org.opencv.core.Point(matchLocation.x + templ.cols(), matchLocation.y + templ.rows()),
                new Scalar(0, 0, 0, 0));

        double w = templ.cols();
        double h = templ.rows();

//        Mat tmp = new Mat();
//        // 两个Mat对象差分求值
//        Core.absdiff(mat3, mat4, tmp);
//        // 计算矩阵元素之和
//        Scalar scalar = Core.sumElems(tmp);
//        System.out.println(scalar.toString());
//
//        if(scalar.val[0] > 777777) {
//            System.out.println("存在运动物体");
//        } else {
//            System.out.println("不存在运动物体");
//        }

        return new MatXy((int) matchLocation.x, (int) matchLocation.y, (int) w, (int) h, 0, false, img, null, null);
    }

    public interface BitmapTouchXy {
        void touch(int si, int x, int y);
    }
}