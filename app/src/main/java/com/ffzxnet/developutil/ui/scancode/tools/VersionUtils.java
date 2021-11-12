package com.ffzxnet.developutil.ui.scancode.tools;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

public class VersionUtils {

    /**
     * 判断当前SDK版本是否是Q版本以上
     * @return
     */
    public static boolean isTargetQ(Context context) {
        return Build.VERSION.SDK_INT >= 29 && context.getApplicationInfo().targetSdkVersion >= 29;
    }

    /**
     * 检查app的运行模式
     * @return true 为作用域模式；false为兼容模式
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean isExternalStorageLegacy() {
        return Environment.isExternalStorageLegacy();
    }
}
