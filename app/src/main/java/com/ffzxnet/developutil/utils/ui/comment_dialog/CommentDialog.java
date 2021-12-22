package com.ffzxnet.developutil.utils.ui.comment_dialog;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.utils.tools.TxtSpannableUtil;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

/**
 * 创建者： feifan.pi 在 2017/6/28.
 * 留言输入弹窗
 */

public class CommentDialog extends Dialog implements View.OnClickListener, TextWatcher {
    private EditText input;
    private ImageView emoji;
    private Button sendMsg;
    private LinearLayout emojiLay;
    //获取用户输入的内容
    ICommentDialogListen iCommentDialogListen;
    private TextView mTvRemind;


    public CommentDialog(@NonNull Context context, @NonNull ICommentDialogListen iCommentDialogListen) {
        this(context, R.style.DialogStyle);
        this.iCommentDialogListen = iCommentDialogListen;
    }

    public CommentDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected CommentDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setWindowAnimations(R.style.DialogAnimation);
        dialogWindow.setGravity(Gravity.BOTTOM);
        lp.width = MyConstans.Screen_Width; // 宽度
//        lp.height = (int) (ScreenUtils.getScreenHeight(MyApplication.getContext()) * 0.5); // 高度
        // lp.alpha = 0.7f; // 透明度
        dialogWindow.setAttributes(lp);

        initView();
    }

    private void initView() {
        emoji = (ImageView) findViewById(R.id.comment_dialog_emoji);
        sendMsg = (Button) findViewById(R.id.comment_dialog_send_msg);
        input = (EditText) findViewById(R.id.comment_dialog_input_ed);
        emojiLay = (LinearLayout) findViewById(R.id.comment_dialog_emoji_lay);
        mTvRemind = (TextView) findViewById(R.id.tv_remind_comment_length);

        input.addTextChangedListener(this);

        //弹出自己的表情列表
//        emoji.setOnClickListener(this);
        sendMsg.setOnClickListener(this);

        emojiLay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    emojiLay.setVisibility(View.GONE);
                }
            }
        });

        sendMsg.setEnabled(false);
        sendMsg.addTextChangedListener(this);
    }

    /**
     * 显示软键盘
     */
    private void showSoftInput() {
        input.setFocusable(true);
        input.setFocusableInTouchMode(true);
        input.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 500);
    }

    /**
     * 显示对话框
     *
     * @param hintMsg hint信息
     */
    public void showDilog(String hintMsg) {
        show();
        showSoftInput();
        //设置输入框的hint信息
        if (TextUtils.isEmpty(hintMsg)) {
            input.setHint("请输入内容");
        } else {
            input.setHint(hintMsg);
        }
    }

    public void hideEmojiLay() {
        emojiLay.setVisibility(View.GONE);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        emojiLay.setVisibility(View.GONE);
        input.setText("");
        if (iCommentDialogListen != null) {
            iCommentDialogListen.onDialogDismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_dialog_emoji:
                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                //弹出表情
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        emojiLay.post(new Runnable() {
                            @Override
                            public void run() {
                                emojiLay.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                }, 500);

                break;
            case R.id.comment_dialog_send_msg:
                //发送
                iCommentDialogListen.commentDialogSendMsg(input.getText().toString());
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < 1) {
            if (sendMsg.isEnabled()) {
                mTvRemind.setText("0/100");
                sendMsg.setEnabled(false);
//                sendMsg.setBackground(MyApplication.getDrawableByResId(R.drawable.bg_grayee_color_radius_5));
                sendMsg.setTextColor(MyApplication.getColorByResId(R.color.gray_CC));
            }
        } else {
            if (!sendMsg.isEnabled()) {
                sendMsg.setEnabled(true);
//                sendMsg.setBackground(MyApplication.getDrawableByResId(R.drawable.bg_main_color_radius_5));
                sendMsg.setTextColor(MyApplication.getColorByResId(R.color.white));
            }

            int len = s.length();
            if (len > 100) {
                String str = s.toString();
                //截取新字符串
                String newStr = str.substring(0, len - count);
                input.setText(newStr);
                Spannable editable = input.getText();
                //新字符串的长度
                int selEndIndex = editable.length();
                //光标所在的位置
                Selection.setSelection(editable, selEndIndex);
            } else {
                String lengthMsg = len + "/" + 100;
                if (len == 100) {
                    mTvRemind.setText(TxtSpannableUtil.highlight(lengthMsg, R.color.red, 0, lengthMsg.indexOf("/"), 0));
                } else {
                    mTvRemind.setText(TxtSpannableUtil.highlight(lengthMsg, R.color.gray_CC, 0, lengthMsg.length(), 0));
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
