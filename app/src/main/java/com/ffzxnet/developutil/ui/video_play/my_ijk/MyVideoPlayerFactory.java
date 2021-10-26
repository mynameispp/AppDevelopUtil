package com.ffzxnet.developutil.ui.video_play.my_ijk;

import android.content.Context;

import xyz.doikki.videoplayer.player.PlayerFactory;

public class MyVideoPlayerFactory extends PlayerFactory<MyIjkPlayer> {
    public static MyVideoPlayerFactory create() {
        return new MyVideoPlayerFactory();
    }

    @Override
    public MyIjkPlayer createPlayer(Context context) {
        //返回自定义的IjkPlayer
        return new MyIjkPlayer(context);
    }
}
