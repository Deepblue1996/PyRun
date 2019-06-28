package com.deep.pyrun.util;

import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.intelligence.dpwork.util.Lag;

import static com.deep.pyrun.core.WorkCore.DATAPATH;
import static com.deep.pyrun.core.WorkCore.DEFAULT_LANGUAGE;

/**
 * Class -
 * <p>
 * Created by Deepblue on 2019/6/28 0028.
 */

public class TextFilter {
    public static String bitmap2Text(Bitmap bitmap) {

        long now = System.currentTimeMillis();

        TessBaseAPI tessBaseAPI = new TessBaseAPI();

        tessBaseAPI.init(DATAPATH, DEFAULT_LANGUAGE);

        tessBaseAPI.setImage(bitmap);
        final String text = tessBaseAPI.getUTF8Text();

        Lag.i("分析："+text);

        Lag.i("耗时: " + (System.currentTimeMillis() - now));

        tessBaseAPI.end();

        return text;
    }
}
