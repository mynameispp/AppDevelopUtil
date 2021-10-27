package com.ffzxnet.developutil.ui.video_play;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.GlideApp;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.ui.video_download.adapter.DownLoadOverAdapter;
import com.ffzxnet.developutil.ui.video_download.bean.DownloadVideoInfoBean;
import com.ffzxnet.developutil.ui.video_download.utils.DownLoadUtil;
import com.ffzxnet.developutil.ui.video_play.cache_video.VideoProxyCacheManage;
import com.ffzxnet.developutil.ui.video_play.my_ijk.MyVideoView;
import com.ffzxnet.developutil.ui.video_play.view.danmu.MyDanmakuView;
import com.ffzxnet.developutil.utils.ui.ToastUtil;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videocontroller.component.ErrorView;
import xyz.doikki.videocontroller.component.PrepareView;
import xyz.doikki.videocontroller.component.VodControlView;
import xyz.doikki.videoplayer.player.VideoView;

public class DKVideoPlayActivity extends BaseActivity implements DownLoadOverAdapter.AdapterListen {

    @BindView(R.id.myVideoPlay)
    MyVideoView myVideoView;
    @BindView(R.id.video_play_local_video_rv)
    RecyclerView videoPlayLocalVideoRv;
    @BindView(R.id.danmu_input_ed)
    EditText danmuInputEd;
    @BindView(R.id.danmu_send_btn)
    TextView danmuSendBtn;
    @BindView(R.id.danmu_switch)
    ImageView danmuSwitch;
    @BindView(R.id.danmu_input_layout)
    LinearLayout danmuInputLayout;

    //默认播放控制界面
    private StandardVideoController controllerPlayer;
    //弹幕
    private MyDanmakuView mMyDanmakuView;
    private boolean showDanmu = true;//是否显示弹幕

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_dk_video_play;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        String videoUrl = getBundle().getString(MyConstans.KEY_DATA, "");
        if (!TextUtils.isEmpty(videoUrl)) {
            //外部传入的地址
            playVideo(videoUrl);
        }
        showLocalVideos();
    }

    //显示本地下载完的视频
    private void showLocalVideos() {
        videoPlayLocalVideoRv.setLayoutManager(new LinearLayoutManager(this));
        DownLoadOverAdapter adapter = new DownLoadOverAdapter(DownLoadUtil.getDownLoadOver(), this);
        videoPlayLocalVideoRv.setAdapter(adapter);
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    //播放视频
    private void playVideo(String videoUrl) {
        if (myVideoView.isPlaying()) {
            myVideoView.pause();
        }
        myVideoView.release();
        //设置视频地址或缓存
        if (videoUrl.indexOf("http") == 0) {
            //网络视频缓存
            myVideoView.setUrl(VideoProxyCacheManage.getInstance(this).getProxyUrl(videoUrl));
        } else {
            myVideoView.setUrl(videoUrl);
        }

        if (null == controllerPlayer) {
            controllerPlayer = new StandardVideoController(this);
//            controllerPlayer.addDefaultControlComponent("", false);
            //底部控制控件
            VodControlView vodControlView = new VodControlView(this);//点播控制条
            //是否显示底部进度条。默认显示
            vodControlView.showBottomProgress(false);
            controllerPlayer.addControlComponent(vodControlView);
            //播放状态
            myVideoView.addOnStateChangeListener(new VideoView.OnStateChangeListener() {
                @Override
                public void onPlayerStateChanged(int playerState) {
                    Log.e("ddddddddd播放器状态", playerState + "======");
                    if (playerState == 10) {
                        //10=小屏，11=全屏
                        isFullScreen(false);
//                        int uiOptions = myVideoView.getSystemUiVisibility();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//                        }
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                            uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//                        }
//                        myVideoView.setSystemUiVisibility(uiOptions);
//                        getWindow().setFlags(
//                                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }
                }

                @Override
                public void onPlayStateChanged(int playState) {
                    Log.e("ddddddddd播放状态", playState + "======");
                    switch (playState) {
                        case VideoView.STATE_PREPARED:
                            //视频初始化完毕
                            //显示弹幕发送控件
                            danmuInputLayout.setVisibility(View.VISIBLE);
                            //获取音轨
                            myVideoView.getTrackInfo();
                            break;
                        case VideoView.STATE_PLAYING:
                            //播放中
                            mMyDanmakuView.addDanmaku("别人发的弹幕", false);
                            break;
                        case VideoView.STATE_PLAYBACK_COMPLETED:
                            //播放完
                            break;
                        case VideoView.STATE_ERROR:
                            //播放失败
                            break;
                    }
                }
            });
            //竖屏也开启手势操作，默认关闭
            controllerPlayer.setEnableInNormal(true);
            //设置弹幕
            mMyDanmakuView = new MyDanmakuView(this);
            controllerPlayer.addControlComponent(mMyDanmakuView);
            //设置错误页面
            ErrorView errorView = new ErrorView(this);
            controllerPlayer.addControlComponent(errorView);
            //视频封面
            PrepareView prepareView = new PrepareView(this);
            prepareView.setClickStart();
            ImageView thumb = prepareView.findViewById(R.id.thumb);
            GlideApp.with(thumb)
                    .load("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF")
                    .into(thumb);
            controllerPlayer.addControlComponent(prepareView);
            //设置控制器
            myVideoView.setVideoController(controllerPlayer);
            //循环播放
            myVideoView.setLooping(true);
        }
        //自动播放
//        myVideoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myVideoView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myVideoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myVideoView.isFullScreen()) {
            myVideoView.stopFullScreen();
        }
        if (myVideoView.isPlaying()) {
            myVideoView.pause();
        }
        myVideoView.release();
    }

    @Override
    public void onItemDownLoadOverClick(DownloadVideoInfoBean data) {
        playVideo(data.getDownLoadUrl().getFilePath());
    }

    @OnClick({R.id.danmu_send_btn, R.id.danmu_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.danmu_send_btn:
                String danmuContent = danmuInputEd.getText().toString().trim();
                if (TextUtils.isEmpty(danmuContent)) {
                    ToastUtil.showToastShort("弹幕内容不能为空");
                } else {
                    mMyDanmakuView.addDanmaku(danmuContent, true);
                    danmuInputEd.setText("");
                }
                break;
            case R.id.danmu_switch:
                //是否开启弹幕
                if (showDanmu) {
                    mMyDanmakuView.hide();
                } else {
                    mMyDanmakuView.show();
                }
                showDanmu = !showDanmu;
                break;
        }
    }
}
