package com.deep.pyrun.script.bean;

import com.deep.pyrun.bean.ScreenState;

/**
 * 脚本基本属性
 * Created by Administrator on 2019/6/30 0030.
 */

public class ScriptBean {
    public ScreenState screenState;
    public int buZhou;
    public long lastTouchTime;

    public ScriptBean() {
        screenState = new ScreenState();
        buZhou = 0;
        lastTouchTime = 0;
    }

    public ScriptBean(ScreenState screenState, int buZhou, long lastTouchTime) {
        this.screenState = screenState;
        this.buZhou = buZhou;
        this.lastTouchTime = lastTouchTime;
    }
}
