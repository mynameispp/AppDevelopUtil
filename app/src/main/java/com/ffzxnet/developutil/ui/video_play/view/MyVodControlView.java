package com.ffzxnet.developutil.ui.video_play.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.ffzxnet.developutil.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import xyz.doikki.videocontroller.component.VodControlView;
import xyz.doikki.videoplayer.player.VideoView;

public class MyVodControlView extends VodControlView {
    private TextView mAnthology;//选集按钮
    private TextView mNextVideo;//下一集
    private RecyclerView mAnthologyRv;//选集数据

    {
        mAnthology = findViewById(R.id.anthology_videos_btn);
        mAnthology.setOnClickListener(this);
        mNextVideo = findViewById(R.id.next_video_btn);
        mAnthologyRv = findViewById(R.id.anthology_videos_rv);
    }

    public MyVodControlView(@NonNull Context context) {
        super(context);
    }

    public MyVodControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVodControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.my_dkplayer_layout_vod_control_view;
    }

    /**
     * 设置选集参数
     * @param layoutManager 排版
     * @param adapter 数据
     */
    public void setAnthologyVidesAdapter(RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mAnthologyRv.setLayoutManager(layoutManager);
            mAnthologyRv.setAdapter(adapter);
            if (adapter.getItemCount() > 1) {
                mAnthology.setVisibility(VISIBLE);
                mNextVideo.setVisibility(VISIBLE);
            } else {
                mAnthology.setVisibility(GONE);
                mAnthology.setVisibility(GONE);
            }
        }
    }

    /**
     * 设置点击下一集监听
     * @param onClickListener
     */
    public void setNextVideoListen(OnClickListener onClickListener){
        mNextVideo.setOnClickListener(onClickListener);
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {
        super.onVisibilityChanged(isVisible, anim);
        if (!isVisible){
            mAnthologyRv.setVisibility(GONE);
        }
    }

    @Override
    public void onPlayStateChanged(int playState) {
        super.onPlayStateChanged(playState);
        switch (playState) {
            case VideoView.PLAYER_NORMAL:
                mNextVideo.setVisibility(GONE);
                mAnthology.setVisibility(GONE);
                break;
            case VideoView.PLAYER_FULL_SCREEN:
                mNextVideo.setVisibility(VISIBLE);
                mAnthology.setVisibility(VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        //显示或隐藏选集
        if (v.getId() == R.id.anthology_videos_btn) {
            if (mAnthologyRv.getVisibility() == GONE) {
                mAnthologyRv.setVisibility(VISIBLE);
                mControlWrapper.stopFadeOut();
            } else {
                mAnthologyRv.setVisibility(GONE);
                mControlWrapper.startFadeOut();
            }
        }
    }
}
