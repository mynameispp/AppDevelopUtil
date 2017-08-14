package com.ffzxnet.developutil.base.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffzxnet.countrymeet.R;
import com.ffzxnet.countrymeet.utils.ui.LoadingDialog;

/**
 * 创建者： feifan.pi 在 2017/5/15.
 */

public abstract class BaseFragment extends Fragment {

    public boolean isActive;

    protected abstract void initView(View view, Bundle savedInstanceState);

    //获取布局文件ID
    protected abstract int getLayoutId();

    //加载弹窗
    private LoadingDialog loadingDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadingDialog = new LoadingDialog(getContext());
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
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
        startActivity(it);
        // 进入Activity动画
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_center);
    }

    /**
     * Intent跳转
     *
     * @param it
     */
    protected void redirectActivity(Intent it) {
        startActivity(it);
        // 进入Activity动画
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_center);
    }

    /**
     * IntentForResult跳转
     *
     * @param it
     */
    protected void redirectActivityForResult(Intent it, int requestCode) {
        startActivityForResult(it, requestCode);
        // 进入Activity动画
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_center);
    }


    public void showLoadingDialog(boolean b ) {
        if (b) {
            loadingDialog.show();
        } else {
            loadingDialog.dismiss();
        }
    }

    public void showLoadingDialog(boolean b, String msg){
        if (b) {
            loadingDialog.showDailog(msg);
        } else {
            loadingDialog.dismiss();
        }
    }

}
