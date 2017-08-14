package com.ffzxnet.developutil.base.net;

/**
 * 创建者： feifan.pi 在 2017/3/6.
 */

public class BaseApiResultData<T> extends BaseResponse {
    /**
     * 返回标志
     */
    private int code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 数据总量
     */
    private String recordsTotal;
    /**
     * 数据总量
     */
    private int total;


    /**
     * 信息
     */
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(String recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
