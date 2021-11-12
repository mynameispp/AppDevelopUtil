package com.ffzxnet.developutil.base.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.ui.for_result_activity.ForResultActivity;
import com.ffzxnet.developutil.ui.scancode.activity.BaseCaptureActivity;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BaseActivityResultContact extends ActivityResultContract<Intent, Object> {
    public static final int Code1 = 10001;

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Intent input) {
        return input;
    }

    @Override
    public Object parseResult(int resultCode, @Nullable Intent intent) {
        //回调返回值
        if (resultCode == Code1 && null != intent) {
            //首页StarActivityForResult测试界面
            return intent.getStringExtra(MyConstans.KEY_DATA);
        } else if (resultCode == BaseCaptureActivity.RESULT_CODE_QR_SCAN
                && intent != null && intent.getExtras() != null) {
            //扫描二维码
            return intent.getExtras().getString(BaseCaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
        } else if (resultCode == Activity.RESULT_OK){
            return intent;
        }else {
            return intent;
        }
    }
}
