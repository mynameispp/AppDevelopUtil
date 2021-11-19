package com.ffzxnet.developutil.ui.login;

import android.Manifest;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.base.ui.CheckPermissionCallBak;
import com.ffzxnet.developutil.bean.LoginRequestBean;
import com.ffzxnet.developutil.ui.main.MainActivity;
import com.ffzxnet.developutil.utils.tools.DeviceUtil;
import com.ffzxnet.developutil.utils.tools.MD5Util;
import com.ffzxnet.developutil.utils.ui.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginContract.View {
    @BindView(R.id.login_name)
    EditText loginName;
    @BindView(R.id.login_pwd)
    EditText loginPwd;

    private LoginPresenter mPresenter;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new LoginPresenter(this, this);
        mPresenter.start();
    }

    @Override
    public void initView() {
        initToolBar("", "登录", false);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = (LoginPresenter) presenter;
    }

    @Override
    protected void onClickTitleBack() {

    }

    @OnClick(R.id.login_btn)
    public void onViewClicked() {
        CheckPermissionDialog(new CheckPermissionCallBak() {
                                  @Override
                                  public void hasPermission(boolean success) {
                                      if (success) {
                                          LoginRequestBean requestBean = new LoginRequestBean();
                                          requestBean.setUsername(loginName.getText().toString().trim());
                                          requestBean.setPassword(MD5Util.encode2(loginPwd.getText().toString().trim()));
                                          requestBean.setDeviceNo(DeviceUtil.getAdresseMAC(MyApplication.getContext()));
                                          mPresenter.login(requestBean);
                                      }
                                  }
                              }, Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void login(boolean success) {
        if (success) {
            redirectActivity(MainActivity.class);
            finishActivity(this);
        } else {
            ToastUtil.showToastShort("账号或者密码错误", Gravity.CENTER);
        }
    }
}
