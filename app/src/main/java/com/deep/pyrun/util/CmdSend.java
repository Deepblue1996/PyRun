package com.deep.pyrun.util;

import com.intelligence.dpwork.util.Lag;
import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;

/**
 * Class -
 * <p>
 * Created by Deepblue on 2019/6/28 0028.
 */

public class CmdSend {

    public static final String dakaiRenWu = "input tap 2125 236";
    public static final String guanBiRenWu = "input tap 1750 236";
    public static final String dakaiRenWuBan = "input tap 1850 236";
    public static final String guanBiDuiWuBan = "input tap 2125 236";
    public static final String renWuGunDongDingBu = "input swipe 1850 266 1850 600";
    public static final String daKaiHuoDong = "input tap 550 47";
    public static final String dianJiYiTiaoLong = "input tap 463 362";
    public static final String canJiaQuMo = "input tap 1092 290";
    public static final String quMoRenWuCanJia = "input tap 1905 661";
    public static final String quMoRenWuCanJiaZiDongPiPei = "input tap 1276 952";

    public static void dos(int num) {
        CommandResult result;
        String str = "";
        switch (num) {
            case 0:
                str = dakaiRenWu;
                Lag.i("执行打开任务");
                break;
            case 1:
                str = guanBiRenWu;
                Lag.i("执行关闭任务");
                break;
            case 2:
                str = dakaiRenWuBan;
                Lag.i("执行打开任务面板");
                break;
            case 3:
                str = guanBiDuiWuBan;
                Lag.i("执行打开队伍面板");
                break;
            case 4:
                str = renWuGunDongDingBu;
                Lag.i("执行滑动到面板顶部");
                break;
            case 5:
                str = daKaiHuoDong;
                Lag.i("打开活动面板");
                break;
            case 6:
                str = dianJiYiTiaoLong;
                Lag.i("点击一条龙");
                break;
            case 7:
                str = canJiaQuMo;
                Lag.i("面板参加驱魔");
                break;
            case 8:
                str = quMoRenWuCanJia;
                Lag.i("参加驱魔");
                break;
            case 9:
                str = quMoRenWuCanJiaZiDongPiPei;
                Lag.i("参加驱魔");
                break;
        }
        result = Shell.SU.run(str);
        if (result != null && result.isSuccessful()) {
            Lag.i("执行成功" + result.getStdout());
        }
    }
}
