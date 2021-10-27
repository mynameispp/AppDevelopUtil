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

    /**
     * 设置IjkMediaPlayer.OPT_CATEGORY_PLAYER相关配置
     */
    public void setPlayerOption(String name, String value) {
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, name, value);
    }

    /**
     * 设置IjkMediaPlayer.OPT_CATEGORY_PLAYER相关配置
     */
    public void setPlayerOption(String name, long value) {
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, name, value);
    }

    /**
     * 设置IjkMediaPlayer.OPT_CATEGORY_FORMAT相关配置
     */
    public void setFormatOption(String name, String value) {
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, name, value);
    }

    /**
     * 设置IjkMediaPlayer.OPT_CATEGORY_FORMAT相关配置
     */
    public void setFormatOption(String name, long value) {
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, name, value);
    }

    /**
     * 设置IjkMediaPlayer.OPT_CATEGORY_CODEC相关配置
     */
    public void setCodecOption(String name, String value) {
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, name, value);
    }

    /**
     * 设置IjkMediaPlayer.OPT_CATEGORY_CODEC相关配置
     */
    public void setCodecOption(String name, long value) {
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, name, value);
    }

    /**
     * 设置IjkMediaPlayer.OPT_CATEGORY_SWS相关配置
     */
    public void setSwsOption(String name, String value) {
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_SWS, name, value);
    }

    /**
     * 设置IjkMediaPlayer.OPT_CATEGORY_SWS相关配置
     */
    public void setSwsOption(String name, long value) {
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_SWS, name, value);
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
