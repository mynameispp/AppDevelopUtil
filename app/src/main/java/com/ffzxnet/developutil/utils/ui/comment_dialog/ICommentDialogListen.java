package com.ffzxnet.developutil.utils.ui.comment_dialog;

/**
 * 创建者： feifan.pi 在 2017/7/20.
 */

public interface ICommentDialogListen {
    //用户发送的内容
    void commentDialogSendMsg(String msg);

    void onDialogDismiss();
}
