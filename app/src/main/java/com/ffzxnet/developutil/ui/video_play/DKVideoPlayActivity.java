package com.ffzxnet.developutil.ui.video_play;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
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
import com.ffzxnet.developutil.ui.video_play.adapter.AnthologyVideosAdapter;
import com.ffzxnet.developutil.ui.video_play.adapter.AnthologyVideosBean;
import com.ffzxnet.developutil.ui.video_play.cache_video.VideoProxyCacheManage;
import com.ffzxnet.developutil.ui.video_play.my_ijk.MyVideoView;
import com.ffzxnet.developutil.ui.video_play.view.MyVideoController;
import com.ffzxnet.developutil.ui.video_play.view.MyVodControlView;
import com.ffzxnet.developutil.ui.video_play.view.danmu.MyDanmakuView;
import com.ffzxnet.developutil.utils.ui.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videocontroller.component.ErrorView;
import xyz.doikki.videocontroller.component.PrepareView;
import xyz.doikki.videocontroller.component.TitleView;
import xyz.doikki.videoplayer.player.VideoView;

public class DKVideoPlayActivity extends BaseActivity implements DownLoadOverAdapter.AdapterListen, AnthologyVideosAdapter.AdapterListen {

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
    private MyVideoController controllerPlayer;
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
            //组装播放器控件
            controllerPlayer = new MyVideoController(this);
            //测试数据
            List<AnthologyVideosBean> anthologyVideosBeans = new ArrayList<>();
            AnthologyVideosBean item;
            for (int i = 1; i < 21; i++) {
                item = new AnthologyVideosBean();
                item.setAnthologyTitle("第" + i + "集");
                anthologyVideosBeans.add(item);
            }
            //测试数据End
            controllerPlayer.addDefaultControlComponent("视频名称", false, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            },new GridLayoutManager(this,4),new AnthologyVideosAdapter(anthologyVideosBeans, this));
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
                            controllerPlayer.addDanmaku("别人发的弹幕", false);
                            controllerPlayer.addDanmaku("别人发的弹幕", false);
                            controllerPlayer.addDanmaku("别人发的弹幕", false);
                            controllerPlayer.addDanmaku("别人发的弹幕", false);
                            controllerPlayer.addDanmaku("别人发的弹幕", false);
                            controllerPlayer.addDanmaku("别人发的弹幕", false);
                            controllerPlayer.addDanmaku("别人发的弹幕", false);
                            controllerPlayer.addDanmaku("别人发的弹幕", false);
                            controllerPlayer.addDanmaku("别人发的弹幕", false);
                            controllerPlayer.addDanmaku("别人发的弹幕", false);
                            controllerPlayer.addDanmaku("别人发的弹幕", false);
                            controllerPlayer.addDanmaku("别人发的弹幕", false);
                            controllerPlayer.addDanmaku("别人发的弹幕", false);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && myVideoView.isFullScreen()) {
            controllerPlayer.stopFullScreenPlay();
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            myVideoView.stopFullScreen();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
                    controllerPlayer.addDanmaku(danmuContent, true);
                    danmuInputEd.setText("");
                }
                break;
            case R.id.danmu_switch:
                //是否开启弹幕
                if (showDanmu) {
                    controllerPlayer.hideDanmaku();
                    danmuSwitch.setImageResource(R.mipmap.icon_danmu_open);
                } else {
                    controllerPlayer.showDanmaku();
                    danmuSwitch.setImageResource(R.mipmap.icon_danmu_close);
                }
                showDanmu = !showDanmu;
                break;
        }
    }

    @Override
    public void onAnthologyVideosClick(AnthologyVideosBean data) {
        //选集点击
        ToastUtil.showToastShort(data.getAnthologyTitle());
    }
}
