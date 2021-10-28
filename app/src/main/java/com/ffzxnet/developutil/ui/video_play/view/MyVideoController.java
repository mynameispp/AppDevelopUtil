package com.ffzxnet.developutil.ui.video_play.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.GlideApp;
import com.ffzxnet.developutil.ui.video_play.view.danmu.DanmakuBaseBean;
import com.ffzxnet.developutil.ui.video_play.view.danmu.MyDanmakuView;

import java.util.List;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import xyz.doikki.videocontroller.component.ErrorView;
import xyz.doikki.videocontroller.component.GestureView;
import xyz.doikki.videocontroller.component.LiveControlView;
import xyz.doikki.videocontroller.component.PrepareView;
import xyz.doikki.videocontroller.component.TitleView;
import xyz.doikki.videoplayer.controller.GestureVideoController;
import xyz.doikki.videoplayer.player.VideoView;
import xyz.doikki.videoplayer.util.PlayerUtils;

/**
 * 直播/点播控制器
 */

public class MyVideoController extends GestureVideoController implements View.OnClickListener {

    protected ImageView mLockButton;
    protected ProgressBar mLoadingProgress;
    private MyDanmakuView mMyDanmakuView;//弹幕

    public MyVideoController(@NonNull Context context) {
        this(context, null);
    }

    public MyVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyVideoController(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.my_dkplayer_layout_video_controller;
    }

    @Override
    protected void initView() {
        super.initView();
        mLockButton = findViewById(R.id.lock);
        mLockButton.setOnClickListener(this);
        mLoadingProgress = findViewById(R.id.loading);
    }

    /**
     * 快速添加各个组件
     *
     * @param title  标题
     * @param isLive 是否为直播
     */
    public void addDefaultControlComponent(String title, boolean isLive) {
        addDefaultControlComponent(title, isLive, null, null, null);
    }

    /**
     * 快速添加各个组件
     *
     * @param title                    标题
     * @param isLive                   是否为直播
     * @param nextAnthologyClickListen 下一集按钮监听
     * @param layoutManager            选集排列样式
     * @param anthologyAdapter         选集数据适配器
     */
    public void addDefaultControlComponent(String title, boolean isLive
            , View.OnClickListener nextAnthologyClickListen, RecyclerView.LayoutManager layoutManager
            , RecyclerView.Adapter anthologyAdapter) {
        //播放器顶部标题
        TitleView titleView = new TitleView(getContext());
        titleView.setTitle(title);
        //播放器底部控制控件
        MyVodControlView vodControlView = new MyVodControlView(getContext());//点播控制条
        vodControlView.showBottomProgress(false);//是否显示底部进度条。默认显示
        if (nextAnthologyClickListen != null) {
            //下一集点击事件
            vodControlView.setNextVideoListen(nextAnthologyClickListen);
            //选集信息
            vodControlView.setAnthologyVideosAdapter(layoutManager, anthologyAdapter);
        }
        //竖屏也开启手势操作，默认关闭
        setEnableInNormal(true);
        //设置弹幕
        mMyDanmakuView = new MyDanmakuView(getContext());
        //设置错误页面
        ErrorView errorView = new ErrorView(getContext());
        //视频封面
        PrepareView prepareView = new PrepareView(getContext());
        prepareView.setClickStart();
        ImageView thumb = prepareView.findViewById(R.id.thumb);
        GlideApp.with(thumb)
                .load("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF")
                .into(thumb);
        addControlComponent(mMyDanmakuView, errorView, prepareView, titleView);
        if (isLive) {
            addControlComponent(new LiveControlView(getContext()));
        } else {
            addControlComponent(vodControlView);
        }
        addControlComponent(new GestureView(getContext()));
        setCanChangePosition(!isLive);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.lock) {
            mControlWrapper.toggleLockState();
        }
    }

    @Override
    protected void onLockStateChanged(boolean isLocked) {
        if (isLocked) {
            mLockButton.setSelected(true);
            Toast.makeText(getContext(), R.string.dkplayer_locked, Toast.LENGTH_SHORT).show();
        } else {
            mLockButton.setSelected(false);
            Toast.makeText(getContext(), R.string.dkplayer_unlocked, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onVisibilityChanged(boolean isVisible, Animation anim) {
        if (mControlWrapper.isFullScreen()) {
            if (isVisible) {
                if (mLockButton.getVisibility() == GONE) {
                    mLockButton.setVisibility(VISIBLE);
                    if (anim != null) {
                        mLockButton.startAnimation(anim);
                    }
                }
            } else {
                mLockButton.setVisibility(GONE);
                if (anim != null) {
                    mLockButton.startAnimation(anim);
                }
            }
        }
    }

    @Override
    protected void onPlayerStateChanged(int playerState) {
        super.onPlayerStateChanged(playerState);
        switch (playerState) {
            case VideoView.PLAYER_NORMAL:
                setLayoutParams(new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                mLockButton.setVisibility(GONE);
                break;
            case VideoView.PLAYER_FULL_SCREEN:
                if (isShowing()) {
                    mLockButton.setVisibility(VISIBLE);
                } else {
                    mLockButton.setVisibility(GONE);
                }
                break;
        }

        if (mActivity != null && hasCutout()) {
            int orientation = mActivity.getRequestedOrientation();
            int dp24 = PlayerUtils.dp2px(getContext(), 24);
            int cutoutHeight = getCutoutHeight();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                LayoutParams lblp = (LayoutParams) mLockButton.getLayoutParams();
                lblp.setMargins(dp24, 0, dp24, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                LayoutParams layoutParams = (LayoutParams) mLockButton.getLayoutParams();
                layoutParams.setMargins(dp24 + cutoutHeight, 0, dp24 + cutoutHeight, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                LayoutParams layoutParams = (LayoutParams) mLockButton.getLayoutParams();
                layoutParams.setMargins(dp24, 0, dp24, 0);
            }
        }

    }

    @Override
    protected void onPlayStateChanged(int playState) {
        super.onPlayStateChanged(playState);
        switch (playState) {
            //调用release方法会回到此状态
            case VideoView.STATE_IDLE:
                mLockButton.setSelected(false);
                mLoadingProgress.setVisibility(GONE);
                break;
            case VideoView.STATE_PLAYING:
            case VideoView.STATE_PAUSED:
            case VideoView.STATE_PREPARED:
            case VideoView.STATE_ERROR:
            case VideoView.STATE_BUFFERED:
                mLoadingProgress.setVisibility(GONE);
                break;
            case VideoView.STATE_PREPARING:
            case VideoView.STATE_BUFFERING:
                mLoadingProgress.setVisibility(VISIBLE);
                break;
            case VideoView.STATE_PLAYBACK_COMPLETED:
                mLoadingProgress.setVisibility(GONE);
                mLockButton.setVisibility(GONE);
                mLockButton.setSelected(false);
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        if (isLocked()) {
            show();
            Toast.makeText(getContext(), R.string.dkplayer_lock_tip, Toast.LENGTH_SHORT).show();
            return true;
        }
        if (mControlWrapper.isFullScreen()) {
            return stopFullScreen();
        }
        return super.onBackPressed();
    }

    public void stopFullScreenPlay(){
        stopFullScreen();
    }

    //发送弹幕
    public void addDanmaku(String danmu, boolean b) {
        mMyDanmakuView.addDanmaku(danmu, b);
    }

    //设置弹幕数据
    public void seDanmuListData(SparseArray<List<DanmakuBaseBean>> data) {
        mMyDanmakuView.setData(data);
    }

    //显示弹幕
    public void showDanmaku() {
        mMyDanmakuView.show();
    }

    //隐藏弹幕
    public void hideDanmaku() {
        mMyDanmakuView.hide();
    }
}
