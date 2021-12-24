package com.ffzxnet.developutil.ui.guide.code.listener;

import android.view.View;

import com.ffzxnet.developutil.ui.guide.code.core.Controller;

public interface OnLayoutInflatedListener {

    /**
     * @param view       {@link com.ffzxnet.developutil.ui.guide.code.model.GuidePage#setLayoutRes(int, int...)}方法传入的layoutRes填充后的view
     * @param controller {@link Controller}
     */
    void onLayoutInflated(View view, Controller controller);
}
