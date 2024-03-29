package com.ffzxnet.developutil.utils.video_download.listener;

import com.ffzxnet.developutil.utils.video_download.model.VideoTaskItem;

public interface IDownloadListener {

    void onDownloadDefault(VideoTaskItem item);

    void onDownloadPending(VideoTaskItem item);

    void onDownloadPrepare(VideoTaskItem item);

    void onDownloadStart(VideoTaskItem item);

    void onDownloadProgress(VideoTaskItem item);

    void onDownloadSpeed(VideoTaskItem item);

    void onDownloadPause(VideoTaskItem item);

    void onDownloadError(VideoTaskItem item);

    void onDownloadSuccess(VideoTaskItem item);
}
