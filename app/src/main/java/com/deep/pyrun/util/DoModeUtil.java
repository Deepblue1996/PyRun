package com.deep.pyrun.util;

import com.deep.pyrun.bean.ScreenState;
import com.deep.pyrun.script.QuMoScript;
import com.deep.pyrun.script.bean.ScriptBean;
import com.intelligence.dpwork.util.Lag;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 驱魔
 * Created by Administrator on 2019/6/30 0030.
 */

public class DoModeUtil {

    private Timer timer;

    private static DoModeUtil doModeUtil;

    private ScriptBean scriptBean;

    public static DoModeUtil get() {
        if (doModeUtil == null) {
            doModeUtil = new DoModeUtil();
            doModeUtil.scriptBean = new ScriptBean();
        }
        return doModeUtil;
    }

    public void start() {
        stop();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                scriptBean = QuMoScript.run(scriptBean);
            }
        }, 1000, 1000);
    }

    public void update(ScreenState screenState) {
        this.scriptBean.screenState = screenState;
    }

    @SuppressWarnings("all")
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
