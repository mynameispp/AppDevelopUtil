package com.ffzxnet.developutil.base.ui;

import android.content.Context;
import android.content.Intent;

import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.ui.scancode.activity.BaseCaptureActivity;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BaseActivityResultContact extends ActivityResultContract<Intent, Object> {
    public static final int ResultCode_Code1 = 10001;
    public static final String ResultCode_Default = "ResultCode_Default";

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Intent input) {
        return input;
    }

    @Override
    public Object parseResult(int resultCode, @Nullable Intent intent) {
        //回调返回值
        switch (resultCode) {
            case ResultCode_Code1:
                //首页StarActivityForResult测试界面
                if (null != intent) {
                    return intent.getStringExtra(MyConstans.KEY_DATA);
                }
                break;
            case BaseCaptureActivity.RESULT_CODE_QR_SCAN:
                //扫描二维码
                if (intent != null && intent.getExtras() != null) {
                    return intent.getExtras().getString(BaseCaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
                }
                break;
        }

        if (intent != null) {
            intent.putExtra(ResultCode_Default, resultCode);
        }

        return intent;
    }
}
