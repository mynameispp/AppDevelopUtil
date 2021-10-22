package com.ffzxnet.developutil.base.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.mvp.BaseActivityView;
import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.net.ErrorResponse;
import com.ffzxnet.developutil.utils.ui.LoadingDialog;
import com.ffzxnet.developutil.utils.ui.LoadingUtil;
import com.trello.rxlifecycle4.components.support.RxFragment;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.appcompat.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 创建者： feifan.pi 在 2017/5/15.
 */

public abstract class BaseFragment extends RxFragment implements BaseActivityView {
    //获取布局文件ID
    protected abstract int getLayoutId();

    //开始加载布局
    protected abstract void onMyCreateView(View rootView, Bundle savedInstanceState);

    public Context mContext;
    public boolean isVisible = false;//懒加载
    //加载弹窗
    private LoadingDialog loadingDialog;
    //新版startActivityForResult
    protected ActivityResultLauncher resultLauncher;

    private Unbinder unbinder;
    public View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        unbinder = ButterKnife.bind(this, rootView);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        View toolbarStatusVew = rootView.findViewById(R.id.toolbar_status_bg);
        if (null != toolbar) {
            //自适应各种屏幕
            toolbar.setPadding(0, (int) (MyConstans.Screen_Height * 0.05),
                    0, (int) (MyConstans.Screen_Height * 0.015));
        }
        if (null != toolbarStatusVew) {
            //状态栏颜色
            toolbarStatusVew.setBackgroundResource(R.color.white);
        }
        onMyCreateView(rootView, savedInstanceState);
        return rootView;
    }

    @Override
    public void catchApiSubscriberError(ErrorResponse error) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isVisible = false;
    }


    /**
     * Intent跳转
     *
     * @param target
     */
    protected void redirectActivity(Class<? extends Activity> target) {
        redirectActivity(target, null, -1);
    }

    /**
     * Intent跳转
     *
     * @param target
     */
    protected void redirectActivity(Class<? extends Activity> target, Bundle bundle) {
        redirectActivity(target, bundle, -1);
    }

    /**
     * Intent跳转
     *
     * @param target
     */
    protected void redirectActivity(Class<? extends Activity> target, int flags) {
        redirectActivity(target, null, flags);
    }

    /**
     * Intent跳转
     *
     * @param target
     */
    protected void redirectActivity(Class<? extends Activity> target,
                                    Bundle data, int flags) {
        Intent it = new Intent(getContext(), target);
        if (null != data) {
            it.putExtras(data);
        }
        if (flags > -1) {
            it.setFlags(flags);
        }
        redirectActivity(it);
    }

    /**
     * Intent跳转
     *
     * @param it
     */
    protected void redirectActivity(Intent it) {
        startActivity(it);
    }

    /**
     * Intent跳转
     *
     * @param target
     */
    protected void redirectActivityForAnima(Class<? extends Activity> target, Bundle
            bundle, int animaEnterResId) {
        Intent it = new Intent(getContext(), target);
        if (null != bundle) {
            it.putExtras(bundle);
        }
        startActivity(it);
    }

    /**
     * IntentForResult跳转
     */
    protected void redirectActivityForResult(ActivityResultContract resultContract, ActivityResultCallback resultCallback) {
        resultLauncher = registerForActivityResult(resultContract, resultCallback);
    }

    @Override
    public void showLoadingDialog(boolean b) {
        if (isVisible) {
            LoadingUtil.showLoadingDialog(b);
        }

    }

    @Override
    public void showLoadingDialog(boolean b, String msg) {
        if (isVisible) {
            LoadingUtil.showLoadingDialog(b, msg);
        }
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isVisible = !hidden;
        if (isVisible) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void lazyLoad() {
    }

    protected void onInvisible() {
    }

    public String getStringByRes(int resId) {
        return MyApplication.getStringByResId(resId);
    }
}
