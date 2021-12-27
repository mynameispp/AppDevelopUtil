package com.ffzxnet.developutil.ui.unlock.code.language;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.ui.main.MainActivity;
import com.ffzxnet.developutil.utils.tools.MMKVUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置App内语言，记得设置下面两个类对应的方法
 * 1.MyApplication   initLanguage();
 * 2.BaseActivity    attachBaseContext(Context newBase)
 */
public class LanguageSettingActivity extends BaseActivity {

    @BindView(R.id.cb_chinese)
    ImageView cbChinese;
    @BindView(R.id.cb_english)
    ImageView cbEnglish;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_language_setting;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", getString(R.string.languange_title));

        cbChinese.setEnabled(true);
        cbChinese.setEnabled(true);
        if (LanguageUtil.isEnglish()) {
            cbEnglish.setBackgroundResource(R.mipmap.icon_check_open);
            cbChinese.setBackgroundResource(R.mipmap.icon_check_close);
        } else {
            cbEnglish.setBackgroundResource(R.mipmap.icon_check_close);
            cbChinese.setBackgroundResource(R.mipmap.icon_check_open);
        }
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }


    @OnClick({R.id.cb_chinese, R.id.cb_english})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_chinese:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    LanguageUtil.changeAppLanguage(MyApplication.getContext(), LanguageType.CHINESE.getLanguage());
                }
                LanguageUtil.changeAppLanguage(MyApplication.getContext(), LanguageType.CHINESE.getLanguage());
                MMKVUtil.getInstance().putString(MMKVUtil.CURRENT_APP_LANGUAGE, LanguageType.CHINESE.getLanguage());
                redirectActivity(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                break;
            case R.id.cb_english:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    LanguageUtil.changeAppLanguage(MyApplication.getContext(), LanguageType.ENGLISH.getLanguage());
                }
                LanguageUtil.changeAppLanguage(MyApplication.getContext(), LanguageType.ENGLISH.getLanguage());
                MMKVUtil.getInstance().putString(MMKVUtil.CURRENT_APP_LANGUAGE, LanguageType.ENGLISH.getLanguage());
                redirectActivity(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                break;
        }
    }
}
