package com.ffzxnet.developutil.utils.ui;

import android.content.Context;
import android.text.TextUtils;

import java.lang.ref.SoftReference;


/**
 * 创建者： Pi 在 2018/9/3.
 * 注释：
 */
public class LoadingUtil {
    private static SoftReference<LoadingDialog> loadingDialog;

    public static void init(Context context) {
        if (null == loadingDialog) {
            SoftReference<Context> softReference = new SoftReference<>(context);
            loadingDialog = new SoftReference<>(new LoadingDialog(softReference.get()));
        }
    }

    public static void destory() {
        if (loadingDialog != null) {
            if (loadingDialog.get().isShowing()) {
                loadingDialog.get().dismiss();
            }
            loadingDialog = null;
        }
    }

    public static void showLoadingDialog(boolean b) {
        if (loadingDialog == null) {
            return;
        }
        if (b) {
            loadingDialog.get().showDialog("");
        } else {
            loadingDialog.get().closeDialog();
        }
    }

    //进度弹窗 带自定义提示文字
    public static void showLoadingDialog(boolean b, String msg) {
        if (loadingDialog == null) {
            return;
        }
        if (b) {
            if (!TextUtils.isEmpty(msg)) {
                loadingDialog.get().showDialog(msg);
            } else {
                loadingDialog.get().showDialog();
            }
        } else {
            loadingDialog.get().closeDialog();
        }
    }

    public static boolean isShowing() {
        if (loadingDialog == null) {
            return false;
        }
        return loadingDialog.get().isShowing();
    }
}
