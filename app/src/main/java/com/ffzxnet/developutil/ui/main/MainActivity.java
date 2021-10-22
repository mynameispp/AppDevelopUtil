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
import com.ffzxnet.developutil.evenbus.MyEventbus;
import com.ffzxnet.developutil.ui.for_result_activity.ForResultActivity;
import com.ffzxnet.developutil.ui.video_download.utils.DownLoadUtil;
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
        redirectActivityForResult(new ActivityResultC(), new ActivityResultCallback() {
            @Override
            public void onActivityResult(Object result) {
                if (null != result) {
                    ToastUtil.showToastShort("返回的值=" + result.toString());
                }
            }
        });
        //下载监听
        DownLoadUtil.stopAllDownLoading();
        VideoDownloadManager.getInstance().setGlobalDownloadListener(mDownloadListener);
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
                Bundle bundle = new Bundle();
                bundle.putString(MyConstans.Key_Title_Name, "传进来的数据111");
                resultLauncher.launch(bundle);
                break;
        }
    }

    //StartActivityForResult请求
    static class ActivityResultC extends ActivityResultContract<Bundle, Object> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Bundle input) {
            //创建请求
            Intent intent = new Intent(context, ForResultActivity.class);
            intent.putExtras(input);
            return intent;
        }

        @Override
        public Object parseResult(int resultCode, @Nullable Intent intent) {
            //回调返回值
            if (resultCode == 110 && null != intent) {
                return intent.getStringExtra(MyConstans.KEY_DATA);
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

    private DownloadListener mDownloadListener = new DownloadListener() {
        private long mLastProgressTimeStamp;

        @Override
        public void onDownloadDefault(VideoTaskItem item) {
            LogUtils.e("ddddd", "onDownloadDefault: " + item);
            EventBus.getDefault().post(new MyEventbus.DownloadingEven(item));
        }

        @Override
        public void onDownloadPending(VideoTaskItem item) {
            LogUtils.e("ddddd", "onDownloadPending: " + item);
            EventBus.getDefault().post(new MyEventbus.DownloadingEven(item));
        }

        @Override
        public void onDownloadPrepare(VideoTaskItem item) {
            LogUtils.e("ddddd", "onDownloadPrepare: " + item);
            EventBus.getDefault().post(new MyEventbus.DownloadingEven(item));
        }

        @Override
        public void onDownloadStart(VideoTaskItem item) {
            LogUtils.e("ddddd", "onDownloadStart: " + item);
            EventBus.getDefault().post(new MyEventbus.DownloadingEven(item));
        }

        @Override
        public void onDownloadProgress(VideoTaskItem item) {
            long currentTimeStamp = System.currentTimeMillis();
            if (currentTimeStamp - mLastProgressTimeStamp > 1000) {
                LogUtils.e("ddddd", "onDownloadProgress: " + item.getPercentString() + ", curTs=" + item.getCurTs() + ", totalTs=" + item.getTotalTs());
                EventBus.getDefault().post(new MyEventbus.DownloadingEven(item));
                mLastProgressTimeStamp = currentTimeStamp;
            }
        }

        @Override
        public void onDownloadSpeed(VideoTaskItem item) {
            long currentTimeStamp = System.currentTimeMillis();
            if (currentTimeStamp - mLastProgressTimeStamp > 1000) {
                EventBus.getDefault().post(new MyEventbus.DownloadingEven(item));
                mLastProgressTimeStamp = currentTimeStamp;
            }
        }

        @Override
        public void onDownloadPause(VideoTaskItem item) {
            LogUtils.e("ddddd", "onDownloadPause: " + item.getUrl());
            EventBus.getDefault().post(new MyEventbus.DownloadingEven(item));
        }

        @Override
        public void onDownloadError(VideoTaskItem item) {
            LogUtils.e("ddddd", "onDownloadError: " + item.getUrl());
            EventBus.getDefault().post(new MyEventbus.DownloadingEven(item));
        }

        @Override
        public void onDownloadSuccess(VideoTaskItem item) {
            LogUtils.e("ddddd", "onDownloadSuccess: " + item);
            EventBus.getDefault().post(new MyEventbus.DownloadingEven(item));
        }
    };
}
