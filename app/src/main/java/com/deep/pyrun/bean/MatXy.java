package com.deep.pyrun.bean;

import android.graphics.Bitmap;

import org.opencv.core.Mat;

/**
 * 分析属性
 * Created by Administrator on 2019/6/29 0029.
 */

public class MatXy {
    // 存在坐标
    public int x;
    public int y;
    public int w;
    public int h;
    // 相似度
    public float si;
    // 是否找到
    public boolean have;
    // 转换后的图框mat
    public Mat mat;
    // 目标
    public Bitmap bitmap;
    // 结果
    public Bitmap bitmapRec;

    public MatXy(int x, int y, int w, int h, float si, boolean have, Mat mat, Bitmap bitmap, Bitmap bitmapRec) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.si = si;
        this.have = have;
        this.mat = mat;
        this.bitmap = bitmap;
        this.bitmapRec = bitmapRec;
    }
}
