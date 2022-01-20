package com.ffzxnet.developutil.ui.video_play.view;

public interface MyVodControlClickListen {
    //下一集
    void onNextVideoClick();

    //弹幕开关
    void onSwitchDanmaku(boolean onOff);

    //发送弹幕
    void onSendDanmaku(String content);
}
