package com.ffzxnet.developutil.ui.video_play.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.adapter.GridSpacingItemDecoration;
import com.ffzxnet.developutil.base.ui.adapter.LinearLaySpacingItemDecoration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xyz.doikki.videoplayer.controller.ControlWrapper;
import xyz.doikki.videoplayer.controller.IControlComponent;
import xyz.doikki.videoplayer.player.VideoView;
import xyz.doikki.videoplayer.util.PlayerUtils;

import static xyz.doikki.videoplayer.util.PlayerUtils.stringForTime;

/**
 * 播放自定义操控界面
 */
public class MyVodControlView extends FrameLayout implements IControlComponent, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    protected ControlWrapper mControlWrapper;

    private TextView mTotalTime, mCurrTime;
    private ImageView mFullScreen;
    private LinearLayout mBottomContainer;
    private SeekBar mVideoProgress;
    private ProgressBar mBottomProgress;
    private ImageView mPlayButton;

    private boolean mIsDragging;

    private boolean mIsShowBottomProgress = true;
    private MyVodControlClickListen controlClickListen;
    private TextView mAnthology;//选集按钮
    private TextView mNextVideo;//下一集
    private RecyclerView mAnthologyRv;//选集数据

    private LinearLayout ordinaryBottomLayout;
    private LinearLayout danmakuBottomLayout;//开启弹幕

    //========弹幕===================
    private EditText danmakuEd;
    private ImageView danmakuSwitch;
    private boolean showDanmaku = false;//默认开启弹幕布局
    private boolean showDanmakuSwitch = true;//开启弹幕

    public MyVodControlView(@NonNull Context context) {
        this(context, null);
    }

    public MyVodControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyVodControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVisibility(GONE);
        LayoutInflater.from(getContext()).inflate(getLayoutId(), this, true);
        ordinaryBottomLayout = findViewById(R.id.bottom_container);
        danmakuBottomLayout = findViewById(R.id.danmaku_bottom_container);
        //===============弹幕布局===============================================
        showDanmakuLayout(showDanmaku);
        //5.1以下系统SeekBar高度需要设置成WRAP_CONTENT
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mVideoProgress.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }

    public void setShowDanmakuLayout(boolean showDanmaku) {
        this.showDanmaku = showDanmaku;
        showDanmakuLayout(showDanmaku);
    }

    public void showDanmakuLayout(boolean showDanmaku) {
        if (showDanmaku) {
            ordinaryBottomLayout.setVisibility(GONE);
            danmakuBottomLayout.setVisibility(VISIBLE);
            danmakuEd = findViewById(R.id.danmaku_content_ed);
            danmakuSwitch = findViewById(R.id.danmaku_switch);
            danmakuSwitch.setOnClickListener(this);
            refreshDanmakuSwitch(showDanmakuSwitch);
            initDanmuEd();
            mFullScreen = findViewById(R.id.danmaku_fullscreen);
            mFullScreen.setOnClickListener(this);
            mBottomContainer = findViewById(R.id.danmaku_bottom_container);
            mVideoProgress = findViewById(R.id.danmaku_seekBar);
            mVideoProgress.setOnSeekBarChangeListener(this);
            mTotalTime = findViewById(R.id.danmaku_total_time);
            mCurrTime = findViewById(R.id.danmaku_curr_time);
            mPlayButton = findViewById(R.id.danmaku_iv_play);
            if (null != mControlWrapper) {
                mPlayButton.setSelected(mControlWrapper.isPlaying());
            }
            mPlayButton.setOnClickListener(this);
            mBottomProgress = findViewById(R.id.bottom_progress);

            mAnthology = findViewById(R.id.danmaku_anthology_videos_btn);
            mAnthology.setOnClickListener(this);
            mNextVideo = findViewById(R.id.danmaku_next_video_btn);
            mNextVideo.setOnClickListener(this);
            mAnthologyRv = findViewById(R.id.anthology_videos_rv);
        } else {
            ordinaryBottomLayout.setVisibility(VISIBLE);
            danmakuBottomLayout.setVisibility(GONE);
            mFullScreen = findViewById(R.id.fullscreen);
            mFullScreen.setOnClickListener(this);
            mBottomContainer = findViewById(R.id.bottom_container);
            mVideoProgress = findViewById(R.id.seekBar);
            mVideoProgress.setOnSeekBarChangeListener(this);
            mTotalTime = findViewById(R.id.total_time);
            mCurrTime = findViewById(R.id.curr_time);
            mPlayButton = findViewById(R.id.iv_play);
            if (null != mControlWrapper) {
                mPlayButton.setSelected(mControlWrapper.isPlaying());
            }
            mPlayButton.setOnClickListener(this);
            mBottomProgress = findViewById(R.id.bottom_progress);

            mAnthology = findViewById(R.id.anthology_videos_btn);
            mAnthology.setOnClickListener(this);
            mNextVideo = findViewById(R.id.next_video_btn);
            mNextVideo.setOnClickListener(this);
            mAnthologyRv = findViewById(R.id.anthology_videos_rv);
        }
    }

    public void refreshDanmakuSwitch(boolean show) {
        this.showDanmakuSwitch = show;
        if (showDanmakuSwitch) {
            danmakuSwitch.setImageResource(R.mipmap.icon_danmu_close);
        } else {
            danmakuSwitch.setImageResource(R.mipmap.icon_danmu_open);
        }
    }

    protected int getLayoutId() {
        return R.layout.my_dkplayer_layout_vod_control_view;
    }

    /**
     * 弹幕初始化
     */
    private void initDanmuEd() {
        danmakuEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //监听软键盘的发送按钮
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //发送弹幕
                    String content = danmakuEd.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)) {
                        danmakuEd.setText("");
                        //收起键盘
                        hidSoftInput();
                        //通知界面发送弹幕
                        controlClickListen.onSendDanmaku(content);
                        //发送完重置界面隐藏倒计时
                        mControlWrapper.toggleShowState();
                    }
                    return true;
                }
                return false;
            }
        });
        InputFilter inputFilter = new InputFilter() {
            final String InputEditexFilter = "[^a-zA-Z0-9\\u4E00-\\u9FA5_:：，,.。·?？!！(∩_)~⁄ω✿◡‿୧̀◡•́๑૭o￣▽\\s]";
            Pattern emoji = Pattern.compile(InputEditexFilter);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher emojiMatcher = emoji.matcher(source);
                if (emojiMatcher.find()) {
                    Toast.makeText(danmakuEd.getContext(), "不能输入表情符号哦", Toast.LENGTH_SHORT).show();
                    return "";
                }
                return null;
            }
        };
        danmakuEd.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(20)});
//        danmuEd.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(20)});//限制字数
    }

    //收起键盘
    public void hidSoftInput() {
        InputMethodManager imm = (InputMethodManager) danmakuEd.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(danmakuEd.getWindowToken(), 0);
        }
    }

    /**
     * 设置选集参数
     *
     * @param layoutManager 排版
     * @param adapter       数据
     */
    public void setAnthologyVideosAdapter(RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mAnthologyRv.setLayoutManager(layoutManager);
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                mAnthologyRv.addItemDecoration(new GridSpacingItemDecoration(gridLayoutManager.getSpanCount(), 10, true));
            } else {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                mAnthologyRv.addItemDecoration(new LinearLaySpacingItemDecoration(linearLayoutManager.getOrientation()
                        , 10, R.color.white));
            }
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
     * 设置点击对外监听
     *
     * @param onClickListener
     */
    public void setNextVideoListen(MyVodControlClickListen onClickListener) {
        controlClickListen = onClickListener;
    }

    /**
     * 是否显示底部进度条，默认显示
     */
    public void showBottomProgress(boolean isShow) {
        mIsShowBottomProgress = isShow;
    }

    @Override
    public void attach(@NonNull ControlWrapper controlWrapper) {
        mControlWrapper = controlWrapper;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {
        //控制器隐藏/显示
        if (isVisible) {
            mBottomContainer.setVisibility(VISIBLE);
            if (anim != null) {
                mBottomContainer.startAnimation(anim);
            }
            if (mIsShowBottomProgress) {
                mBottomProgress.setVisibility(GONE);
            }
        } else {
            if (danmakuEd != null && danmakuEd.getText().toString().length() > 0) {
                //弹幕有内容不隐藏
                return;
            }
            mBottomContainer.setVisibility(GONE);
            if (anim != null) {
                mBottomContainer.startAnimation(anim);
            }
            if (mIsShowBottomProgress) {
                mBottomProgress.setVisibility(VISIBLE);
                AlphaAnimation animation = new AlphaAnimation(0f, 1f);
                animation.setDuration(300);
                mBottomProgress.startAnimation(animation);
            }
            if (mAnthologyRv.getVisibility() == VISIBLE) {
                mAnthologyRv.setVisibility(GONE);
                if (anim != null) {
                    mAnthologyRv.startAnimation(anim);
                }
            }
        }
    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case VideoView.STATE_IDLE:
            case VideoView.STATE_PLAYBACK_COMPLETED:
                setVisibility(GONE);
                mBottomProgress.setProgress(0);
                mBottomProgress.setSecondaryProgress(0);
                mVideoProgress.setProgress(0);
                mVideoProgress.setSecondaryProgress(0);
                break;
            case VideoView.STATE_START_ABORT:
            case VideoView.STATE_PREPARING:
            case VideoView.STATE_PREPARED:
            case VideoView.STATE_ERROR:
                setVisibility(GONE);
                break;
            case VideoView.STATE_PLAYING:
                mPlayButton.setSelected(true);
                if (mIsShowBottomProgress) {
                    if (mControlWrapper.isShowing()) {
                        mBottomProgress.setVisibility(GONE);
                        mBottomContainer.setVisibility(VISIBLE);
                    } else {
                        mBottomContainer.setVisibility(GONE);
                        mBottomProgress.setVisibility(VISIBLE);
                    }
                } else {
                    mBottomContainer.setVisibility(GONE);
                }
                setVisibility(VISIBLE);
                //开始刷新进度
                mControlWrapper.startProgress();
                break;
            case VideoView.STATE_PAUSED:
                mPlayButton.setSelected(false);
                break;
            case VideoView.STATE_BUFFERING:
            case VideoView.STATE_BUFFERED:
                mPlayButton.setSelected(mControlWrapper.isPlaying());
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {
        switch (playerState) {
            case VideoView.PLAYER_NORMAL:
                mFullScreen.setSelected(false);
                //小屏
                showDanmakuLayout(false);
                if (mAnthologyRv.getVisibility() == VISIBLE) {
                    mAnthologyRv.setVisibility(GONE);
                    mControlWrapper.startFadeOut();
                }
                mNextVideo.setVisibility(GONE);
                mAnthology.setVisibility(GONE);
                break;
            case VideoView.PLAYER_FULL_SCREEN:
                mFullScreen.setSelected(true);
                mNextVideo.setVisibility(VISIBLE);
                mAnthology.setVisibility(VISIBLE);
                showDanmakuLayout(showDanmaku);
                break;
        }

        Activity activity = PlayerUtils.scanForActivity(getContext());
        if (activity != null && mControlWrapper.hasCutout()) {
            int orientation = activity.getRequestedOrientation();
            int cutoutHeight = mControlWrapper.getCutoutHeight();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                mBottomContainer.setPadding(0, 0, 0, 0);
                mBottomProgress.setPadding(0, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                mBottomContainer.setPadding(cutoutHeight, 0, 0, 0);
                mBottomProgress.setPadding(cutoutHeight, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                mBottomContainer.setPadding(0, 0, cutoutHeight, 0);
                mBottomProgress.setPadding(0, 0, cutoutHeight, 0);
            }
        }
    }

    @Override
    public void setProgress(int duration, int position) {
        if (mIsDragging) {
            return;
        }

        if (mVideoProgress != null) {
            if (duration > 0) {
                mVideoProgress.setEnabled(true);
                int pos = (int) (position * 1.0 / duration * mVideoProgress.getMax());
                mVideoProgress.setProgress(pos);
                mBottomProgress.setProgress(pos);
            } else {
                mVideoProgress.setEnabled(false);
            }
            int percent = mControlWrapper.getBufferedPercentage();
            if (percent >= 95) { //解决缓冲进度不能100%问题
                mVideoProgress.setSecondaryProgress(mVideoProgress.getMax());
                mBottomProgress.setSecondaryProgress(mBottomProgress.getMax());
            } else {
                mVideoProgress.setSecondaryProgress(percent * 10);
                mBottomProgress.setSecondaryProgress(percent * 10);
            }
        }

        if (mTotalTime != null)
            mTotalTime.setText(stringForTime(duration));
        if (mCurrTime != null)
            mCurrTime.setText(stringForTime(position));
    }

    @Override
    public void onLockStateChanged(boolean isLocked) {
        onVisibilityChanged(!isLocked, null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fullscreen:
            case R.id.danmaku_fullscreen:
                toggleFullScreen();
                break;
            case R.id.iv_play:
            case R.id.danmaku_iv_play:
                mControlWrapper.togglePlay();
                break;
            case R.id.anthology_videos_btn:
            case R.id.danmaku_anthology_videos_btn:
                //显示或隐藏选集
                if (mAnthologyRv.getVisibility() == GONE) {
                    mAnthologyRv.setVisibility(VISIBLE);
                    mControlWrapper.stopFadeOut();
                } else {
                    mAnthologyRv.setVisibility(GONE);
                    mControlWrapper.startFadeOut();
                }
                break;
            case R.id.next_video_btn:
            case R.id.danmaku_next_video_btn:
                //下一集
                if (null != controlClickListen) {
                    controlClickListen.onNextVideoClick();
                }
                break;
            case R.id.danmaku_switch:
                //弹幕开关
                if (null != controlClickListen) {
                    showDanmakuSwitch = !showDanmakuSwitch;
                    controlClickListen.onSwitchDanmaku(showDanmakuSwitch);
                    refreshDanmakuSwitch(showDanmakuSwitch);
                }
                break;
        }
    }

    /**
     * 横竖屏切换
     */
    private void toggleFullScreen() {
        Activity activity = PlayerUtils.scanForActivity(getContext());
        mControlWrapper.toggleFullScreen(activity);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mIsDragging = true;
        mControlWrapper.stopProgress();
        mControlWrapper.stopFadeOut();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        long duration = mControlWrapper.getDuration();
        long newPosition = (duration * seekBar.getProgress()) / mVideoProgress.getMax();
        mControlWrapper.seekTo((int) newPosition);
        mIsDragging = false;
        mControlWrapper.startProgress();
        mControlWrapper.startFadeOut();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
            return;
        }

        long duration = mControlWrapper.getDuration();
        long newPosition = (duration * progress) / mVideoProgress.getMax();
        if (mCurrTime != null)
            mCurrTime.setText(stringForTime((int) newPosition));
    }

}
