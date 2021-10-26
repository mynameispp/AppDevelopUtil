package com.ffzxnet.developutil.ui.video_play.my_ijk;

import android.content.Context;
import android.util.Log;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;
import tv.danmaku.ijk.media.player.misc.IjkTrackInfo;
import xyz.doikki.videoplayer.ijk.IjkPlayer;

public class MyIjkPlayer extends IjkPlayer {
    public MyIjkPlayer(Context context) {
        super(context);
    }

    public void setMediaPlayerOption(){
        //某些视频在SeekTo的时候，会跳回到拖动前的位置，这是因为视频的关键帧的问题，通俗一点就是FFMPEG不兼容，视频压缩过于厉害，seek只支持关键帧，出现这个情况就是原始的视频文件中i 帧比较少
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        //播放前的探测Size，默认是1M, 改小一点会出画面更快
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 1024 * 6);

        // 跳过循环滤波
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        // 设置最长分析时长
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100L);
        // 通过立即清理数据包来减少等待时长
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1L);
        // 暂停输出直到停止后读取足够的数据包
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0L);
        // 网络不好的情况下进行丢包
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1L);
        // 去掉音频
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "an", 1);
        // 不查询stream_info，直接使用
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"find_stream_info", 0);
        // 等待开始之后才绘制
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "render-wait-start", 1);
    }

    public void getTrackInfo() {
        //默认音轨
        int defaultAudio = mMediaPlayer.getSelectedTrack(ITrackInfo.MEDIA_TRACK_TYPE_AUDIO);
        Log.e("dddddddddd", "=====默认音轨====" + defaultAudio);
        IjkTrackInfo[] tackInfo = mMediaPlayer.getTrackInfo();
        for (IjkTrackInfo ijkTrackInfo : tackInfo) {
            Log.e("dddddddddd", String.format("=====音轨信息====" + ijkTrackInfo.toString()));
        }
    }

    public void setStrack(int trackIndex) {
        //切换音轨。这里只写了切换音轨可能会有问题。
        mMediaPlayer.selectTrack(trackIndex);
    }
}
