package com.deep.pyrun.script;

import com.deep.pyrun.bean.ScreenState;
import com.deep.pyrun.script.bean.ScriptBean;
import com.deep.pyrun.util.CmdSend;
import com.deep.pyrun.util.ScreenRunUtil;
import com.deep.pyrun.view.MainScreen;
import com.intelligence.dpwork.util.Lag;

/**
 * 驱魔
 * Created by Administrator on 2019/6/30 0030.
 */

public class QuMoScript {

    public static ScriptBean run(ScriptBean scriptBean) {
        ScreenState screenState = scriptBean.screenState;
        int buZhou = scriptBean.buZhou;
        long lastTouchTime = scriptBean.lastTouchTime;
        // 判断是否长时间不动 10s
        if (screenState != null) {
            // 如果出现青蛙驱魔参加
            if (screenState.huoDongRiLiQuMoDianJiType == 1) {
                CmdSend.dos(CmdSend.Dos.QuMoRenWuCanJia);
                return scriptBean;
            }
            // 如果在主界面
            if (screenState.zhuJieMainType == 1) {
                if (ScreenRunUtil.peopleIsRun) {
                    Lag.i("驱魔: 正在执行");
                    MainScreen.scriptName = "正在执行";
                } else {
                    Lag.i("驱魔: 人物不动，正在判断超时");
                    MainScreen.scriptName = "人物不动，正在判断超时";
                }
                if (!ScreenRunUtil.peopleIsRun && System.currentTimeMillis() - ScreenRunUtil.peopleIsRunUpdate > 15000 && buZhou == 0) {
                    Lag.i("驱魔: 人物不动超过十秒");
                    buZhou = 1;
                    CmdSend.dos(CmdSend.Dos.DaKaiHuoDong);
                    Lag.i("驱魔: 开启活动日历");
                    MainScreen.scriptName = "人物不动超过十秒, 开启活动日历";
                }
            }
            // 如果在活动日历
            if (screenState.huoDongRiLiType == 1) {
                buZhou = 1;
            }
            // 如果在战斗界面
            if (screenState.zhanDouJieMainType == 1) {
                Lag.i("驱魔: 正在战斗");
                MainScreen.scriptName = "正在战斗";
            }
            // 如果不在抽奖和不在继续驱魔
            if (buZhou > 0 && screenState.huoDongQuMoChouJiangType != 1 && screenState.huoDongQuMoJiXuType != 1) {
                // 判断是否成功进入活动日历
                Lag.i("驱魔: 已经申请任务");
                MainScreen.scriptName = "已经申请任务";
                if (screenState.huoDongRiLiType == 1) {
                    Lag.i("驱魔: 已经申请任务，但是在活动日历");
                    MainScreen.scriptName = "已经申请任务，但是在活动日历";
                    if (screenState.huoDongRiLiQuMoType == 1) {
                        Lag.i("驱魔: 已经申请任务，但是在活动日历驱魔");
                        MainScreen.scriptName = "已经申请任务，但是在活动日历驱魔";
                        CmdSend.dos(CmdSend.Dos.CanJiaQuMo);
                        Lag.i("驱魔: 参加驱魔");
                        MainScreen.scriptName = "参加驱魔";
                        lastTouchTime = System.currentTimeMillis();
                        buZhou = 3;
                    } else {
                        Lag.i("驱魔: 已经申请任务，但是在活动日历，点击一条龙");
                        MainScreen.scriptName = "已经申请任务，但是在活动日历，点击一条龙";
                        if (buZhou == 1) {
                            CmdSend.dos(CmdSend.Dos.DianJiYiTiaoLong);
                            Lag.i("驱魔: 活动日历点击一条龙");
                            MainScreen.scriptName = "活动日历点击一条龙";
                            buZhou = 2;
                        }
                    }
                    // 如果人物没有动
                } else if (screenState.zhuJieMainType == 1 && !ScreenRunUtil.peopleIsRun) {
                    if (System.currentTimeMillis() - lastTouchTime > 10000) {
                        buZhou = 0;
                        Lag.i("驱魔: 执行任务超时");
                        MainScreen.scriptName = "执行任务超时";
                    }
                }

            } else if (screenState.huoDongQuMoChouJiangType == 1) { // 驱魔抽奖
                // 超出两秒, 继续
                if (lastTouchTime == 0 || System.currentTimeMillis() - lastTouchTime > 2000) {
                    CmdSend.dos(CmdSend.Dos.QuMoChouJiang);
                    Lag.i("驱魔: 驱魔抽奖");
                    MainScreen.scriptName = "驱魔抽奖";
                    lastTouchTime = System.currentTimeMillis();
                }
            } else if (screenState.huoDongQuMoJiXuType == 1) { // 继续驱魔
                // 超出两秒, 继续
                if (lastTouchTime == 0 || System.currentTimeMillis() - lastTouchTime > 2000) {
                    CmdSend.dos(CmdSend.Dos.QuMoJiXu);
                    Lag.i("驱魔: 继续驱魔");
                    MainScreen.scriptName = "继续驱魔";
                    lastTouchTime = System.currentTimeMillis();
                }
            }
        }
        Lag.i("驱魔: ----------------------------------------------------------");
        return new ScriptBean(screenState, buZhou, lastTouchTime);
    }

}
