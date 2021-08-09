package com.ffzxnet.developutil.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.ui.for_result_activity.ForResultActivity;
import com.ffzxnet.developutil.utils.ui.ToastUtil;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainContract.View, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.main_bottom_rg)
    RadioGroup mainBottomRg;

    //连按两次返回按钮退出App
    private long exitTime;
    private MainPresenter mainPresenter;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        //初始化
        new MainPresenter(this, this);
        mainPresenter.start();
    }

    @Override
    public void initView() {
        //初始化控件
        initToolBar("", "首页", false);
        mainBottomRg.setOnCheckedChangeListener(this);
        //默认选中第一个
        mainBottomRg.getChildAt(0).performClick();
        //注册ForResult监听
        redirectActivityForResult(new ActivityResultC(), new ActivityResultCallback() {
            @Override
            public void onActivityResult(Object result) {
                if (null != result) {
                    ToastUtil.showToastShort("返回的值=" + result.toString());
                }
            }
        });
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mainPresenter = (MainPresenter) presenter;
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_bottom_1:
                mainPresenter.showFragment(MyConstans.Flag_Fragment_First, null);
                break;
            case R.id.main_bottom_2:
                mainPresenter.showFragment(MyConstans.Flag_Fragment_Seconde, null);
                break;
            case R.id.main_bottom_3:
                //StartActivityResult新版替换方法
                resultLauncher.launch("");
                break;
        }
    }

    //请求
    static class ActivityResultC extends ActivityResultContract<String, Object> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, String input) {
            return new Intent(context, ForResultActivity.class);
        }

        @Override
        public Object parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == 110 && null != intent) {
                return intent.getStringExtra("value");
            } else {
                return null;
            }
        }
    }

    @Override
    public void showFragment(Fragment showFragment, Fragment hideFragment) {
        //加载Fragment
        if (null != showFragment) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (hideFragment == null) {
                if (null != fragmentManager.findFragmentByTag(showFragment.getClass().getSimpleName())) {
                    fragmentManager.beginTransaction()
                            .show(showFragment).commitAllowingStateLoss();
                } else {
                    fragmentManager.beginTransaction()
                            .add(R.id.main_content, showFragment, showFragment.getClass().getSimpleName())
                            .commitAllowingStateLoss();
                }
            } else {
                if (null != fragmentManager.findFragmentByTag(showFragment.getClass().getSimpleName())) {
                    fragmentManager.beginTransaction()
                            .hide(hideFragment).show(showFragment).commitAllowingStateLoss();
                } else {
                    fragmentManager.beginTransaction()
                            .hide(hideFragment)
                            .add(R.id.main_content, showFragment, showFragment.getClass().getSimpleName())
                            .commitAllowingStateLoss();
                }
            }
        } else {
            ToastUtil.showToastLong("跳转失败");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
