package com.ffzxnet.developutil.ui.unlock.code;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.base.ui.BaseActivityResultContact;
import com.ffzxnet.developutil.ui.unlock.code.fingerprint.FingerprintDialogFragment;
import com.ffzxnet.developutil.ui.unlock.code.gesture.SetGestureLockActivity;
import com.ffzxnet.developutil.utils.tools.DeviceUtil;
import com.ffzxnet.developutil.utils.tools.MMKVUtil;
import com.ffzxnet.developutil.utils.ui.ToastUtil;

import javax.crypto.Cipher;

import androidx.activity.result.ActivityResultCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

public class LoginSettingActivity extends BaseActivity {

    @BindView(R.id.changeGesture)
    TextView changeGesture;
    @BindView(R.id.fingerprintImg)
    ImageView fingerprintImg;
    @BindView(R.id.gestureImg)
    ImageView gestureImg;
    @BindView(R.id.fingerprintCL)
    ConstraintLayout fingerprintCL;
    @BindView(R.id.gestureLockCL)
    ConstraintLayout gestureLockCL;
    @BindView(R.id.fingerprintTV)
    TextView fingerprintTV;

    private final int SETGESTURELOCK = 100;
    private Context mContext;
    private Boolean isFingerprint, isGesture;
    private Cipher cipher;
    private FingerprintDialogFragment dialogFragment;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_login_setting;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "设置手势或者指纹验证");
        initView();

        redirectActivityForResult(new BaseActivityResultContact(), new ActivityResultCallback() {
            @Override
            public void onActivityResult(Object result) {
                //设置手势
                if (result != null) {
                    isGesture = true;
                    gestureImg.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_check_open));
                    changeGesture.setVisibility(View.VISIBLE);
                    MMKVUtil.getInstance().putBoolean(MMKVUtil.ISGESTURELOCK_KEY, true);
                }
            }
        });
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    private void initView() {
        mContext = this;
        isFingerprint = MMKVUtil.getInstance().getBoolean(MMKVUtil.ISFINGERPRINT_KEY, false);
        isGesture = MMKVUtil.getInstance().getBoolean(MMKVUtil.ISGESTURELOCK_KEY, false);

        if (isGesture) {
            gestureImg.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_check_open));
            changeGesture.setVisibility(View.VISIBLE);
        }
        //判断是否支持指纹
        if (DeviceUtil.supportFingerprint(MyApplication.getContext())) {
            /**
             * 生成一个对称加密的key
             */
            DeviceUtil.initKey();

            /**
             * 生成一个Cipher对象
             */
            cipher = DeviceUtil.initCipher();
            if (isFingerprint) {
                fingerprintImg.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_check_open));
                fingerprintTV.setVisibility(View.VISIBLE);
            }
        } else {
            fingerprintCL.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.fingerprintImg, R.id.gestureImg, R.id.changeGesture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fingerprintImg:
                if (isFingerprint) {
                    showDeleteDialog();
                } else {
                    showFingerPrintDialog(cipher);
                }
                break;

            case R.id.gestureImg:
                if (isGesture) {
                    gestureImg.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_check_close));
                    MMKVUtil.getInstance().putBoolean(MMKVUtil.ISGESTURELOCK_KEY, false);
                    changeGesture.setVisibility(View.GONE);
                    isGesture = false;
                } else {
                    resultLauncher.launch(new Intent(this, SetGestureLockActivity.class));
                }
                break;

            case R.id.changeGesture:
                Intent intent = new Intent(mContext, GestureLockActivity.class);
                intent.putExtra("type", "change");
                startActivity(intent);
                break;
        }
    }


    private void showFingerPrintDialog(Cipher cipher) {
        dialogFragment = new FingerprintDialogFragment();
        dialogFragment.setCipher(cipher);
        dialogFragment.show(getSupportFragmentManager(), "fingerprint");

        dialogFragment.setOnFingerprintSetting(new FingerprintDialogFragment.OnFingerprintSetting() {
            @Override
            public void onFingerprint(boolean isSucceed) {
                if (isSucceed) {
                    isFingerprint = true;
                    MMKVUtil.getInstance().putBoolean(MMKVUtil.ISFINGERPRINT_KEY, true);
                    ToastUtil.showToastShort("指纹设置成功！");
                    fingerprintImg.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_check_open));
                    fingerprintTV.setVisibility(View.VISIBLE);
                } else {
                    ToastUtil.showToastShort("指纹设置失败！");
                }
            }
        });
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否关闭指纹登录？");
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fingerprintImg.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_check_close));
                fingerprintTV.setVisibility(View.GONE);
                changeGesture.setVisibility(View.GONE);
                isFingerprint = false;
                MMKVUtil.getInstance().putBoolean(MMKVUtil.ISFINGERPRINT_KEY, false);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

}
