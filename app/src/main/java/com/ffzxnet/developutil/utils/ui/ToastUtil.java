package com.ffzxnet.developutil.utils.ui;

import android.view.Gravity;
import android.widget.Toast;

import com.ffzxnet.developutil.application.MyApplication;


/**
 * 中间打印
 */
public class ToastUtil {
    private static Toast toast;

    public static void cancleToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    /**
     * Toast
     * 对外调用
     *
     * @param msg 信息
     */
    public static void showToastShort(String msg) {
        showToastShort(msg, Gravity.CENTER);
    }

    /**
     * Toast
     * 对外调用
     *
     * @param msg     信息
     * @param gravity Toast显示的位置 Gravity.CENTER
     */
    public static void showToastShort(String msg, int gravity) {
        showToastShortBase(msg, gravity);
    }

    /**
     * Toast
     * 对外调用
     *
     * @param msg 信息
     */
    public static void showToastLong(String msg) {
        showToastLong(msg, Gravity.CENTER);
    }

    /**
     * Toast
     * 对外调用
     *
     * @param msg     信息
     * @param gravity Toast显示的位置
     */
    public static void showToastLong(String msg, int gravity) {
        showToastLongBase(msg, gravity);
    }

    private static void showToastShortBase(String msg, int gravity) {
        cancleToast();
        toast = Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT);
        if (gravity > -1) {
            toast.setGravity(gravity, 0, 0);
        }
        toast.show();
    }

    private static void showToastLongBase(String msg, int gravity) {
        cancleToast();
        toast = Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_LONG);
        if (gravity > -1) {
            toast.setGravity(gravity, 0, 0);
        }
        toast.show();
    }
}