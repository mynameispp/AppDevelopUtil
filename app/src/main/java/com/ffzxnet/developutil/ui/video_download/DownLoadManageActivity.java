package com.ffzxnet.developutil.ui.video_download;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.GlideApp;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.evenbus.MyEventbus;
import com.ffzxnet.developutil.service.DownLoadingService;
import com.ffzxnet.developutil.ui.video_download.adapter.DownLoadOverAdapter;
import com.ffzxnet.developutil.ui.video_download.bean.DownloadVideoInfoBean;
import com.ffzxnet.developutil.ui.video_download.downloading.DownloadingActivity;
import com.ffzxnet.developutil.ui.video_download.player.PlayerActivity;
import com.ffzxnet.developutil.ui.video_download.utils.DeviceUtils;
import com.ffzxnet.developutil.ui.video_download.utils.DownLoadUtil;
import com.ffzxnet.developutil.ui.video_play.DKVideoPlayActivity;
import com.ffzxnet.developutil.utils.ui.ToastUtil;
import com.ffzxnet.developutil.utils.video_download.VideoDownloadManager;
import com.ffzxnet.developutil.utils.video_download.listener.IDownloadInfosCallback;
import com.ffzxnet.developutil.utils.video_download.model.VideoTaskItem;
import com.ffzxnet.developutil.utils.video_download.model.VideoTaskState;
import com.ffzxnet.developutil.utils.video_download.utils.VideoStorageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class DownLoadManageActivity extends BaseActivity implements DownLoadOverAdapter.AdapterListen {
    private static final int MSG_COUNT_SIZE = 0x1;
    @BindView(R.id.toolbar_right_tv)
    TextView downloadMainEdit;
    @BindView(R.id.toolbar_right_tv2)
    TextView downloadManageDelete;
    @BindView(R.id.download_main_downloading_info_image)
    ImageView downloadMainDownloadingInfoImage;
    @BindView(R.id.download_main_downloading_info_video_count)
    TextView downloadMainDownloadingInfoVideoCount;
    @BindView(R.id.download_main_downloading_info_name)
    TextView downloadMainDownloadingInfoName;
    @BindView(R.id.download_main_downloading_info_subject_name)
    TextView downloadMainDownloadingInfoSubjectName;
    @BindView(R.id.download_main_downloading_info_progress)
    ProgressBar downloadMainDownloadingInfoProgress;
    @BindView(R.id.download_main_downloading_info_speed)
    TextView downloadMainDownloadingInfoSpeed;
    @BindView(R.id.download_main_downloading_info_all_size)
    TextView downloadMainDownloadingInfoAllSize;
    @BindView(R.id.download_main_download_over_rv)
    RecyclerView downloadMainDownloadOverRv;
    @BindView(R.id.download_main_downloading_info_more)
    TextView downloadMainDownloadingInfoMore;
    @BindView(R.id.download_main_downloading_info_layout)
    RelativeLayout downloadMainDownloadingInfoLayout;
    @BindView(R.id.download_main_store_size_text)
    TextView storeSizeText;

    private DownLoadOverAdapter adapter;
    private List<DownloadVideoInfoBean> downLoadingVideos;
    private List<DownloadVideoInfoBean> downLoadOverVideos;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_COUNT_SIZE) {
                String filePath = VideoDownloadManager.getInstance().getDownloadPath();
                File file = new File(filePath);
                if (file.exists()) {
                    long size = VideoStorageUtils.countTotalSize(file);
                    storeSizeText.setText("已下载:" + VideoStorageUtils.getSizeStr(size) + " , 剩余:" + DeviceUtils.getBlockSurplus());
                }
            }
        }
    };

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_download_manage;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "下载管理");
        downloadMainEdit.setText("编辑");
        downloadManageDelete.setText("删除");
        downloadManageDelete.setVisibility(View.GONE);
        bindService(new Intent(this, DownLoadingService.class), serviceConnection, BIND_AUTO_CREATE);

        downloadMainEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDownloadOverVideos();
            }
        });
        downloadManageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDownloadOverVideo();
            }
        });
        setTestData();
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        super.onDestroy();
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    //测试数据
    private void setTestData() {
        if (DownLoadUtil.getDownLoading().size() > 0) {
            return;
        }
        List<DownloadVideoInfoBean> downloadVideoInfoBeans = new ArrayList<>();
        List<VideoTaskItem> videoTaskItems = new ArrayList<>();
        VideoTaskItem item1 = new VideoTaskItem("https://v3.dious.cc/20201224/v04Vp1ES/index.m3u8", "https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF", "test1", "group-1");
        VideoTaskItem item2 = new VideoTaskItem("https://v3.dious.cc/20201224/6Q1yAHRu/index.m3u8", "https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF", "test2", "group-1");
        VideoTaskItem item3 = new VideoTaskItem("https://v3.dious.cc/20201224/aQKzuq6G/index.m3u8", "https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF", "test3", "group-1");
        VideoTaskItem item4 = new VideoTaskItem("https://v3.dious.cc/20201224/WWTyUxS6/index.m3u8", "https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF", "test3", "group-1");
        VideoTaskItem item5 = new VideoTaskItem("http://videoconverter.vivo.com.cn/201706/655_1498479540118.mp4.main.m3u8", "https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF", "test4", "group-2");
        VideoTaskItem item6 = new VideoTaskItem("https://europe.olemovienews.com/hlstimeofffmp4/20210226/fICqcpqr/mp4/fICqcpqr.mp4/master.m3u8", "https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF", "test5", "group-2");
        VideoTaskItem item7 = new VideoTaskItem("https://rrsp-1252816746.cos.ap-shanghai.myqcloud.com/0c1f023caa3bbefbe16a5ce564142bbe.mp4", "https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF", "test6", "group-2");
        VideoTaskItem item8 = new VideoTaskItem("http://v-cdn.40sp.top/videos/202110/02/614ee1946fcbfe5cb3c233e9/b7b3g9/index.m3u8", "https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF", "test7", "group-1");
        VideoTaskItem item9 = new VideoTaskItem("http://v-cdn.40sp.top/videos/202110/07/615cb2755ca1dd5cba0fd13a/31bb88/index.m3u8 ", "https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF", "test8", "group-1");

        videoTaskItems.add(item1);
        videoTaskItems.add(item2);
        videoTaskItems.add(item3);
        videoTaskItems.add(item4);
        videoTaskItems.add(item5);
        videoTaskItems.add(item6);
        videoTaskItems.add(item7);
        videoTaskItems.add(item8);
        videoTaskItems.add(item9);

        DownloadVideoInfoBean videoInfoBean;
        int i = 1;
        for (VideoTaskItem videoTaskItem : videoTaskItems) {
            videoInfoBean = new DownloadVideoInfoBean();
            videoInfoBean.setVideoName("测试视频" + i);
            videoInfoBean.setVideoId("" + i);
            videoInfoBean.setDownLoadUrl(videoTaskItem);
            downloadVideoInfoBeans.add(videoInfoBean);
            i++;
        }
        DownLoadUtil.saveDownLoading(downloadVideoInfoBeans);
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    private void initData() {
        //下载中
        downLoadingVideos = DownLoadUtil.getDownLoading();
        if (downLoadingVideos != null && downLoadingVideos.size() > 0) {
            GlideApp.with(downloadMainDownloadingInfoImage)
                    .load(downLoadingVideos.get(0).getVideoImage())
                    .placeholder(R.mipmap.icon_default_video_bg)
                    .into(downloadMainDownloadingInfoImage);
            downloadMainDownloadingInfoVideoCount.setText(downLoadingVideos.size() + "个视频");
            DownloadVideoInfoBean firsVideo = downLoadingVideos.get(0);
            downloadMainDownloadingInfoName.setText(firsVideo.getVideoName());
            downloadMainDownloadingInfoSubjectName.setText(firsVideo.getSubjectName());
            downloadMainDownloadingInfoProgress.setProgress((int) firsVideo.getDownLoadUrl().getPercent());
            if (firsVideo.getDownLoadUrl().getTotalSize() != 0) {
                downloadMainDownloadingInfoAllSize.setText(VideoStorageUtils.getSizeStr(firsVideo.getDownLoadUrl().getTotalSize()));
            } else if (firsVideo.getDownLoadUrl().getDownloadSize() != 0) {
                downloadMainDownloadingInfoAllSize.setText(firsVideo.getDownLoadUrl().getDownloadSizeString());
            } else {
                downloadMainDownloadingInfoAllSize.setText("");
            }
        } else {
            downloadMainDownloadingInfoLayout.setVisibility(View.GONE);
        }

        //下载完
        downLoadOverVideos = DownLoadUtil.getDownLoadOver();
        if (downLoadOverVideos != null) {
            if (adapter == null) {
                downloadMainDownloadOverRv.setLayoutManager(new LinearLayoutManager(this));
                adapter = new DownLoadOverAdapter(downLoadOverVideos, this);
                downloadMainDownloadOverRv.setAdapter(adapter);
            } else {
                adapter.setDatas(downLoadOverVideos);
            }
        }
    }

    @OnClick({R.id.toolbar_right_tv, R.id.toolbar_right_tv2, R.id.download_main_downloading_info_layout
            , R.id.download_main_downloading_info_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_right_tv2:
                deleteDownloadOverVideo();
                break;
            case R.id.toolbar_right_tv:
                editDownloadOverVideos();
                break;
            case R.id.download_main_downloading_info_layout:
            case R.id.download_main_downloading_info_more:
                startActivity(new Intent(this, DownloadingActivity.class));
                break;
        }
    }

    private void deleteDownloadOverVideo() {
        if (adapter != null && adapter.getDatas() != null) {
            Iterator<DownloadVideoInfoBean> iterator = adapter.getDatas().iterator();
            while (iterator.hasNext()) {
                DownloadVideoInfoBean data = iterator.next();
                if (data.isSelect()) {
                    VideoDownloadManager.getInstance().deleteVideoTask(data.getDownLoadUrl(), true);
                    iterator.remove();
                }
            }
            DownLoadUtil.saveDownLoadOver(adapter.getDatas());
            adapter.notifyDataSetChanged();
            //全部删除
//                    VideoDownloadManager.getInstance().deleteAllVideoFiles();;
//                    adapter.setDatas(new ArrayList<>());
//                    adapter.notifyDataSetChanged();
//                    SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.SP_DownLoadOver_List, "");
//                    if (downLoadOverVideos != null) {
//                        downLoadOverVideos.clear();
//                    }
        }
    }

    private void editDownloadOverVideos() {
        if (adapter != null) {
            if (adapter.isEdit()) {
                downloadMainEdit.setText("编辑");
                downloadManageDelete.setVisibility(View.GONE);
            } else {
                downloadMainEdit.setText("取消");
                downloadManageDelete.setVisibility(View.VISIBLE);
            }
            adapter.setEdit(!adapter.isEdit());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemDownLoadOverClick(DownloadVideoInfoBean data) {
        //点击播放
        DownLoadUtil.starPlayM3u8ToMp4(data, new DownLoadUtil.TransformM3U8ToMp4Listen() {
            @Override
            public void onTransformProgressing(String progress) {
                showLoadingDialog(true, progress);
            }

            @Override
            public void onTransformProgressFinsh(DownloadVideoInfoBean video) {
                //播放
                for (DownloadVideoInfoBean adapterData : adapter.getDatas()) {
                    if (adapterData.getDownLoadUrl().getUrl().equals(video.getDownLoadUrl().getUrl())) {
                        //转换后保存路径，下次播放不用再转换
                        adapterData.getDownLoadUrl().setFilePath(video.getDownLoadUrl().getFilePath());
                        DownLoadUtil.saveDownLoadOver(adapter.getDatas());
                        break;
                    }
                }
                showLoadingDialog(false);
                //普通播放
//                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
//                intent.putExtra("videoUrl", "file://" + video.getDownLoadUrl().getFilePath());
//                startActivity(intent);
                //DK播放器
                Intent intent = new Intent(getApplicationContext(), DKVideoPlayActivity.class);
                intent.putExtra(MyConstans.KEY_DATA, video.getDownLoadUrl().getFilePath());
                startActivity(intent);
            }

            @Override
            public void onTransformProgressError(String msg) {
                showLoadingDialog(false);
                ToastUtil.showToastShort(msg);
            }
        });

    }


    private IDownloadInfosCallback mInfosCallback =
            new IDownloadInfosCallback() {
                @Override
                public void onDownloadInfos(List<VideoTaskItem> items) {
                    mHandler.sendEmptyMessage(MSG_COUNT_SIZE);
                    for (VideoTaskItem item : items) {
                        if (item.getTaskState() == VideoTaskState.DOWNLOADING) {
                            showDownloadingStatus(item);
                            break;
                        }
                    }
                }
            };

    /**
     * 更新下载状态
     *
     * @param item 视频
     */
    private void showDownloadingStatus(VideoTaskItem item) {
        if (mHandler == null) {
            return;
        }
        mHandler.sendEmptyMessage(MSG_COUNT_SIZE);
        if (downLoadingVideos != null && item.getTaskState() == VideoTaskState.SUCCESS) {
            for (DownloadVideoInfoBean downLoadingVideo : downLoadingVideos) {
                if (downLoadingVideo.getDownLoadUrl().getUrl().equals(item.getUrl())) {
                    downLoadingVideo.setDownLoadUrl(item);
                    downLoadOverVideos.add(0, downLoadingVideo);
                    downLoadingVideos.remove(downLoadingVideo);
                    break;
                }
            }
            DownLoadUtil.saveDownLoading(downLoadingVideos);
            DownLoadUtil.saveDownLoadOver(downLoadOverVideos);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("dddddddd=notifyChanged=", "=========runOnUiThread== Manage=========" + item.getTaskState());
                if (item.getTaskState() != VideoTaskState.SUCCESS) {
                    downloadMainDownloadingInfoName.setText(item.getFileName());
                    if (item.getTaskState() == VideoTaskState.ERROR) {
                        VideoDownloadManager.getInstance().pauseDownloadTask(item);
                        VideoDownloadManager.getInstance().startDownload(item);
                        downloadMainDownloadingInfoSpeed.setText("下载出错了，点击重试");
                        Log.e("dddddddd=notifyChanged=", "=========runOnUiThread== Manage=========下载出错了");
                    }
                    if (item.getTaskState() == VideoTaskState.PAUSE) {
                        downloadMainDownloadingInfoSpeed.setText("暂停");
                    }
                    if (item.getTaskState() == VideoTaskState.PENDING ||
                            item.getTaskState() == VideoTaskState.PREPARE) {
                        downloadMainDownloadingInfoSpeed.setText("连接中");
                    } else {
                        downloadMainDownloadingInfoSpeed.setText(item.getSpeedString());
                    }
                    if (item.getTotalSize() == 0) {
                        downloadMainDownloadingInfoAllSize.setText(item.getDownloadSizeString());
                    } else {
                        downloadMainDownloadingInfoAllSize.setText(VideoStorageUtils.getSizeStr(item.getTotalSize()));
                    }
                    downloadMainDownloadingInfoName.setText(item.getGroupName());
                    downloadMainDownloadingInfoSubjectName.setText(item.getTitle());
                    downloadMainDownloadingInfoProgress.setProgress((int) item.getPercent());
                    downloadMainDownloadingInfoProgress.setVisibility(View.VISIBLE);

                    GlideApp.with(downloadMainDownloadingInfoImage)
                            .load(item.getCoverUrl())
                            .placeholder(R.mipmap.icon_default_video_bg)
                            .into(downloadMainDownloadingInfoImage);
                } else {
                    downloadMainDownloadingInfoName.setText(item.getFileName());
                    downloadMainDownloadingInfoSpeed.setText("");
                    downloadMainDownloadingInfoAllSize.setText("");
                    downloadMainDownloadingInfoProgress.setVisibility(View.INVISIBLE);
                    initData();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        VideoDownloadManager.getInstance().fetchDownloadItems(mInfosCallback);
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        VideoDownloadManager.getInstance().removeDownloadInfosCallback(mInfosCallback);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downdloadingEvent(MyEventbus.DownloadingEven even) {
        showDownloadingStatus(even.getVideoTaskItem());
    }
}
