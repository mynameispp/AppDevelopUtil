package com.ffzxnet.developutil.ui.anti_desktop_uninstall;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

public class MyAdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        super.onEnabled(context, intent);
    }

    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
    }
}
