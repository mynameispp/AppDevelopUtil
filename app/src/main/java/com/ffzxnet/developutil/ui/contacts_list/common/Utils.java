package com.ffzxnet.developutil.ui.contacts_list.common;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Utils {

    public static boolean checkHasPermission(Activity activity, String permission, int requestCode) {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity,
                permission)) {
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission)) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},
                        requestCode);
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},
                        requestCode);
            }
        }
        return false;
    }
}
