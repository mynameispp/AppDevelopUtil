package com.ffzxnet.developutil.ui.video_download.bean;

import com.ffzxnet.developutil.utils.video_download.model.VideoTaskItem;

import java.io.Serializable;

import androidx.annotation.Nullable;

public class DownloadVideoInfoBean implements Serializable {
    private String videoId;
    private String videoName;//视频名称
    private String subjectName;//分集名称
    private String videoImage;
    private String videoSize;
    private int videoCount;//有多少集
    private long lastPlayTime;//上次播放到哪
    private long videoTime;//视频时长
    private VideoTaskItem downLoadUrl;
    private String speed;
    private int downLoadProgress;
    private boolean isSelect;

    public VideoTaskItem getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(VideoTaskItem downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public int getDownLoadProgress() {
        return downLoadProgress;
    }

    public void setDownLoadProgress(int downLoadProgress) {
        this.downLoadProgress = downLoadProgress;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }

    public String getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(String videoSize) {
        this.videoSize = videoSize;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }

    public long getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(long lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }

    public long getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(long videoTime) {
        this.videoTime = videoTime;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        DownloadVideoInfoBean otherIbj = (DownloadVideoInfoBean) obj;
        return otherIbj.getVideoId().equals(videoId);
    }
}
