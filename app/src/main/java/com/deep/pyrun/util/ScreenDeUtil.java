package com.deep.pyrun.util;

import com.deep.pyrun.bean.ScreenState;
import com.intelligence.dpwork.util.Lag;

/**
 * 返回状态链式
 * Created by Administrator on 2019/6/29 0029.
 */

public class ScreenDeUtil {

    // 1：正确 0:不对 -1:内存出错
    @SuppressWarnings("all")
    public static ScreenState runs() {
        ScreenState screenState = new ScreenState();
        screenState.zhuJieMainType = ScreenSeeUtil.get().isZhuJieMain();
        Lag.i("分析: 在主界面:"+screenState.zhuJieMainType);
        // 是否主界面
        if (screenState.zhuJieMainType == 1) {
            // 获取任务栏状态
            screenState.renWuLanType = ScreenSeeUtil.get().isOpenReWu();
            Lag.i("分析: 主界面->任务栏状态");
            if(screenState.renWuLanType == 0) {
                // 获取驱魔点击
                screenState.huoDongRiLiQuMoDianJiType = ScreenSeeUtil.get().isHuoDongRiLiQuMoDianJi();
                Lag.i("分析: 主界面->任务栏关闭->驱魔任务参加点击");
            }
            // 获取活动驱魔抽奖
            screenState.huoDongQuMoChouJiangType = ScreenSeeUtil.get().isQuMoChouJiang();
            if (screenState.huoDongQuMoChouJiangType != 1) {
                // 获取活动继续驱魔
                screenState.huoDongQuMoJiXuType = ScreenSeeUtil.get().isJiXuQuMo();
                Lag.i("分析: 继续驱魔");
            } else if(screenState.huoDongQuMoChouJiangType == 1) {
                Lag.i("分析: 驱魔抽奖");
            }
        } else if (screenState.zhuJieMainType == 0) { // 不在主界面
            Lag.i("分析: 不在主界面:"+screenState.zhuJieMainType);
            // 获取战斗界面
            screenState.zhanDouJieMainType = ScreenSeeUtil.get().isZhanDouMain();
            if (screenState.zhanDouJieMainType != 1) {
                Lag.i("分析: 不在战斗界面");
                // 获取活动日历
                screenState.huoDongRiLiType = ScreenSeeUtil.get().isHuoDongRiLi();
                // 判断是否在活动日历
                if (screenState.huoDongRiLiType == 1) {
                    Lag.i("分析: 活动界面");
                    // 获取活动日历驱魔
                    screenState.huoDongRiLiQuMoType = ScreenSeeUtil.get().isHuoDongRiLiQuMo();
                    if(screenState.huoDongRiLiQuMoType == 1) {
                        Lag.i("分析: 活动日历驱魔界面");
                    } else {
                        Lag.i("分析: 不在活动日历驱魔界面");
                    }
                } else {
                    // 获取活动驱魔抽奖
                    screenState.huoDongQuMoChouJiangType = ScreenSeeUtil.get().isQuMoChouJiang();
                    if (screenState.huoDongQuMoChouJiangType != 1) {
                        // 获取活动继续驱魔
                        screenState.huoDongQuMoJiXuType = ScreenSeeUtil.get().isJiXuQuMo();
                        Lag.i("分析: 继续驱魔");
                    } else if(screenState.huoDongQuMoChouJiangType == 1) {
                        Lag.i("分析: 驱魔抽奖");
                    }
                }
            } else if(screenState.zhanDouJieMainType == 1) {
                Lag.i("分析: 在战斗界面");
            }
        } else {
            Lag.i("内存出错");
        }
        return screenState;
    }
}
