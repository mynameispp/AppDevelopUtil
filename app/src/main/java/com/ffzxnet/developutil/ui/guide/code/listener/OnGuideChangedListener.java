package com.ffzxnet.developutil.ui.guide.code.listener;


import com.ffzxnet.developutil.ui.guide.code.core.Controller;

public interface OnGuideChangedListener {
    /**
     * 当引导层显示时回调
     *
     * @param controller
     */
    void onShowed(Controller controller);

    /**
     * 当引导层消失时回调
     *
     * @param controller
     */
    void onRemoved(Controller controller);
}