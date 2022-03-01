package com.ffzxnet.developutil.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ffzxnet.developutil.BuildConfig;
import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.ui.login.LoginActivity;
import com.ffzxnet.developutil.ui.main.MainActivity;
import com.ffzxnet.developutil.utils.tools.MMKVUtil;
import com.ffzxnet.developutil.utils.tools.SignCheck;
import com.ffzxnet.developutil.utils.ui.PrivacyPolicyPopupDialog;
import com.ffzxnet.developutil.utils.ui.ToastUtil;
import com.smarx.notchlib.NotchScreenManager;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;

/**
 * 创建者： feifan.pi 在 2017/7/19.
 */

public class SplashActivity extends BaseActivity {
    @BindView(R.id.boot_page_vs)
    ViewStub bootPageVs;
    @BindView(R.id.boot_page_countdown_tv)
    TextView bootPageCountdownTv;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_boot_page;
    }

    @Override
    public void isFullScreen(boolean yes) {
        // 设置Activity全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        //这里放入签名文件的SHA1值
        //keytool -list -v -keystore 你的签名文件路径.jks
        //在你的JDK/bin文件夹下面执行上面文件查询对应的值
        if (!BuildConfig.DEBUG) {
            SignCheck signCheck = new SignCheck(this,
                    "E3:D8:62:29:47:03:40:69:E5:88:F9:8D:45:58:E5:FB:96:CB:E1:21");
            if (signCheck.check()) {
                //TODO 签名正常
            } else {
                //TODO 签名不正确
                ToastUtil.showToastShort("当前App可能被串改，请前往官方渠道下载正版");
                finish();
            }
        }
        //适配异形屏
        NotchScreenManager.getInstance().setDisplayInNotch(this);
//        MMKVUtil.getInstance().putBoolean(MMKVUtil.Agree_Privacy_Policy, false);
        //是否同意隐私政策
        if (!MMKVUtil.getInstance().getBoolean(MMKVUtil.Agree_Privacy_Policy, false)) {
            PrivacyPolicyPopupDialog dialog = new PrivacyPolicyPopupDialog();
            dialog.setListen(new PrivacyPolicyPopupDialog.Listen() {
                @Override
                public void privacyPolicyAgreeClick(boolean agree) {
                    if (agree) {
                        //同意协议
                        MMKVUtil.getInstance().putBoolean(MMKVUtil.Agree_Privacy_Policy, true);
                        //初始化Application的第三方代码
                        ((MyApplication) MyApplication.getContext()).checkAgreePrivacyPolicy();
                        initView();
                    } else {
                        finish();
                    }
                }
            });
            dialog.show(getSupportFragmentManager(), "PrivacyPolicyPopupDialog");
        } else {
            initView();
        }
    }

    private void initView() {
        //        boolean isFirstUsed = SharedPreferencesUtil.getInstance().getBoolean(SharedPreferencesUtil.KEY_FIRST_TIME_USE, true);
        boolean isFirstUsed = MMKVUtil.getInstance().getBoolean(MMKVUtil.Key_First_Open_App, true);
        if (isFirstUsed) {
            bootPageVs.inflate();
            ViewPager2 bootViewPage = findViewById(R.id.boot_page_vp);
            List<Integer> pictures = new ArrayList<>();
            pictures.add(R.mipmap.ic_launcher);
            pictures.add(R.mipmap.ic_launcher);
            pictures.add(R.mipmap.ic_launcher);
            pictures.add(R.mipmap.ic_launcher);
            bootViewPage.setAdapter(new SplashPageAdapter(pictures));
//            SharedPreferencesUtil.getInstance().putBoolean(SharedPreferencesUtil.KEY_FIRST_TIME_USE, false);
            MMKVUtil.getInstance().putBoolean(MMKVUtil.Key_First_Open_App, false);
            Button intoMainActivity = findViewById(R.id.splash_into_main_btn);
            intoMainActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirectActivity(LoginActivity.class);
                    finish();
                }
            });
            bootViewPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    if (position == pictures.size() - 1) {
                        intoMainActivity.setVisibility(View.VISIBLE);
                    } else {
                        intoMainActivity.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    redirectActivity(MainActivity.class);
                    finish();
                }
            }, 2000);
        }
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    public void showLoadingDialog(boolean b, String msg) {

    }
}
