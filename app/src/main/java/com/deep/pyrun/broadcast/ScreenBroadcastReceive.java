package com.deep.pyrun.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 旋转屏幕
 * Created by Administrator on 2019/6/29 0029.
 */

public class ScreenBroadcastReceive extends BroadcastReceiver {

    private ScreenListener screenListener;

    public void setScreenListener(ScreenListener screenListener) {
        this.screenListener = screenListener;
    }

    public interface ScreenListener {
        void has();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (screenListener != null) {
            screenListener.has();
        }
    }
}
