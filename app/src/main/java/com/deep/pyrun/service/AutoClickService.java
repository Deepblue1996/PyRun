package com.deep.pyrun.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.accessibilityservice.GestureDescription.Builder;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.deep.pyrun.view.MainScreen;
import com.intelligence.dpwork.util.DisplayUtil;
import com.intelligence.dpwork.util.Lag;

/**
 * Class -
 * <p>
 * Created by Deepblue on 2019/7/1 0001.
 */

public class AutoClickService extends AccessibilityService {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Lag.i("启动无障碍成功");
        MainScreen.autoClickService = this;
    }

    public final void click(int i, int i2, long j, long j2) {
        if (Build.VERSION.SDK_INT >= 24) {
            if (i == 2160) {
                i--;
            }
            if (i2 == 1080) {
                i2--;
            }
            Path path = new Path();
            path.moveTo((float) i, (float) i2);
            dispatchGesture(new Builder().addStroke(new GestureDescription.StrokeDescription(path, j, j2)).build(), null, null);
        }
    }

    private final void clickAtPosition(int i, int i2, AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo != null) {
            if (accessibilityNodeInfo.getChildCount() == 0) {
                Rect rect = new Rect();
                accessibilityNodeInfo.getBoundsInScreen(rect);
                if (rect.contains(i, i2)) {
                    accessibilityNodeInfo.performAction(16);
                    String stringBuilder = "1º - Node Information: " +
                            accessibilityNodeInfo.toString();
                    System.out.println(stringBuilder);
                }
            } else {
                Rect rect2 = new Rect();
                accessibilityNodeInfo.getBoundsInScreen(rect2);
                if (rect2.contains(i, i2)) {
                    accessibilityNodeInfo.performAction(16);
                    String stringBuilder2 = "2º - Node Information: " +
                            accessibilityNodeInfo.toString();
                    System.out.println(stringBuilder2);
                }
                int childCount = accessibilityNodeInfo.getChildCount();
                for (int i3 = 0; i3 < childCount; i3++) {
                    clickAtPosition(i, i2, accessibilityNodeInfo.getChild(i3));
                }
            }
        }
    }

}
