package com.ffzxnet.developutil.base.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ffzxnet.countrymeet.R;
import com.ffzxnet.countrymeet.application.MyApplication;
import com.ffzxnet.countrymeet.base.mvp.BaseActivityView;
import com.ffzxnet.countrymeet.utils.tools.LogUtil;
import com.ffzxnet.countrymeet.utils.ui.LoadingDialog;
import com.umeng.socialize.UMShareAPI;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 创建者： feifan.pi 在 2017/3/6.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseActivityView {

    /**
     * 当前activity是否是活动状态
     */
    protected boolean isActive;

    protected boolean hasMenu;//是否显示菜单

    //加载弹窗
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new LoadingDialog(this);
        //改变状态栏的颜色
//        String activtyName = this.getLocalClassName();
//        if (activtyName.contains("MainActivity") || activtyName.contains("UserInfoActivity")) {
//            setStausColor(R.color.colorAccent);
//        } else {
//            setStausColor(R.color.white);
//        }
//        if (MyApplication.language != Locale.CHINESE) {
//            //切换语言
//            switchLanguage(MyApplication.language);
//        }
    }

    /**
     * 5.0以上系统设置手机状态栏颜色
     *
     * @param colorId
     */
    public void setStausColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(MyApplication.getColorByResId(colorId));
            //底部虚拟按键颜色
//            window.setNavigationBarColor(MyApplication.getColorByResId(colorId));
            //判断当前手机品牌
            String phoneSystem = Build.MANUFACTURER;
            if ("Xiaomi".equalsIgnoreCase(phoneSystem)) {
                MIUI(colorId);
            } else if (("samsung".equalsIgnoreCase(phoneSystem) || "huawei".equalsIgnoreCase(phoneSystem))
                    && colorId == R.color.white) {
                //三星华为手机如果设置状态栏为白色，则统一改成系统色
                window.setStatusBarColor(MyApplication.getColorByResId(R.color.colorPrimaryDark));
            }
        }
    }

    //更改小米系统的状态栏颜色
    public boolean MIUI(int colorId) {
        boolean result = false;
        Window window = getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (colorId == R.color.white) {//状态栏透明且黑色字体
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {//清除黑色字体
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
                result = true;
            } catch (Exception e) {
                LogUtil.e("sss", e.toString());
            }
        }
        return result;
    }

    /**
     * 退出Activity动画
     */
    public void finishActivity(Activity activity) {
        activity.finish();
        overridePendingTransition(R.anim.push_center, R.anim.push_right_out);
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
        // 进入Activity动画
        overridePendingTransition(R.anim.push_left_in, R.anim.push_center);
    }

    /**
     * Intent跳转
     *
     * @param it
     */
    protected void redirectActivity(Intent it) {
        startActivity(it);
        // 进入Activity动画
        overridePendingTransition(R.anim.push_left_in, R.anim.push_center);
    }

    /**
     * IntentForResult跳转
     *
     * @param it
     */
    protected void redirectActivityForResult(Intent it, int requestCode) {
        startActivityForResult(it, requestCode);
        // 进入Activity动画
        overridePendingTransition(R.anim.push_left_in, R.anim.push_center);
    }

    /**
     * 返回
     */
    protected void goBack() {
        finishActivity(this);
    }

    /**
     * 点击标题返回按钮
     */
    protected abstract void onClickTitleBack();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && !loadingDialog.isShowing()) {
            goBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    //进度弹窗


    @Override
    public void showLoadingDialog(boolean b) {
        if (b) {
            loadingDialog.ShowDialog();
        } else {
            loadingDialog.closeDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //============初始化ToolBar==========================

    /**
     * 初始化ToolBar
     *
     * @param back_name   返回按钮旁边的标题
     * @param center_name 中间标题
     * @param hasBack     是否显示返回按钮
     * @param hasMenu     是否显示菜单按钮
     */
    public void initToolBar(String back_name, String center_name, boolean hasBack, boolean hasMenu) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //返回按钮旁边的标题
        toolbar.setTitle(back_name);
        if (!TextUtils.isEmpty(center_name)) {
            //中间标题
            ((TextView) findViewById(R.id.title_name)).setText(center_name);
        }
        if (hasBack) {
            //替换toolbar系统的返回按钮
            toolbar.setNavigationIcon(R.mipmap.icon_left_arrow);
            setSupportActionBar(toolbar);
            //设置返回按钮的监听事件
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            setSupportActionBar(toolbar);
        }
        this.hasMenu = hasMenu;
    }

    /**
     * 改变toolBar字体色
     */
    public void setToolBarTitleColor(int colorId) {
        ((TextView) findViewById(R.id.title_name)).setTextColor(MyApplication.getColorByResId(colorId));
    }

    /**
     * 改变toolBar返回按钮图标
     */
    public void setToolBarBackView(int drawableId) {
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(drawableId);
    }

    /**
     * 改变toolBar背景色
     */
    public void setToolBarBackground(int resID) {
        ((Toolbar) findViewById(R.id.toolbar)).setBackgroundResource(resID);
    }

    /**
     * 初始化ToolBar
     *
     * @param back_name   返回按钮旁边的标题
     * @param center_name 中间标题
     * @param hasBack     是否显示返回按钮
     * @param hasMenu     是否显示菜单按钮
     */
    public void initToolBar(String back_name, String center_name, boolean hasBack, boolean hasMenu, int resColor) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //返回按钮旁边的标题
        toolbar.setTitle(back_name);
        toolbar.setBackgroundColor(MyApplication.getColorByResId(resColor));
        if (!TextUtils.isEmpty(center_name)) {
            //中间标题
            ((TextView) findViewById(R.id.title_name)).setText(center_name);
        }
        if (hasBack) {
            //替换toolbar系统的返回按钮
            toolbar.setNavigationIcon(R.mipmap.icon_title_back_white);
            setSupportActionBar(toolbar);
            //设置返回按钮的监听事件
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            setSupportActionBar(toolbar);
        }
        this.hasMenu = hasMenu;
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

    //切换语言====================================

    /**
     * 切换语言
     *
     * @param
     */
//    public void switchLanguage(Locale locale) {
//        Resources res = getResources();
//        Configuration config = res.getConfiguration();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Locale currentLocal;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            currentLocal = config.getLocales().get(0);
//            config.setLocale(locale);
//            res.updateConfiguration(config, dm);
//            // 如果切换了语言
//            if (!currentLocal.equals(config.getLocales().get(0))) {
//                // 这里需要重新刷新当前页面中使用到的资源
//                MyApplication.language = locale;
//                recreate();
//            }
//        } else {
//            currentLocal = config.locale;
//            config.locale = locale;
//            res.updateConfiguration(config, dm);
//            // 如果切换了语言
//            if (!currentLocal.equals(config.locale)) {
//                // 这里需要重新刷新当前页面中使用到的资源
//                MyApplication.language = locale;
//                recreate();
//            }
//        }
//
//    }
    public <T> T findByID(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //必须要重写acitvity此方法，否则友盟分享不会回调
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


}
