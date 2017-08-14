package com.ffzxnet.developutil.utils.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.ffzxnet.countrymeet.R;


/**
 * 进度弹窗
 */
public class LoadingDialog extends ProgressDialog {
    private TextView msgTv;

    public LoadingDialog(Context context) {
        super(context);
    }
//
//    public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
//        super(context, themeResId);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.loading_layout);
        this.setCanceledOnTouchOutside(false);

//        Window dialogWindow = getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.CENTER);
//        dialogWindow.setAttributes(lp);

//        FrescoUti.load(("res:///" + R.mipmap.loading), (SimpleDraweeView) findViewById(R.id.loading_img), 22, 22);
        msgTv = (TextView) findViewById(R.id.loading_msg);
        setMessage("加载数据中...");
    }

//    public void setMessage(String msg) {
//        if (null != msgTv && !TextUtils.isEmpty(msg)) {
//            msgTv.setText(msg);
//        }
//    }

    /**
     * 显示  对外使用
     */
    public void ShowDialog() {
        if (!isShowing()) {
            show();
        }
    }

    /**
     * 隐藏关闭   对外使用
     */
    public void closeDialog() {
        if (isShowing()) {
            dismiss();
        }
    }

    public void showDailog(String msg){
        setMessage(msg);
        if (!isShowing()) {
            show();
        }
    }


}
