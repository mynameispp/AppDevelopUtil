package com.ffzxnet.developutil.utils.badge_notice.badge_number;

import android.app.Application;
import android.app.Notification;
import androidx.annotation.NonNull;

public class OPPOModelImpl implements IconBadgeNumModel {
    private static final String NOTIFICATION_ERROR = "not support : oppo";

    @Override
    public Notification setIconBadgeNum(@NonNull Application context, Notification notification, int count) throws Exception {
        if (true) {
            throw new Exception(NOTIFICATION_ERROR);
        }
        return null;
    }
}
