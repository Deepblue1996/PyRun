package com.deep.pyrun.util;

import com.deep.pyrun.service.AutoClickService;
import com.deep.pyrun.view.MainScreen;
import com.intelligence.dpwork.util.Lag;
import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;

/**
 * Class -
 * <p>
 * Created by Deepblue on 2019/6/28 0028.
 */

public class CmdSend {

    public enum Dos {
        DaKaiRenWu,
        GuanBiRenWu,
        DakaiRenWuBan,
        GuanBiDuiWuBan,
        RenWuGunDongDingBu,
        DaKaiHuoDong,
        DianJiYiTiaoLong,
        CanJiaQuMo,
        QuMoRenWuCanJia,
        QuMoRenWuCanJiaZiDongPiPei,
        QuMoChouJiang,
        QuMoJiXu
    }

    private static final String dakaiRenWu = "input tap 2125 236";
    private static final String guanBiRenWu = "input tap 1750 236";
    private static final String dakaiRenWuBan = "input tap 1850 236";
    private static final String guanBiDuiWuBan = "input tap 2125 236";
    private static final String renWuGunDongDingBu = "input swipe 1850 266 1850 600";
    private static final String daKaiHuoDong = "input tap 550 47";
    private static final String dianJiYiTiaoLong = "input tap 463 362";
    private static final String canJiaQuMo = "input tap 1092 290";
    private static final String quMoRenWuCanJia = "input tap 1905 661";
    private static final String quMoRenWuCanJiaZiDongPiPei = "input tap 1276 952";
    private static final String quMoChouJiang = "input tap 1033 535";
    private static final String quMoJiXu = "input tap 1203 594";

    private static final int[] dakaiRenWuSuZu = new int[]{2125, 236};
    private static final int[] guanBiRenWuSuZu = new int[]{1750, 236};
    private static final int[] dakaiRenWuBanSuZu = new int[]{1850, 236};
    private static final int[] guanBiDuiWuBanSuZu = new int[]{2125, 236};
    private static final int[] renWuGunDongDingBuSuZu = new int[]{1850, 266, 1850, 600};
    private static final int[] daKaiHuoDongSuZu = new int[]{550, 47};
    private static final int[] dianJiYiTiaoLongSuZu = new int[]{463, 362};
    private static final int[] canJiaQuMoSuZu = new int[]{1092, 290};
    private static final int[] quMoRenWuCanJiaSuZu = new int[]{1905, 661};
    private static final int[] quMoRenWuCanJiaZiDongPiPeiSuZu = new int[]{1276, 952};
    private static final int[] quMoChouJiangSuZu = new int[]{1033, 535};
    private static final int[] quMoJiXuSuZu = new int[]{1203, 594};

    public static void dos(Dos num) {
        if (MainScreen.autoClickService == null) {
            CommandResult result;
            String str = "";
            switch (num) {
                case DaKaiRenWu:
                    str = dakaiRenWu;
                    Lag.i("执行打开任务");
                    break;
                case GuanBiRenWu:
                    str = guanBiRenWu;
                    Lag.i("执行关闭任务");
                    break;
                case DakaiRenWuBan:
                    str = dakaiRenWuBan;
                    Lag.i("执行打开任务面板");
                    break;
                case GuanBiDuiWuBan:
                    str = guanBiDuiWuBan;
                    Lag.i("执行打开队伍面板");
                    break;
                case RenWuGunDongDingBu:
                    str = renWuGunDongDingBu;
                    Lag.i("执行滑动到面板顶部");
                    break;
                case DaKaiHuoDong:
                    str = daKaiHuoDong;
                    Lag.i("打开活动面板");
                    break;
                case DianJiYiTiaoLong:
                    str = dianJiYiTiaoLong;
                    Lag.i("点击一条龙");
                    break;
                case CanJiaQuMo:
                    str = canJiaQuMo;
                    Lag.i("面板参加驱魔");
                    break;
                case QuMoRenWuCanJia:
                    str = quMoRenWuCanJia;
                    Lag.i("参加驱魔");
                    break;
                case QuMoRenWuCanJiaZiDongPiPei:
                    str = quMoRenWuCanJiaZiDongPiPei;
                    Lag.i("参加驱魔匹配");
                    break;
                case QuMoChouJiang:
                    str = quMoChouJiang;
                    Lag.i("驱魔抽奖");
                    break;
                case QuMoJiXu:
                    str = quMoJiXu;
                    Lag.i("继续驱魔");
                    break;
            }
            result = Shell.SU.run(str);
            if (result != null && result.isSuccessful()) {
                Lag.i("执行成功" + result.getStdout());
            }
        } else {
            int[] str = new int[]{0, 0};
            switch (num) {
                case DaKaiRenWu:
                    str = dakaiRenWuSuZu;
                    Lag.i("执行打开任务");
                    break;
                case GuanBiRenWu:
                    str = guanBiRenWuSuZu;
                    Lag.i("执行关闭任务");
                    break;
                case DakaiRenWuBan:
                    str = dakaiRenWuBanSuZu;
                    Lag.i("执行打开任务面板");
                    break;
                case GuanBiDuiWuBan:
                    str = guanBiDuiWuBanSuZu;
                    Lag.i("执行打开队伍面板");
                    break;
                case RenWuGunDongDingBu:
                    str = renWuGunDongDingBuSuZu;
                    Lag.i("执行滑动到面板顶部");
                    break;
                case DaKaiHuoDong:
                    str = daKaiHuoDongSuZu;
                    Lag.i("打开活动面板");
                    break;
                case DianJiYiTiaoLong:
                    str = dianJiYiTiaoLongSuZu;
                    Lag.i("点击一条龙");
                    break;
                case CanJiaQuMo:
                    str = canJiaQuMoSuZu;
                    Lag.i("面板参加驱魔");
                    break;
                case QuMoRenWuCanJia:
                    str = quMoRenWuCanJiaSuZu;
                    Lag.i("参加驱魔");
                    break;
                case QuMoRenWuCanJiaZiDongPiPei:
                    str = quMoRenWuCanJiaZiDongPiPeiSuZu;
                    Lag.i("参加驱魔匹配");
                    break;
                case QuMoChouJiang:
                    str = quMoChouJiangSuZu;
                    Lag.i("驱魔抽奖");
                    break;
                case QuMoJiXu:
                    str = quMoJiXuSuZu;
                    Lag.i("继续驱魔");
                    break;
            }
            MainScreen.autoClickService.click(str[0], str[1], 100, 50);
            Lag.i("免root执行成功");
        }
    }
}
