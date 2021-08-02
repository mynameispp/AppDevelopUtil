package com.ffzxnet.developutil.ui.login;

import com.ffzxnet.developutil.base.mvp.BasePresenter;
import com.ffzxnet.developutil.base.mvp.BaseView;
import com.ffzxnet.developutil.bean.LoginRequestBean;

public interface LoginContract {
    interface Presenter extends BasePresenter {
        void login(LoginRequestBean requestBean);
    }

    interface View extends BaseView<Presenter> {
        void login(boolean success);
    }
}
