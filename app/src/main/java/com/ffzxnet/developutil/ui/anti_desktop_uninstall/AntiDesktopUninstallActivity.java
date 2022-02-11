package com.ffzxnet.developutil.ui.anti_desktop_uninstall;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;

import butterknife.OnClick;

public class AntiDesktopUninstallActivity extends BaseActivity {
    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_anti_desktop_uninstall;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "获取设备管理权限");
        //https://www.jianshu.com/p/22a708c74c1e 防止应用被杀死
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    @OnClick({R.id.anit_desktop_uninstall_go_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.anit_desktop_uninstall_go_setting:
                //进入设备管理器界面
                Intent intent = new Intent();
                ComponentName cm = new ComponentName("com.android.settings",
                        "com.android.settings.DeviceAdminSettings");
                intent.setComponent(cm);
                intent.setAction("android.intent.action.VIEW");
                startActivity(intent);
                break;
        }
    }
}
