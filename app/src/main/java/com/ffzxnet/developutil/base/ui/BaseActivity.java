package com.ffzxnet.developutil.base.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.ffzxnet.developutil.net.ErrorResponse;
import com.ffzxnet.developutil.net.net_status.NetType;
import com.ffzxnet.developutil.net.net_status.NetworkLiveData;
import com.ffzxnet.developutil.ui.login.LoginActivity;
import com.ffzxnet.developutil.ui.unlock.code.language.LanguageType;
import com.ffzxnet.developutil.ui.unlock.code.language.LanguageUtil;
import com.ffzxnet.developutil.utils.tools.LogUtil;
import com.ffzxnet.developutil.utils.tools.MMKVUtil;
import com.ffzxnet.developutil.utils.tools.SetTextViewDrawable;
import com.ffzxnet.developutil.utils.ui.LoadingUtil;
import com.ffzxnet.developutil.utils.ui.PermissionDescriptionDialog;
import com.ffzxnet.developutil.utils.ui.ToastUtil;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity;

import java.net.HttpURLConnection;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

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
    //权限说明
    private PermissionDescriptionDialog permissionDescriptionDialog;
    private RxPermissions rxPermissions;

    //需要全屏，继承类覆写这个方法即可，参考SplashActivity
    public void isFullScreen(boolean yes) {
        if (yes) {
            setFullscreen(false, false, false);
        } else {
            setFullscreen(true, true, true);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸式主题
        isFullScreen(false);
        //设置布局文件
        setContentView(getContentViewByBase(savedInstanceState));
        //绑定控件
        ButterKnife.bind(this);
        //监听网络全局
        monitorNetStatus();
        //设置状态栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (null != toolbar) {
            int padding = (int) (MyConstans.Screen_Height * 0.015);
            toolbar.setPadding(padding, MyConstans.Screen_Status_Height + padding,
                    padding, padding);
            //标题栏背景色.也可以在Style文件里面设置
//            toolbar.setBackgroundColor(MyApplication.getColorByResId(R.color.white));
        }
        //状态栏底色
        View toolbarStatusBGVew = findViewById(R.id.toolbar_status_bg);
        if (null != toolbarStatusBGVew) {
            //状态栏颜色
            toolbarStatusBGVew.setBackgroundResource(R.color.white);
            //设置高度
            toolbarStatusBGVew.getLayoutParams().height = MyConstans.Screen_Status_Height;
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
        //隐藏标题栏
        if (null != getSupportActionBar()) {
            getSupportActionBar().hide();
        }
        //设置顶部状态栏和底部导航栏背景颜色为透明
        setNavigationStatusColor(Color.TRANSPARENT);

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
        if (statusTextBlack) {
            //导航栏文字黑色
            uiOptions |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }

        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
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
     * @param timeSecond 延时时间(秒)
     */
    protected void goBackBySlowly(int timeSecond) {
        //延迟，让点击的效果飘一伙~
        Observable.timer(timeSecond, TimeUnit.SECONDS)
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
                        goBackByQuick();
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
            if (isTaskRoot() && !(this instanceof LoginActivity)) {
                //回退到桌面，再次打开会先显示Splash界面
                moveTaskToBack(true);
            } else {
                goBackByQuick();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        //加载提示
        LoadingUtil.init(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
        //加载提示
        LoadingUtil.destory();
    }

    @Override
    public void onClick(View v) {
        //ToolBar左右两边的控件
        if (v.getId() == R.id.toolbar_left_tv) {
            onClickTitleBack();
        }
        if (v.getId() == R.id.toolbar_right_tv) {
            onClickRightBtn(v);
        }
    }

    protected void onClickRightBtn(View v) {

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

    private boolean goLogin;

    @Override
    public void catchApiSubscriberError(ErrorResponse error) {
        if (error.getCode() == HttpURLConnection.HTTP_UNAUTHORIZED && !goLogin) {
            //goLogin 防止多个请求跳转
            goLogin = true;
            ToastUtil.showToastShort(error.getMessage());
            //Token失效
        }
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
        //左边控件
        TextView toolbarLeft = findViewById(R.id.toolbar_left_tv);
        if (!TextUtils.isEmpty(back_name)) {
            //返回按钮旁边的标题
            toolbarLeft.setText(back_name);
        }
        if (showBackView) {
            //设置返回按钮图标
            SetTextViewDrawable.setLeftView(toolbarLeft, R.mipmap.icon_toleft);
        }
        //右边控件
        TextView toolbarRight = findViewById(R.id.toolbar_right_tv);
        //设置点击事件
        toolbarLeft.setOnClickListener(this);
        toolbarRight.setOnClickListener(this);
    }

    /**
     * 改变toolBar标题字体色
     */
    public void setToolBarTitleColor(int colorId) {
        ((TextView) findViewById(R.id.toolbar_title_name)).setTextColor(MyApplication.getColorByResId(colorId));
    }

    /**
     * 改变toolBar标题
     */
    public void setToolBarTitle(String title) {
        ((TextView) findViewById(R.id.toolbar_title_name)).setText(title);
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
    @Override
    protected void attachBaseContext(Context newBase) {
        //获取我们存储的语言环境 比如 "en","zh",等等
        String language = MMKVUtil.getInstance().getString(MMKVUtil.CURRENT_APP_LANGUAGE, LanguageType.CHINESE.getLanguage());
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase, language));
    }

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

    /**
     * 请求权限
     *
     * @param callBak 授权结果
     * @param perms   要申请的权限
     */
    public void CheckPermissionDialog(CheckPermissionDialogCallBak callBak, String... perms) {
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        boolean hasPermission = true;
        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(this, perm)
                    != PackageManager.PERMISSION_GRANTED) {
                hasPermission = false;
                break;
            }
        }
        if (hasPermission) {
            callBak.hasPermission(true);
        } else {
            if (permissionDescriptionDialog == null) {
                permissionDescriptionDialog = new PermissionDescriptionDialog();
            }
            Bundle bundle = new Bundle();
            bundle.putStringArray(PermissionDescriptionDialog.PermissionData, perms);
            permissionDescriptionDialog.setArguments(bundle);
            permissionDescriptionDialog.setOnclickListen(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != permissionDescriptionDialog) {
                        permissionDescriptionDialog.dismiss();
                    }
                    rxPermissions.request(perms).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                callBak.hasPermission(true);
                            } else {
                                //权限拒绝 申请权限
                                callBak.hasPermission(false);
                                ToastUtil.showToastShort("没有权限！请到系统设置里面开启对应权限");
                            }
                        }
                    });
                }
            });

            if (isActive) {
                if (permissionDescriptionDialog.isVisible()) {
                    permissionDescriptionDialog.dismiss();
                }
                permissionDescriptionDialog.showNow(getSupportFragmentManager(), "permissionDescriptionDialog");
            }
        }
    }
}
