package com.ffzxnet.developutil.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ffzxnet.developutil.evenbus.MyEventbus;
import com.ffzxnet.developutil.utils.video_download.VideoDownloadManager;
import com.ffzxnet.developutil.utils.video_download.listener.DownloadListener;
import com.ffzxnet.developutil.utils.video_download.model.VideoTaskItem;
import com.ffzxnet.developutil.utils.video_download.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;

public class DownLoadingService extends Service {
    private boolean isListen = false;

    @Override
    public void onCreate() {
        super.onCreate();
        VideoDownloadManager.getInstance().setGlobalDownloadListener(mDownloadListener);
        LogUtils.e("下载通知统一处理通知", "开启监听: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        isListen = true;
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isListen = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VideoDownloadManager.getInstance().setGlobalDownloadListener(mDownloadListener);
        LogUtils.e("下载通知统一处理通知", "关闭监听: ");
    }

    private DownloadListener mDownloadListener = new DownloadListener() {
        private long mLastProgressTimeStamp;

        @Override
        public void onDownloadDefault(VideoTaskItem item) {
            LogUtils.e("下载通知统一处理通知", "onDownloadDefault: " + item.getTitle());
            postDownloadingStatus(item);
        }

        @Override
        public void onDownloadPending(VideoTaskItem item) {
            LogUtils.e("下载通知统一处理通知", "onDownloadPending: " + item.getFileName());
            postDownloadingStatus(item);
        }

        @Override
        public void onDownloadPrepare(VideoTaskItem item) {
            LogUtils.e("下载通知统一处理通知", "onDownloadPrepare: " + item.getFileName());
            postDownloadingStatus(item);
        }

        @Override
        public void onDownloadStart(VideoTaskItem item) {
            LogUtils.e("下载通知统一处理通知", "onDownloadStart: " + item.getFileName());
            postDownloadingStatus(item);
        }

        @Override
        public void onDownloadProgress(VideoTaskItem item) {
            long currentTimeStamp = System.currentTimeMillis();
            if (currentTimeStamp - mLastProgressTimeStamp > 600) {
            LogUtils.e("下载通知统一处理通知", "onDownloadProgress: " + item.getFileName());
            postDownloadingStatus(item);
            mLastProgressTimeStamp = currentTimeStamp;
            }
        }

        @Override
        public void onDownloadSpeed(VideoTaskItem item) {
            LogUtils.e("下载通知统一处理通知", "onDownloadSpeed: " + item.getFileName());
            long currentTimeStamp = System.currentTimeMillis();
            if (currentTimeStamp - mLastProgressTimeStamp > 600) {
            postDownloadingStatus(item);
            mLastProgressTimeStamp = currentTimeStamp;
            }
        }

        @Override
        public void onDownloadPause(VideoTaskItem item) {
            LogUtils.e("下载通知统一处理通知", "onDownloadPause: " + item.getFileName());
            postDownloadingStatus(item);
        }

        @Override
        public void onDownloadError(VideoTaskItem item) {
            LogUtils.e("下载通知统一处理通知", "onDownloadError: " + item.getFileName());
            postDownloadingStatus(item);
        }

        @Override
        public void onDownloadSuccess(VideoTaskItem item) {
            LogUtils.e("下载通知统一处理通知", "onDownloadSuccess: " + item.getFileName());
            postDownloadingStatus(item);
        }
    };

    private void postDownloadingStatus(VideoTaskItem item) {
        LogUtils.e("下载通知统一处理通知", "是否通知: " + isListen);
        if (!isListen) {
            return;
        }
        EventBus.getDefault().post(new MyEventbus.DownloadingEven(item));
    }
}
