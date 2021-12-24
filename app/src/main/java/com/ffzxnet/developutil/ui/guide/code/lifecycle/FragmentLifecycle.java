package com.ffzxnet.developutil.ui.guide.code.lifecycle;

public interface FragmentLifecycle {
    void onStart();

    void onStop();

    void onDestroyView();

    void onDestroy();
}