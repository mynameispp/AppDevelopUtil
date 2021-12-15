package com.ffzxnet.developutil.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.base.ui.BaseActivityResultContact;
import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.evenbus.MyEventbus;
import com.ffzxnet.developutil.ui.for_result_activity.ForResultActivity;
import com.ffzxnet.developutil.ui.video_download.utils.DownLoadUtil;
import com.ffzxnet.developutil.utils.tools.AntiHijackingUtil;
import com.ffzxnet.developutil.utils.ui.ToastUtil;
import com.ffzxnet.developutil.utils.video_download.VideoDownloadManager;
import com.ffzxnet.developutil.utils.video_download.listener.DownloadListener;
import com.ffzxnet.developutil.utils.video_download.model.VideoTaskItem;
import com.ffzxnet.developutil.utils.video_download.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

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
        redirectActivityForResult(new BaseActivityResultContact(), new ActivityResultCallback() {
            @Override
            public void onActivityResult(Object result) {
                if (null != result) {
                    ToastUtil.showToastShort("返回的值=" + result.toString());
                }
            }
        });
        //进来先重置下载中的状态
        DownLoadUtil.stopAllDownLoading();
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
                setToolBarTitle("列表");
                break;
            case R.id.main_bottom_2:
                mainPresenter.showFragment(MyConstans.Flag_Fragment_Seconde, null);
                setToolBarTitle("功能");
                break;
            case R.id.main_bottom_3:
                //StartActivityResult新版替换方法
                Bundle bundle = new Bundle();
                bundle.putString(MyConstans.Key_Title_Name, "传进来的数据111");
                //创建请求
                Intent intent = new Intent(this, ForResultActivity.class);
                intent.putExtras(bundle);
                resultLauncher.launch(intent);
                break;
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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            } else {
//                finish();
////                System.exit(0);//杀死进程
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止所有下载
        DownLoadUtil.stopAllDownLoading();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //防止Activity被劫持
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 白名单
                boolean safe = AntiHijackingUtil.checkActivity(getApplicationContext());
                // 系统桌面
                boolean isHome = AntiHijackingUtil.isHome(getApplicationContext());
                // 锁屏操作
                boolean isReflectScreen = AntiHijackingUtil.isReflectScreen(getApplicationContext());
                // 判断程序是否当前显示
                if (!safe && !isHome && !isReflectScreen) {
                    Looper.prepare();
                    ToastUtil.showToastLong(getString(R.string.activity_safe_warning));
                    Looper.loop();
                }
            }
        }).start();
    }
}
