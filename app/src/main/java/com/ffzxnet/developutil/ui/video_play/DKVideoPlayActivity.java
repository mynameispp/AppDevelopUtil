package com.ffzxnet.developutil.ui.video_play;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.ui.video_download.adapter.DownLoadOverAdapter;
import com.ffzxnet.developutil.ui.video_download.bean.DownloadVideoInfoBean;
import com.ffzxnet.developutil.ui.video_download.utils.DownLoadUtil;
import com.ffzxnet.developutil.ui.video_play.cache_video.VideoProxyCacheManage;
import com.ffzxnet.developutil.ui.video_play.my_ijk.MyVideoView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videoplayer.player.VideoView;

public class DKVideoPlayActivity extends BaseActivity implements DownLoadOverAdapter.AdapterListen {

    @BindView(R.id.myVideoPlay)
    MyVideoView myVideoView;
    @BindView(R.id.video_play_local_video_rv)
    RecyclerView videoPlayLocalVideoRv;

    //默认播放控制界面
    private StandardVideoController controllerPlayer;


    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_dk_video_play;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        String videoUrl = getBundle().getString(MyConstans.KEY_DATA, "");
        if (!TextUtils.isEmpty(videoUrl)) {
            playVideo(videoUrl);
        }
        showLocalVideos();
    }

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
            controllerPlayer.addDefaultControlComponent("", false);
            //播放状态
            myVideoView.addOnStateChangeListener(new VideoView.OnStateChangeListener() {
                @Override
                public void onPlayerStateChanged(int playerState) {
                    Log.e("ddddddddd播放器状态", playerState + "======");
                    if (playerState == 10) {
                        int uiOptions = myVideoView.getSystemUiVisibility();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                        }
                        myVideoView.setSystemUiVisibility(uiOptions);
                        getWindow().setFlags(
                                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }
                }

                @Override
                public void onPlayStateChanged(int playState) {
                    Log.e("ddddddddd播放状态", playState + "======");
                    if (playState == VideoView.STATE_PLAYING) {
                        myVideoView.getTrackInfo();
                    }
                    if (playState == -1 || playState == 5) {
                        //播放完成或者失败
                    }
                }
            });
            myVideoView.setVideoController(controllerPlayer); //设置控制器
            //循环播放
            myVideoView.setLooping(true);
        }
        myVideoView.start();
        //设置配置信息
        myVideoView.setMediaPlayerOption();
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
}
