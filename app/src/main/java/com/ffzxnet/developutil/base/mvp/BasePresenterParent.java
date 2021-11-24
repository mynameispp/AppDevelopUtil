package com.ffzxnet.developutil.base.mvp;


import com.trello.rxlifecycle4.LifecycleProvider;
import com.trello.rxlifecycle4.LifecycleTransformer;
import com.trello.rxlifecycle4.android.ActivityEvent;
import com.trello.rxlifecycle4.android.FragmentEvent;

/**
 * 给网络请求绑定生命周期
 */
public abstract class BasePresenterParent {
    private LifecycleProvider lifecycleProvider;

    public BasePresenterParent(LifecycleProvider lifecycleProvider) {
        this.lifecycleProvider = lifecycleProvider;
    }

    protected LifecycleTransformer getLifecycleTransformerByStopToActivity() {
        return lifecycleProvider.bindUntilEvent(ActivityEvent.STOP);
    }

    protected LifecycleTransformer getLifecycleTransformerByStopToFragment() {
        return lifecycleProvider.bindUntilEvent(FragmentEvent.STOP);
    }

    protected LifecycleTransformer getLifecycleTransformerByDestroyToActivity() {
        return lifecycleProvider.bindUntilEvent(ActivityEvent.DESTROY);
    }

    protected LifecycleTransformer getLifecycleTransformerByDestroyToFragment() {
        return lifecycleProvider.bindUntilEvent(FragmentEvent.DESTROY);
    }
}
