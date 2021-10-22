package com.ffzxnet.developutil.ui.video_download.downloading;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.evenbus.MyEventbus;
import com.ffzxnet.developutil.ui.video_download.adapter.DownLoadingAdapter;
import com.ffzxnet.developutil.ui.video_download.bean.DownloadVideoInfoBean;
import com.ffzxnet.developutil.ui.video_download.utils.DeviceUtils;
import com.ffzxnet.developutil.ui.video_download.utils.DownLoadUtil;
import com.ffzxnet.developutil.utils.video_download.VideoDownloadManager;
import com.ffzxnet.developutil.utils.video_download.listener.IDownloadInfosCallback;
import com.ffzxnet.developutil.utils.video_download.model.VideoTaskItem;
import com.ffzxnet.developutil.utils.video_download.model.VideoTaskState;
import com.ffzxnet.developutil.utils.video_download.utils.VideoStorageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class DownloadingActivity extends BaseActivity implements DownLoadingAdapter.AdapterListen {
    private static final int MSG_COUNT_SIZE = 0x1;

    @BindView(R.id.toolbar_right_tv)
    TextView downloadingEdit;
    @BindView(R.id.downloading_rv)
    RecyclerView downloadingRv;
    @BindView(R.id.downloading_stop_all)
    TextView downloadingStopAll;
    @BindView(R.id.downloading_setting)
    TextView downloadingSetting;
    @BindView(R.id.store_size_text)
    TextView storeSizeText;
    @BindView(R.id.toolbar_right_tv2)
    TextView downloadingDelete;

    private DownLoadingAdapter adapter;
    private LinearLayoutManager downloadingLayoutManage;
    private long mLastProgressTimeStamp;
    private long mLastSpeedTimeStamp;

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
        return R.layout.activity_downloading;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initView();
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    private void initView() {
        initToolBar("", "离线缓存");
        mHandler.sendEmptyMessage(MSG_COUNT_SIZE);
        List<DownloadVideoInfoBean> downLoadingVideos = DownLoadUtil.getDownLoading();
        if (downLoadingVideos != null && downLoadingVideos.size() > 0) {
            downloadingLayoutManage = new LinearLayoutManager(this);
            downloadingRv.setLayoutManager(downloadingLayoutManage);
            adapter = new DownLoadingAdapter(downLoadingVideos, this);
            downloadingRv.setAdapter(adapter);
        }
    }

    @OnClick({R.id.toolbar_right_tv, R.id.toolbar_right_tv2, R.id.downloading_stop_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_right_tv:
                if (adapter != null) {
                    if (adapter.isEdit()) {
                        downloadingEdit.setText("编辑");
                        downloadingDelete.setVisibility(View.GONE);
                    } else {
                        downloadingEdit.setText("取消");
                        downloadingDelete.setVisibility(View.VISIBLE);
                    }
                    adapter.setEdit(!adapter.isEdit());
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.toolbar_right_tv2:
                if (adapter != null && adapter.getDatas() != null) {
                    Iterator<DownloadVideoInfoBean> iterator = adapter.getDatas().iterator();
                    while (iterator.hasNext()) {
                        DownloadVideoInfoBean data = iterator.next();
                        if (data.isSelect()) {
                            VideoDownloadManager.getInstance().pauseDownloadTask(data.getDownLoadUrl());
                            VideoDownloadManager.getInstance().deleteVideoTask(data.getDownLoadUrl(), true);
                            iterator.remove();
                        }
                    }
                    DownLoadUtil.saveDownLoading(adapter.getDatas());
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.downloading_stop_all:
                VideoDownloadManager.getInstance().pauseAllDownloadTasks();
                break;
        }
    }

    @Override
    public void onItemDownLoadingClick(DownloadVideoInfoBean data) {
        VideoTaskItem item = data.getDownLoadUrl();
        if (item.isInitialTask()) {
            VideoDownloadManager.getInstance().startDownload(item);
        } else if (item.isRunningTask()) {
            VideoDownloadManager.getInstance().pauseDownloadTask(item.getUrl());
        } else if (item.isInterruptTask()) {
            VideoDownloadManager.getInstance().resumeDownload(item.getUrl());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downdloadingEvent(MyEventbus.DownloadingEven even) {
        notifyChanged(even.getVideoTaskItem());
    }

    private void notifyChanged(final VideoTaskItem item) {
        if (mHandler == null) {
            return;
        }
        mHandler.sendEmptyMessage(MSG_COUNT_SIZE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("dddddddd=notifyChanged=", "=========runOnUiThread===========" + item.getTaskState());
                int position = 0;
                for (DownloadVideoInfoBean data : adapter.getDatas()) {
                    if (data.getDownLoadUrl().equals(item)) {
                        View view = downloadingLayoutManage.findViewByPosition(position);
                        if (view != null) {
                            DownLoadingAdapter.MyHolder myHolder = new DownLoadingAdapter.MyHolder(view);
                            myHolder.itemDownloadingInfoSpeed.setText("连接中...");
                            myHolder.setDownloadInfoText(data);
                            data.setDownLoadUrl(item);
                        }
                        break;
                    }
                    position++;
                }

                if (adapter != null && item.getTaskState() == VideoTaskState.SUCCESS) {
                    int position1 = 0;
                    for (DownloadVideoInfoBean data : adapter.getDatas()) {
                        if (!TextUtils.isEmpty(data.getDownLoadUrl().getFileHash()) &&
                                data.getDownLoadUrl().getFileHash().equals(item.getFileHash())) {
                            List<DownloadVideoInfoBean> downLoadOverVideos = DownLoadUtil.getDownLoadOver();
                            if (!downLoadOverVideos.contains(data)) {
                                //加入下载完成李恩
                                downLoadOverVideos.add(0, data);
                                DownLoadUtil.saveDownLoadOver(downLoadOverVideos);
                            }
                            adapter.getDatas().remove(data);
                            adapter.notifyItemRemoved(position1);
                            DownLoadUtil.saveDownLoading(adapter.getDatas());
                            break;
                        }
                        position1++;
                    }
                }
            }
        });
    }

    private IDownloadInfosCallback mInfosCallback =
            new IDownloadInfosCallback() {
                @Override
                public void onDownloadInfos(List<VideoTaskItem> items) {
                    for (VideoTaskItem item : items) {
                        if (item.getTaskState() != VideoTaskState.SUCCESS) {
                            notifyChanged(item);
                        }
                    }
                }
            };

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        VideoDownloadManager.getInstance().fetchDownloadItems(mInfosCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        VideoDownloadManager.getInstance().removeDownloadInfosCallback(mInfosCallback);
        if (null != adapter) {
            DownLoadUtil.saveDownLoading(adapter.getDatas());
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        super.onDestroy();
    }
}
