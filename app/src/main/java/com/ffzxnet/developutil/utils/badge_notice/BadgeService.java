package com.ffzxnet.developutil.utils.badge_notice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

//角标服务
public class BadgeService extends Service {
    IconBadgeNumManager setIconBadgeNumManager;
    private boolean isStop = true;
    private int count;
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (!isStop) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count += 2;
                if (count == 20) {
                    sendIconNumNotification(0);
                    return;
                }
                sendIconNumNotification(count);
            }
        }
    });

    public class BadgeServiceBind extends Binder {
        public BadgeService getService() {
            return BadgeService.this;
        }

        public void setNoticeBadgeNumber(int number) {
            sendIconNumNotification(number);
        }

        //测试用
        public void starAutoTest() {
            autoTest();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setIconBadgeNumManager = new IconBadgeNumManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        isStop = false;
//        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStop = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new BadgeServiceBind();
    }

    //测试用
    private void autoTest() {
        if (isStop) {
            isStop = false;
            thread.start();
        }
    }

    //发送角标数字
    private void sendIconNumNotification(int number) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm == null) return;
        String notificationChannelId = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = createNotificationChannel();
            nm.createNotificationChannel(notificationChannel);
            notificationChannelId = notificationChannel.getId();
        }
        Notification notification = null;
        try {
                notification = new NotificationCompat.Builder(this, notificationChannelId)
//                        .setSmallIcon(getApplicationInfo().icon)
//                        .setWhen(System.currentTimeMillis())
//                        .setContentTitle("title")
//                        .setContentText("content num: " + number)
//                        .setTicker("ticker")
//                        .setAutoCancel(true)
//                        .setNumber(number)
                        .build();
            notification = setIconBadgeNumManager.setIconBadgeNum(getApplication(), notification, number);

            nm.notify(32154, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static NotificationChannel createNotificationChannel() {
        String channelId = "test";
        NotificationChannel channel = null;
        channel = new NotificationChannel(channelId,
                "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true); //是否在桌面icon右上角展示小红点
        channel.setLightColor(Color.RED); //小红点颜色
        channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
        return channel;
    }
}
