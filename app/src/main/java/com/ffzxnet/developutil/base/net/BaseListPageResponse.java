package com.ffzxnet.developutil.base.net;

/**
 * 分页数据第二层
 * Created by shiyaofang on 2017/7/18.
 */

public class BaseListPageResponse<T> {

    private T data;
    private int total;
    private String order;
    private String sort;


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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
