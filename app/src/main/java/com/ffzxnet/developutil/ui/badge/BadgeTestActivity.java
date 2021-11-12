package com.ffzxnet.developutil.ui.badge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.utils.badge_notice.BadgeService;

import butterknife.OnClick;

public class BadgeTestActivity extends BaseActivity {

    private BadgeServiceConnection badgeServiceonnection;
    private BadgeService.BadgeServiceBind badgeServiceBind;

    private int testNumber = 1;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_badge_test;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "桌面角标测试");
        badgeServiceonnection = new BadgeServiceConnection();
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    @OnClick({R.id.badge_test_star, R.id.badge_test_add, R.id.badge_test_clear, R.id.badge_test_auto_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.badge_test_star:
                bindService(new Intent(this, BadgeService.class), badgeServiceonnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.badge_test_add:
                if (badgeServiceBind != null) {
                    badgeServiceBind.setNoticeBadgeNumber(testNumber++);
                }
                break;
            case R.id.badge_test_clear:
                if (badgeServiceBind != null) {
                    badgeServiceBind.setNoticeBadgeNumber(0);
                }
                break;
            case R.id.badge_test_auto_test:
                if (badgeServiceBind != null) {
                    badgeServiceBind.starAutoTest();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (badgeServiceonnection != null && badgeServiceBind != null) {
            unbindService(badgeServiceonnection);
        }
    }

    class BadgeServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            badgeServiceBind = (BadgeService.BadgeServiceBind) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }
}
