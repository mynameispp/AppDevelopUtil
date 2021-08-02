package com.ffzxnet.developutil.ui.main;

import android.os.Bundle;

import com.ffzxnet.developutil.base.mvp.BasePresenterParent;
import com.ffzxnet.developutil.base.net.BaseApiResultData;
import com.ffzxnet.developutil.base.ui.BaseFragment;
import com.ffzxnet.developutil.bean.LoginRequestBean;
import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.net.ApiImp;
import com.ffzxnet.developutil.net.ErrorResponse;
import com.ffzxnet.developutil.net.IApiSubscriberCallBack;
import com.ffzxnet.developutil.ui.main.fragment.first.FirstFragment;
import com.ffzxnet.developutil.ui.main.fragment.second.SecondFragment;
import com.ffzxnet.developutil.utils.tools.MD5Util;
import com.trello.rxlifecycle4.LifecycleProvider;

public class MainPresenter extends BasePresenterParent implements MainContract.Presenter {
    private MainContract.View mView;
    private BaseFragment oldFragment = null;
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;

    public MainPresenter(LifecycleProvider lifecycleProvider, MainContract.View mView) {
        super(lifecycleProvider);
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void showFragment(int flag, Bundle bundle) {
        BaseFragment fragment = null;
        switch (flag) {
            case MyConstans.Flag_Fragment_First:
                if (firstFragment == null) {
                    firstFragment = new FirstFragment();
                }
                firstFragment.setArguments(bundle);
                fragment = firstFragment;
                break;
            case MyConstans.Flag_Fragment_Seconde:
                if (secondFragment == null) {
                    secondFragment = new SecondFragment();
                }
                secondFragment.setArguments(bundle);
                fragment = secondFragment;
                break;
        }
        mView.showFragment(fragment, oldFragment);
        oldFragment = fragment;
    }

    @Override
    public void testApi() {
        LoginRequestBean requestBean=new LoginRequestBean();
        requestBean.setDeviceNo("12");
        requestBean.setUsername("admin");
        requestBean.setPassword(MD5Util.encode2("123456"));
        ApiImp.getInstance().login(requestBean, getLifecycleTransformerByStopToActivity(), new IApiSubscriberCallBack<BaseApiResultData<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(ErrorResponse error) {

            }

            @Override
            public void onNext(BaseApiResultData<String> stringBaseApiResultData) {

            }
        });
    }
}
