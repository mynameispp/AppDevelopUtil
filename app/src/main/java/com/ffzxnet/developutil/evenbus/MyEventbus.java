package com.ffzxnet.developutil.evenbus;

import com.ffzxnet.developutil.utils.video_download.model.VideoTaskItem;

import org.greenrobot.eventbus.EventBus;

public class MyEventbus {
    //下载状态通知
    public static class DownloadingEven extends EventBus {
        private VideoTaskItem videoTaskItem;

        public DownloadingEven(VideoTaskItem videoTaskItem) {
            this.videoTaskItem = videoTaskItem;
        }

        public VideoTaskItem getVideoTaskItem() {
            return videoTaskItem;
        }

        public void setVideoTaskItem(VideoTaskItem videoTaskItem) {
            this.videoTaskItem = videoTaskItem;
        }
    }

    //有电话
    public static class InCallEvent {
    }
}
