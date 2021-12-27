package com.ffzxnet.developutil.ui.unlock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.ui.unlock.code.GestureLockActivity;
import com.ffzxnet.developutil.ui.unlock.code.LoginSettingActivity;
import com.ffzxnet.developutil.ui.unlock.code.language.LanguageSettingActivity;
import com.ffzxnet.developutil.utils.tools.MMKVUtil;
import com.ffzxnet.developutil.utils.ui.ToastUtil;
import com.ffzxnet.developutil.utils.ui.titanic.Typefaces;

import butterknife.BindView;
import butterknife.OnClick;

//小控件
public class UnlockActivity extends BaseActivity {

    @BindView(R.id.titanic_text)
    TextView titanicText;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_small_tool;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "解锁");
        // set fancy typeface
        titanicText.setTypeface(Typefaces.get(MyApplication.getContext(), "Satisfy-Regular.ttf"));
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    @OnClick({R.id.moreLanguageBtn, R.id.fingerprintLoginBtn, R.id.loginBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.moreLanguageBtn:
                redirectActivity(LanguageSettingActivity.class);
                break;
            case R.id.fingerprintLoginBtn:
                redirectActivity(LoginSettingActivity.class);
                break;

            case R.id.loginBtn:
                Intent intent = null;
                boolean isFingerprint = MMKVUtil.getInstance().getBoolean(MMKVUtil.ISFINGERPRINT_KEY, false);
                boolean isGesture = MMKVUtil.getInstance().getBoolean(MMKVUtil.ISGESTURELOCK_KEY, false);
                if (!isFingerprint && !isGesture) {
                    ToastUtil.showToastShort("请先设置指纹解锁或手势解锁！");
                } else {
                    intent = new Intent(this, GestureLockActivity.class);
                    intent.putExtra("type", "login");
                    startActivity(intent);
                }
                break;
        }
    }

}
