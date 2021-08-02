package com.ffzxnet.developutil.ui.main;

import android.os.Bundle;

import com.ffzxnet.developutil.base.mvp.BasePresenter;
import com.ffzxnet.developutil.base.mvp.BaseView;

import androidx.fragment.app.Fragment;

public interface MainContract {
    interface Presenter extends BasePresenter {
        void showFragment(int flag, Bundle bundle);
        void testApi();
    }

    interface View extends BaseView<Presenter> {
        void showFragment(Fragment showFragment, Fragment hideFragment);
    }
}
