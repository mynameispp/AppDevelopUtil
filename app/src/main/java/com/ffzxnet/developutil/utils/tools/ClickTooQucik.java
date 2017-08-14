package com.ffzxnet.developutil.utils.tools;

/**
 * 防止用户快速点击
 * 创建者： feifan.pi 在 2017/5/16.
 */

public class ClickTooQucik {
    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
