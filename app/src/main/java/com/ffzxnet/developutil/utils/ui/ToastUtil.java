package com.ffzxnet.developutil.utils.ui;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.ffzxnet.countrymeet.application.MyApplication;


/**
 * 中间打印
 */
public class ToastUtil {
    private static Toast toast;

    private static void showToast(Context context, String msg, int gravity) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        if (gravity > -1) {
            toast.setGravity(gravity, 0, 0);
        }
        toast.show();
    }

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
     * @param gravity Toast显示的位置
     */
    public static void showToastShort(String msg, int gravity) {
        showToast(MyApplication.getContext(), msg, gravity);
    }


}