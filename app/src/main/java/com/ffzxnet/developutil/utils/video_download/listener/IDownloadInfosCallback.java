package com.ffzxnet.developutil.utils.video_download.listener;

import com.ffzxnet.developutil.utils.video_download.model.VideoTaskItem;

import java.util.List;

public interface IDownloadInfosCallback {

    void onDownloadInfos(List<VideoTaskItem> items);
}
