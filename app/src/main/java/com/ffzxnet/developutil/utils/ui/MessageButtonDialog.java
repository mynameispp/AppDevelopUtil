package com.ffzxnet.developutil.utils.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ffzxnet.countrymeet.R;
import com.ffzxnet.countrymeet.application.MyApplication;


/**
 * 消息弹框 有按钮
 */
public class MessageButtonDialog extends Dialog implements
        View.OnClickListener {
    private TextView title_tx, message_tx, btn_ok_tx, btn_no_tx;
    private String title, message, btn_ok = "", btn_no = "";
    private MyDialogOnClick MyDialogOnClick;// 点击回调
    private boolean no_ok = false;// 显示还是隐藏no按钮
    private LinearLayout layout;
    //    private Context context;
    // 标题颜色，内容颜色
    private int title_color, message_color;
    // 标题字大小，内容字大小
    private float title_size, message_size;
    /**
     * 是否有html样式
     */
    private boolean yesHtml = false;

    public MessageButtonDialog(Context context) {
        this(context, -1);
    }

    public MessageButtonDialog(Context context, int theme) {
        this(context, "", "", false, null, theme);
    }

    /**
     * @param context
     * @param title           标题
     * @param message         提示内容
     * @param MyDialogOnClick 点击事件
     * @param no_ok           显示还是隐藏no按钮 true不显示
     */
    public MessageButtonDialog(Context context, String title, String message,
                               Boolean no_ok, MyDialogOnClick MyDialogOnClick) {
        this(context, title, message, no_ok, MyDialogOnClick, R.style.message_button_dialog);
    }

    /**
     * @param context
     * @param title           标题
     * @param message         提示内容
     * @param MyDialogOnClick 点击事件
     * @param theme           dialog样式 R.style.message_dialog
     * @param no_ok           显示还是隐藏no按钮 true不显示
     */
    public MessageButtonDialog(Context context, String title, String message,
                               Boolean no_ok, MyDialogOnClick MyDialogOnClick, int theme) {
        this(context, title, message, no_ok, MyDialogOnClick, theme, false);
    }

    /**
     * @param context
     * @param title           标题
     * @param message         提示内容
     * @param MyDialogOnClick 点击事件
     * @param theme           dialog样式 R.style.message_dialog
     * @param no_ok           显示还是隐藏no按钮 true不显示
     */
    public MessageButtonDialog(Context context, String title, String message,
                               Boolean no_ok, MyDialogOnClick MyDialogOnClick, int theme,
                               boolean yesHtml) {
        super(context, theme);
        this.title = title;
        this.message = message;
        this.MyDialogOnClick = MyDialogOnClick;
        this.no_ok = no_ok;
        this.yesHtml = yesHtml;
    }

    /**
     * @param context
     * @param title           标题
     * @param message         提示内容
     * @param btn_ok          确认按钮的名称
     * @param btn_no          取消按钮的名称
     * @param no_ok           显示还是隐藏no按钮 true不显示
     * @param MyDialogOnClick 点击事件
     */
    public MessageButtonDialog(Context context, String title, String message,
                               String btn_ok, String btn_no, Boolean no_ok, MyDialogOnClick MyDialogOnClick) {
        this(context, title, message, btn_ok, btn_no, no_ok, MyDialogOnClick, R.style.message_button_dialog);
    }

    /**
     * @param context
     * @param title           标题
     * @param message         提示内容
     * @param btn_ok          确认按钮的名称
     * @param btn_no          取消按钮的名称
     * @param MyDialogOnClick 点击事件
     * @param theme           dialog样式 R.style.message_dialog
     */
    public MessageButtonDialog(Context context, String title, String message,
                               String btn_ok, String btn_no, Boolean no_ok, MyDialogOnClick MyDialogOnClick, int theme) {
        this(context, title, message, btn_ok, btn_no, no_ok, MyDialogOnClick, theme, false);
    }

    /**
     * @param context
     * @param title           标题
     * @param message         提示内容
     * @param btn_ok          确认按钮的名称
     * @param btn_no          取消按钮的名称
     * @param MyDialogOnClick 点击事件
     * @param theme           dialog样式 R.style.message_dialog
     */
    public MessageButtonDialog(Context context, String title, String message,
                               String btn_ok, String btn_no, Boolean no_ok, MyDialogOnClick MyDialogOnClick,
                               int theme, boolean yesHtml) {
        super(context, theme);
        this.title = title;
        this.message = message;
        this.btn_ok = btn_ok;
        this.btn_no = btn_no;
        this.MyDialogOnClick = MyDialogOnClick;
        this.yesHtml = yesHtml;
    }

    public void setBtn_ok_tx(String btn_ok) {
        this.btn_ok = btn_ok;
    }

    public void setBtn_no_tx(String btn_no) {
        this.btn_no = btn_no;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_message_button);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        layout = (LinearLayout) findViewById(R.id.mydialog_lay);
        title_tx = (TextView) findViewById(R.id.mydialog_title);
        message_tx = (TextView) findViewById(R.id.mydialog_message);
        btn_no_tx = (TextView) findViewById(R.id.mydialog_no);
        btn_ok_tx = (TextView) findViewById(R.id.mydialog_ok);

        int windWidth = MyApplication.Screen_Width;
        int layWidth = (int) (windWidth * 0.8);
        LayoutParams params = new LayoutParams(layWidth,
                LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);

        if (no_ok) {
            btn_no_tx.setVisibility(View.GONE);
            //间隔线
            ((ViewGroup) btn_no_tx.getParent()).getChildAt(1).setVisibility(View.GONE);
            btn_ok_tx.setBackgroundResource(R.drawable.message_dialog_one_btn_bg_color);
            btn_ok_tx.setTextColor(ContextCompat.getColorStateList(MyApplication.getContext(), R.color.message_dialog_btn_txt_color));
        }

        if (title_color > 0) {
            title_tx.setTextColor(ContextCompat.getColor(MyApplication.getContext(), title_color));
        }
        if (title_size > 0) {
            title_tx.setTextSize(title_size);
        }
        if (message_color > 0) {
            message_tx.setTextColor(ContextCompat.getColor(MyApplication.getContext(),
                    message_color));
        }
        if (message_size > 0) {
            message_tx.setTextSize(message_size);
        }

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = layout.getLayoutParams().width; // 宽度
        lp.height = layout.getLayoutParams().height; // 高度
        // lp.alpha = 0.7f; // 透明度
        dialogWindow.setAttributes(lp);
        if (TextUtils.isEmpty(title)) {
            //没有标题
            title_tx.setVisibility(View.GONE);
            //设置内容边距
            LayoutParams params1 = (LayoutParams) message_tx.getLayoutParams();
            params1.setMargins(20, 44, 20, 44);
            message_tx.requestLayout();
        } else {
            title_tx.setText(title);
        }
        if (yesHtml) {
            message_tx.setText(Html.fromHtml(message));
        } else {
            message_tx.setText(message);
        }
        if (!"".equals(btn_ok)) {
            btn_ok_tx.setText(btn_ok);
        }
        if (!"".equals(btn_no)) {
            btn_no_tx.setText(btn_no);
        }
        btn_ok_tx.setOnClickListener(this);
        btn_no_tx.setOnClickListener(this);
    }

    /**
     * 设置透明度
     */
    public void setAlpha(float alpha) {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.alpha = alpha; // 透明度
        dialogWindow.setAttributes(lp);
    }

    /**
     * 设置title字体大小
     */
    public void setTitleSize(float size) {
        title_size = size;
    }

    /**
     * 设置title字体颜色
     *
     * @param color R.color.*** 如果报红线 在调用的方法前加上 @SuppressLint("ResourceAsColor")
     */
    public void setTitleColor(int color) {
        title_color = color;
    }

    /**
     * 设置message字体大小
     */
    public void setMessageSize(float size) {
        message_size = size;
    }

    /**
     * 设置message字体颜色
     *
     * @param color R.color.*** 如果报红线 在调用的方法前加上 @SuppressLint("ResourceAsColor")
     */
    public void setMessageColor(int color) {
        message_color = color;
    }

    /**
     * 设置message
     *
     * @param msg
     */
    public void setMessage(String msg) {
        if (null != message_tx) {
            message_tx.setText(msg);
        }
    }

    /**
     * 按钮回调监听
     */
    public interface MyDialogOnClick {
        public void btnOk(Dialog dialog);

        public void btnNo(Dialog dialog);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.mydialog_ok:
                if (MyDialogOnClick != null) {
                    MyDialogOnClick.btnOk(this);
                    dismiss();
                }
                break;
            case R.id.mydialog_no:
                if (MyDialogOnClick != null) {
                    MyDialogOnClick.btnNo(this);
                    dismiss();
                }
                break;
            default:
                break;
        }
    }

    private boolean isPay = false;

    /**
     * 按返回键无效
     */
    public void setCanceledOnBack(boolean ispayb) {
        isPay = ispayb;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isPay) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 設置圖片
     */
    public void setImage() {
        setImageB = true;

    }

    boolean setImageB = false;
}
