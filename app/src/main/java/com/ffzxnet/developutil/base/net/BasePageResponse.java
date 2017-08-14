package com.ffzxnet.developutil.base.net;

/**
 * 分页数据第一层
 * Created by shiyaofang on 2017/7/18.
 */

public class BasePageResponse<T> {

    private T pageInfo;

    public T getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(T pageInfo) {
        this.pageInfo = pageInfo;
    }
}
