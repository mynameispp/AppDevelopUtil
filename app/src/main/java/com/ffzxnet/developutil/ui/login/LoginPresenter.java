package com.ffzxnet.developutil.ui.login;

import com.ffzxnet.developutil.base.mvp.BasePresenterParent;
import com.ffzxnet.developutil.base.net.BaseApiResultData;
import com.ffzxnet.developutil.bean.LoginRequestBean;
import com.ffzxnet.developutil.net.ApiImp;
import com.ffzxnet.developutil.net.ErrorResponse;
import com.ffzxnet.developutil.net.IApiSubscriberCallBack;
import com.ffzxnet.developutil.utils.ui.ToastUtil;
import com.trello.rxlifecycle4.LifecycleProvider;

public class LoginPresenter extends BasePresenterParent implements LoginContract.Presenter {
    private LoginContract.View mView;

    public LoginPresenter(LifecycleProvider lifecycleProvider, LoginContract.View mView) {
        super(lifecycleProvider);
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void login(LoginRequestBean requestBean) {
        ApiImp.getInstance().login(requestBean, getLifecycleTransformerByStopToActivity(), new IApiSubscriberCallBack<BaseApiResultData<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getMessage());
            }

            @Override
            public void onNext(BaseApiResultData<String> data) {
                if (data.getData() != null) {
                    mView.login(true);
                } else {
                    mView.login(false);
                }
            }
        });
    }
}
