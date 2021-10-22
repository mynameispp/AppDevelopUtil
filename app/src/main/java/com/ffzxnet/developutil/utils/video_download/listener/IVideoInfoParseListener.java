package com.ffzxnet.developutil.utils.video_download.listener;

import com.ffzxnet.developutil.utils.video_download.m3u8.M3U8;
import com.ffzxnet.developutil.utils.video_download.model.VideoTaskItem;

public interface IVideoInfoParseListener {

    void onM3U8FileParseSuccess(VideoTaskItem info, M3U8 m3u8);

    void onM3U8FileParseFailed(VideoTaskItem info, Throwable error);
}
