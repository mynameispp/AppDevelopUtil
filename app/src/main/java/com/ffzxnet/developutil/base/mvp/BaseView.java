package com.ffzxnet.developutil.base.mvp;

/**
 * 创建者： feifan.pi 在 2017/3/6.
 */

public interface BaseView<T> extends BaseActivityView{
    void initView();

    void setPresenter(T presenter);

    boolean isActive();
}
