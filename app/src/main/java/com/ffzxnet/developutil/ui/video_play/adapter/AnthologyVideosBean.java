package com.ffzxnet.developutil.ui.video_play.adapter;

import com.ffzxnet.developutil.base.net.BaseResponse;

public class AnthologyVideosBean extends BaseResponse {
    private String anthologyTitle;//第几集

    public String getAnthologyTitle() {
        return anthologyTitle;
    }

    public void setAnthologyTitle(String anthologyTitle) {
        this.anthologyTitle = anthologyTitle;
    }
}
