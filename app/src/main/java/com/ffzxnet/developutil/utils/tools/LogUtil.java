package com.ffzxnet.developutil.utils.tools;

import android.util.Log;

import com.ffzxnet.developutil.BuildConfig;


/**
 * Created by feifan.pi on 2016/3/24.
 */
public class LogUtil {
    //    private static boolean SHOW_LOG = !ApiImpl.BASE_URL.contains("app-client.ffzxnet.com");
    private static boolean SHOW_LOG = BuildConfig.DEBUG;

    public static void e(String tag, String msg) {
        if (SHOW_LOG) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (SHOW_LOG) {
            Log.w(tag, msg);
        }
    }

    /**
     * 如果不是正式地址--显示Log
     *
     * @param tag
     * @param msg
     */
    public static void showLog(String tag, String msg) {
        if (SHOW_LOG) {
            Log.e(tag, msg);
        }
    }
}
