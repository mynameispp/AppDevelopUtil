package com.ffzxnet.developutil.utils.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;


/**
 * 进度弹窗
 */
public class LoadingDialog extends ProgressDialog {
    private TextView msgTv;
    private String msg;

    public LoadingDialog(Context context) {
        this(context, 0);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, R.style.DialogLoading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        this.setCanceledOnTouchOutside(false);

        msgTv = findViewById(R.id.loading_msg);

        if (TextUtils.isEmpty(msg)) {
            msgTv.setText(MyApplication.getStringByResId(R.string.loading_msg));
        } else {
            msgTv.setText(msg);
        }
    }


    /**
     * 显示  对外使用
     */
    public void showDialog() {
        if (!isShowing()) {
            show();
        }
    }

    /**
     * 隐藏关闭   对外使用
     */
    public void closeDialog() {
        dismiss();
    }

    public void showDialog(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            this.msg = msg;
        } else {
            this.msg = MyApplication.getStringByResId(R.string.loading_msg);
        }
        if (!isShowing()) {
            show();
        }
        if (null != msgTv) {
            msgTv.setText(this.msg);
        }
    }


}
