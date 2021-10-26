package com.ffzxnet.developutil.ui.video_play.my_ijk;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import xyz.doikki.videoplayer.player.VideoView;

public class MyVideoView extends VideoView<MyIjkPlayer> {
    public MyVideoView(@NonNull Context context) {
        super(context);
    }

    public MyVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void getTrackInfo(){
        //获取音轨信息
        mMediaPlayer.getTrackInfo();
    }
    public void setStrack(int trackIndex) {
        //切换音轨。这里只写了切换音轨可能会有问题。
        mMediaPlayer.setStrack(trackIndex);
    }

    public void setMediaPlayerOption() {
        mMediaPlayer.setMediaPlayerOption();
    }
}
