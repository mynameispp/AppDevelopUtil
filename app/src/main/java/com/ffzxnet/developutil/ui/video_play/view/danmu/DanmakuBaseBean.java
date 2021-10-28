package com.ffzxnet.developutil.ui.video_play.view.danmu;

import java.io.Serializable;

//弹幕基础内
public class DanmakuBaseBean implements Serializable {
    private String content;//弹幕内容
    private int sendByVideoTime;//发送弹幕时的视频播放时间
    private boolean isSelf;//是否是自己发的

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSendByVideoTime() {
        return sendByVideoTime;
    }

    public void setSendByVideoTime(int sendByVideoTime) {
        this.sendByVideoTime = sendByVideoTime;
    }
}
