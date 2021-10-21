package com.ffzxnet.developutil.bean;

import okhttp3.MultipartBody;

/**
 * 创建者： Pi 在 2018/8/2.
 * 注释：上传文件
 */

public class UpLoadRequest {
    private MultipartBody.Part fileUrl;

    public UpLoadRequest(MultipartBody.Part fileUrl) {
        this.fileUrl = fileUrl;
    }

    public MultipartBody.Part getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(MultipartBody.Part fileUrl) {
        this.fileUrl = fileUrl;
    }
}
