package com.deep.pyrun.util;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.intelligence.dpwork.util.Lag;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 获取当前应用名
 * Created by Administrator on 2019/6/28 0028.
 */

public class PackageName {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static int isRunApp(Context context, String packName) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, System.currentTimeMillis() - 2000, System.currentTimeMillis());
        for (int i = usageStatsList.size() - 1; i > 0; i--) {
            UsageStats key = usageStatsList.get(i);
            if (key.getPackageName().equals(packName)) {
                try {
                    Field mLastEvent = key.getClass().getField("mLastEvent");
                    int state = (int) mLastEvent.get(key);
                    return state;
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }

    public static void checkUsageStateAccessPermission(Context context) {
        if (!checkAppUsagePermission(context)) {
            requestAppUsagePermission(context);
        }
    }

    public static boolean checkAppUsagePermission(Context context) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        if (usageStatsManager == null) {
            return false;
        }
        long currentTime = System.currentTimeMillis();
        // try to get app usage state in last 1 min
        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 60 * 1000, currentTime);
        if (stats.size() == 0) {
            return false;
        }

        return true;
    }

    public static void requestAppUsagePermission(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Lag.i("Start usage access settings activity fail!");
        }
    }
}
