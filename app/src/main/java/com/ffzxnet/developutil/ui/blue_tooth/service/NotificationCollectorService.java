package com.ffzxnet.developutil.ui.blue_tooth.service;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.ffzxnet.developutil.utils.tools.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class NotificationCollectorService extends NotificationListenerService {
    List<String> noticeHistory = new ArrayList<>();

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        LogUtil.e("NotificationCollectorService", "open" + "-----" + sbn.getPackageName());
        LogUtil.e("NotificationCollectorService", "open" + "------" + sbn.getNotification().tickerText);
        LogUtil.e("NotificationCollectorService", "open" + "-----" + sbn.getNotification().extras.get("android.title"));
        LogUtil.e("NotificationCollectorService", "open" + "-----" + sbn.getNotification().extras.get("android.text"));
        noticeHistory.add(sbn.getNotification().extras.get("android.title") + "=" + sbn.getNotification().extras.get("android.text"));
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        LogUtil.e("NotificationCollectorService", "remove" + "-----" + sbn.getPackageName());
        for (String s : noticeHistory) {
            LogUtil.e("NotificationCollectorService", "history" + "-----" + s);
        }
    }
}
