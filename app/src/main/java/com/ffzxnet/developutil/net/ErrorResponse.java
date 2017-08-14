package com.ffzxnet.developutil.net;


import android.text.TextUtils;

import com.ffzxnet.countrymeet.R;
import com.ffzxnet.countrymeet.application.MyApplication;
import com.ffzxnet.countrymeet.base.net.BaseResponse;


/**
 * 创建者： feifan.pi 在 2017/3/22.
 */

public class ErrorResponse extends BaseResponse {
    /**
     * -1，-2系统返回错误
     * -3,其他异常
     */
    private int code;
    private String message;//异常信息
    private String url;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return TextUtils.isEmpty(message) ? MyApplication.getStringByResId(R.string.net_error_for_link_exception) : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
