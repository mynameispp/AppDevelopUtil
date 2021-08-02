package com.ffzxnet.developutil.utils.ui;

import android.content.Context;
import android.text.TextUtils;


/**
 * 创建者： Pi 在 2018/9/3.
 * 注释：
 */
public class LoadingUtil {
    private static LoadingDialog loadingDialog;

    public static void init(Context context) {
        if (null == loadingDialog) {
            loadingDialog = new LoadingDialog(context);
        }
    }

    public static void destory() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            loadingDialog = null;
        }
    }

    public static void showLoadingDialog(boolean b) {
        if (loadingDialog == null) {
            return;
        }
        if (b) {
            loadingDialog.showDialog("");
        } else {
            loadingDialog.closeDialog();
        }
    }

    //进度弹窗 带自定义提示文字
    public static void showLoadingDialog(boolean b, String msg) {
        if (loadingDialog == null) {
            return;
        }
        if (b) {
            if (!TextUtils.isEmpty(msg)) {
                loadingDialog.showDialog(msg);
            } else {
                loadingDialog.showDialog();
            }
        } else {
            loadingDialog.closeDialog();
        }
    }

    public static boolean isShowing() {
        if (loadingDialog == null) {
            return false;
        }
        return loadingDialog.isShowing();
    }
}
