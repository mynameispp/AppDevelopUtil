package com.ffzxnet.developutil.ui.custom_view;

import com.ffzxnet.developutil.base.net.BaseResponse;

public class CustomViewAdapterBean extends BaseResponse {
    private int type;
    private String title;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
