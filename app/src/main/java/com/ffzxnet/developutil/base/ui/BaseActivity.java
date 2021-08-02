package com.ffzxnet.developutil.base.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.mvp.BaseActivityView;
import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.net.net_status.NetType;
import com.ffzxnet.developutil.net.net_status.NetworkLiveData;
import com.ffzxnet.developutil.utils.tools.LogUtil;
import com.ffzxnet.developutil.utils.tools.SetTextViewDrawable;
import com.ffzxnet.developutil.utils.ui.LoadingUtil;
import com.ffzxnet.developutil.utils.ui.ToastUtil;
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 创建者： feifan.pi 在 2017/3/6.
 */

public abstract class BaseActivity extends RxAppCompatActivity implements BaseActivityView, View.OnClickListener {
    //获取视图布局，该方法不能获取布局控件
    public abstract int getContentViewByBase(Bundle savedInstanceState);

    //设置完视图后调用，该方法可以获取布局控件并操作
    public abstract void createdViewByBase(Bundle savedInstanceState);

    //当前activity是否是活动状态
    protected boolean isActive;
    protected boolean hasMenu;//是否显示菜单
    //新版StartActivityResult
    protected ActivityResultLauncher resultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸式主题
        setFullscreen(true, true, true);
        //设置布局文件
        setContentView(getContentViewByBase(savedInstanceState));
        //监听网络全局
        monitorNetStatus();
        //绑定控件
        ButterKnife.bind(this);
        //设置状态栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        View toolbarStatusVew = findViewById(R.id.toolbar_status_bg);
        if (null != toolbar) {
            int padding = (int) (MyConstans.Screen_Height * 0.015);
            toolbar.setPadding(padding, MyConstans.Screen_Status_Height + padding,
                    padding, padding);
        }
        if (null != toolbarStatusVew) {
            //状态栏颜色
            toolbarStatusVew.setBackgroundResource(R.color.white);
            //设置高度
            toolbarStatusVew.getLayoutParams().height = MyConstans.Screen_Status_Height;
        }
        //开始加载页面
        createdViewByBase(savedInstanceState);
//        if (MyApplication.language != Locale.CHINESE) {
//            //切换语言
//            switchLanguage(MyApplication.language);
//        }
    }

    /**
     * 设置是否是沉浸式
     *
     * @param isShowStatusBar     true=显示状态栏
     * @param isShowNavigationBar true=显示导航栏
     * @param statusTextBlack     true=导航栏字体黑色
     */
    public void setFullscreen(boolean isShowStatusBar, boolean isShowNavigationBar, boolean statusTextBlack) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (!isShowStatusBar) {
            uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (!isShowNavigationBar) {
            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        //隐藏标题栏
        if (null != getSupportActionBar()) {
            getSupportActionBar().hide();
        }
        //设置顶部状态栏和底部导航栏背景颜色为透明
        setNavigationStatusColor(Color.TRANSPARENT);

        if (statusTextBlack) {
            //导航栏文字黑色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            //导航栏文字白色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    public void setNavigationStatusColor(int color) {
        //VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setNavigationBarColor(color);
            getWindow().setStatusBarColor(color);
        }
    }

    public Bundle getBundle() {
        if (null == getIntent().getExtras()) {
            return new Bundle();
        } else {
            return getIntent().getExtras();
        }
    }

    /**
     * 退出Activity动画
     */
    public void finishActivity(Activity activity) {
        activity.finish();
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
        Intent it = new Intent(this, target);
        if (null != data) {
            it.putExtras(data);
        }
        if (flags > -1) {
            it.setFlags(flags);
        }
        startActivity(it);
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
     * IntentForResult跳转
     *
     * @param
     */
    protected void redirectActivityForResult(ActivityResultContract resultContract, ActivityResultCallback resultCallback) {
        resultLauncher = registerForActivityResult(resultContract, resultCallback);
    }

    /**
     * 延时返回
     */
    protected void goBackBySlowly() {
        //延迟，让点击的效果飘一伙~
        Observable.timer(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {
                        finishActivity(BaseActivity.this);
                    }
                });

    }

    /**
     * 延时返回
     *
     * @param time 延时时间
     */
    protected void goBackBySlowly(long time) {
        //延迟，让点击的效果飘一伙~
        Observable.timer(time, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {
                        finishActivity(BaseActivity.this);
                    }
                });

    }

    /**
     * 返回
     */
    protected void goBackByQuick() {
        finishActivity(this);
    }

    /**
     * 点击标题返回按钮
     */
    protected abstract void onClickTitleBack();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME ||
                keyCode == KeyEvent.KEYCODE_BACKSLASH) && !LoadingUtil.isShowing()) {
            goBackByQuick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_left_tv) {
            onClickTitleBack();
        }
        if (v.getId() == R.id.toolbar_right_tv) {
            onClickRightBtn();
        }
    }

    protected void onClickRightBtn() {

    }

    //进度弹窗

    @Override
    public void showLoadingDialog(boolean b, String msg) {
        LoadingUtil.showLoadingDialog(b, msg);
    }

    @Override
    public void showLoadingDialog(boolean b) {
        LoadingUtil.showLoadingDialog(b);
    }
//============初始化ToolBar==========================

    /**
     * 初始化ToolBar
     *
     * @param back_name   返回按钮旁边的标题
     * @param center_name 中间标题
     */
    public void initToolBar(String back_name, String center_name) {
        initToolBar(back_name, center_name, true);
    }

    /**
     * 初始化ToolBar
     *
     * @param back_name   返回按钮旁边的标题
     * @param center_name 中间标题
     */
    public void initToolBar(String back_name, String center_name, boolean showBackView) {
        if (!TextUtils.isEmpty(center_name)) {
            //中间标题
            ((TextView) findViewById(R.id.toolbar_title_name)).setText(center_name);
        }
        TextView toolbarLeft = ((TextView) findViewById(R.id.toolbar_left_tv));
        if (!TextUtils.isEmpty(back_name)) {
            //返回按钮旁边的标题
            toolbarLeft.setText(back_name);
        }
        if (showBackView) {
            SetTextViewDrawable.setLeftView(toolbarLeft, R.mipmap.icon_toleft);
        }
        //加大点击区域
        toolbarLeft.setOnClickListener(this);
    }

    /**
     * 改变toolBar字体色
     */
    public void setToolBarTitleColor(int colorId) {
        ((TextView) findViewById(R.id.toolbar_title_name)).setTextColor(MyApplication.getColorByResId(colorId));
    }

    /**
     * 改变toolBar返回按钮图标
     */
    public void setToolBarBackView(int drawableId) {
        TextView textView = (TextView) findViewById(R.id.toolbar_left_tv);
        if (null != textView) {
            if (drawableId == -1 || drawableId == 0) {
                SetTextViewDrawable.claearView(textView);
            } else {
                SetTextViewDrawable.setLeftView(textView, R.mipmap.icon_toleft);
            }
        }
    }

    /**
     * 改变toolBar背景色
     */
    public void setToolBarBackground(int resID) {
        ((Toolbar) findViewById(R.id.toolbar)).setBackgroundResource(resID);
    }

    /**
     * 设置顶部状态栏背景色
     *
     * @param color
     */
    public void setStatusBackgroundColor(int color) {
        View status = findViewById(R.id.toolbar_status_bg);
        if (null != status) {
            status.setBackgroundColor(color);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (hasMenu) {
//        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
//            return true;
//        } else {
//            return super.onCreateOptionsMenu(menu);
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //返回按钮
                onClickTitleBack();
                break;
//            case R.id.action_item1:
//                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //============初始化ToolBar End==========================

    public String getStringByResId(int str) {
        return getResources().getString(str);
    }

    private void monitorNetStatus() {
        NetworkLiveData.get(this).observe(this, new androidx.lifecycle.Observer<NetType>() {
            @Override
            public void onChanged(NetType netType) {
                switch (netType) {
                    case NET_UNKNOW:
                    case NET_4G:
                    case NET_3G:
                    case NET_2G:
                        //手机网络
                        LogUtil.e("NetStatus", "手机网络");
                        getNetInfo(false, true);
                        break;
                    case WIFI:
                        //WIFI
                        LogUtil.e("NetStatus", "WIFI网络");
                        getNetInfo(true, true);
                        break;
                    case NOME:
                        ToastUtil.showToastShort(getStringByResId(R.string.net_error_for_link_exception));
                        LogUtil.e("NetStatus", "无网络");
                        //没有网络，提示用户跳转到设置
                        getNetInfo(false, false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //网络监听
    public void getNetInfo(boolean isWifi, boolean isConnect) {
        LogUtil.e("dddd", "是否是WiFi=" + isWifi + "==是否联网=" + isConnect);
    }

    /**
     * activity下面的子Fragment相互跳转
     *
     * @param flag   Fragment的标识,指定跳转对应的Fragment
     * @param bundle 夹带参数Bundle
     */
    public void beginShowFragment(int flag, Bundle bundle) {
    }

    //切换语言====================================

    /**
     * 切换语言
     *
     * @param
     */
    public void switchLanguage(Locale locale) {
        Resources res = getResources();
        Configuration config = res.getConfiguration();
        DisplayMetrics dm = res.getDisplayMetrics();
        Locale currentLocal;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentLocal = config.getLocales().get(0);
            config.setLocale(locale);
            res.updateConfiguration(config, dm);
            // 如果切换了语言
            if (!currentLocal.equals(config.getLocales().get(0))) {
                // 这里需要重新刷新当前页面中使用到的资源
                MyApplication.language = locale;
                recreate();
            }
        } else {
            currentLocal = config.locale;
            config.locale = locale;
            res.updateConfiguration(config, dm);
            // 如果切换了语言
            if (!currentLocal.equals(config.locale)) {
                // 这里需要重新刷新当前页面中使用到的资源
                MyApplication.language = locale;
                recreate();
            }
        }
    }
}
