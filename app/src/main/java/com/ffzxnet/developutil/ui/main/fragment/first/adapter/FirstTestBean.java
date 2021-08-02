package com.ffzxnet.developutil.ui.main.fragment.first.adapter;

import com.ffzxnet.developutil.base.net.BaseResponse;

public class FirstTestBean extends BaseResponse {
    private String title;
    private String content;
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
