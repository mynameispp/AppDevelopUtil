package com.ffzxnet.developutil.base.mvp;


import com.trello.rxlifecycle4.LifecycleProvider;
import com.trello.rxlifecycle4.LifecycleTransformer;
import com.trello.rxlifecycle4.android.ActivityEvent;
import com.trello.rxlifecycle4.android.FragmentEvent;

/**
 * 给网络请求绑定生命周期
 */
public abstract class BasePresenterParent {
    public LifecycleProvider lifecycleProvider;

    public BasePresenterParent(LifecycleProvider lifecycleProvider) {
        this.lifecycleProvider = lifecycleProvider;
    }

    public LifecycleTransformer getLifecycleTransformerByStopToActivity() {
        return lifecycleProvider.bindUntilEvent(ActivityEvent.STOP);
    }

    public LifecycleTransformer getLifecycleTransformerByStopToFragment() {
        return lifecycleProvider.bindUntilEvent(FragmentEvent.STOP);
    }

    public LifecycleTransformer getLifecycleTransformerByDestroyToActivity() {
        return lifecycleProvider.bindUntilEvent(ActivityEvent.DESTROY);
    }

    public LifecycleTransformer getLifecycleTransformerByDestroyToFragment() {
        return lifecycleProvider.bindUntilEvent(FragmentEvent.DESTROY);
    }
}
