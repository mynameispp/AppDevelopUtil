package com.ffzxnet.developutil.ui.video_play.my_ijk;

import android.content.Context;
import android.util.AttributeSet;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import xyz.doikki.videoplayer.player.VideoView;

public class MyVideoView extends VideoView<MyIjkPlayer> {

    private HashMap<String, Object> mPlayerOptions = new HashMap<>();
    private HashMap<String, Object> mFormatOptions = new HashMap<>();
    private HashMap<String, Object> mCodecOptions = new HashMap<>();
    private HashMap<String, Object> mSwsOptions = new HashMap<>();

    public MyVideoView(@NonNull Context context) {
        super(context);
    }

    public MyVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
    //播放设置
    {
        //某些视频在SeekTo的时候，会跳回到拖动前的位置，这是因为视频的关键帧的问题，通俗一点就是FFMPEG不兼容，视频压缩过于厉害，seek只支持关键帧，出现这个情况就是原始的视频文件中i 帧比较少
        addFormatOption("enable-accurate-seek", 1);
        //播放前的探测Size，默认是1M, 改小一点会出画面更快
        addFormatOption("probesize", 1024 * 6);
        // 跳过循环滤波
        addFormatOption("skip_loop_filter", 48);
        // 设置最长分析时长
        addFormatOption("analyzemaxduration", 100L);
        // 通过立即清理数据包来减少等待时长
        addFormatOption("flush_packets", 1L);
        // 暂停输出直到停止后读取足够的数据包
        addFormatOption("packet-buffering", 0L);
        // 网络不好的情况下进行丢包
        addFormatOption("framedrop", 1L);
        // 去掉音频
        addFormatOption("an", 1);
        // 不查询stream_info，直接使用
        addFormatOption("find_stream_info", 0);
        // 等待开始之后才绘制
        addFormatOption("render-wait-start", 1);
    }

    public void getTrackInfo() {
        //获取音轨信息
        mMediaPlayer.getTrackInfo();
    }

    public void setStrack(int trackIndex) {
        //切换音轨。这里只写了切换音轨可能会有问题。
        mMediaPlayer.setStrack(trackIndex);
    }

    @Override
    protected void setOptions() {
        super.setOptions();
        for (Map.Entry<String, Object> next : mPlayerOptions.entrySet()) {
            String key = next.getKey();
            Object value = next.getValue();
            if (value instanceof String) {
                mMediaPlayer.setPlayerOption(key, (String) value);
            } else if (value instanceof Long) {
                mMediaPlayer.setPlayerOption(key, (Long) value);
            }
        }
        for (Map.Entry<String, Object> next : mFormatOptions.entrySet()) {
            String key = next.getKey();
            Object value = next.getValue();
            if (value instanceof String) {
                mMediaPlayer.setFormatOption(key, (String) value);
            } else if (value instanceof Long) {
                mMediaPlayer.setFormatOption(key, (Long) value);
            }
        }
        for (Map.Entry<String, Object> next : mCodecOptions.entrySet()) {
            String key = next.getKey();
            Object value = next.getValue();
            if (value instanceof String) {
                mMediaPlayer.setCodecOption(key, (String) value);
            } else if (value instanceof Long) {
                mMediaPlayer.setCodecOption(key, (Long) value);
            }
        }
        for (Map.Entry<String, Object> next : mSwsOptions.entrySet()) {
            String key = next.getKey();
            Object value = next.getValue();
            if (value instanceof String) {
                mMediaPlayer.setSwsOption(key, (String) value);
            } else if (value instanceof Long) {
                mMediaPlayer.setSwsOption(key, (Long) value);
            }
        }
    }


    /**
     * 开启硬解
     */
    public void setEnableMediaCodec(boolean isEnable) {
        int value = isEnable ? 1 : 0;
        addPlayerOption("mediacodec-all-videos", value);
        addPlayerOption("mediacodec-sync", value);
        addPlayerOption("mediacodec-auto-rotate", value);
        addPlayerOption("mediacodec-handle-resolution-change", value);
    }

    /**
     * 开启精准seek，可以解决由于视频关键帧较少导致的seek不准确问题
     */
    public void setEnableAccurateSeek(boolean isEnable) {
        addPlayerOption("enable-accurate-seek", isEnable ? 1 : 0);
    }


    public void addPlayerOption(String name, String value) {
        mPlayerOptions.put(name, value);
    }

    public void addPlayerOption(String name, long value) {
        mPlayerOptions.put(name, value);
    }


    public void addFormatOption(String name, String value) {
        mFormatOptions.put(name, value);
    }

    public void addFormatOption(String name, long value) {
        mFormatOptions.put(name, value);
    }


    public void addCodecOption(String name, String value) {
        mCodecOptions.put(name, value);
    }

    public void addCodecOption(String name, long value) {
        mCodecOptions.put(name, value);
    }


    public void addSwsOption(String name, String value) {
        mSwsOptions.put(name, value);
    }

    public void addSwsOption(String name, long value) {
        mSwsOptions.put(name, value);
    }

    @Override
    protected void setInitOptions() {
        super.setInitOptions();
        if (mCurrentPosition > 0) {
            addPlayerOption("seek-at-start", mCurrentPosition);
        }
    }

    @Override
    public void onPrepared() {
        setPlayState(STATE_PREPARED);
    }


    @Override
    public void skipPositionWhenPlay(int position) {
        addPlayerOption("seek-at-start", position);
    }
}
