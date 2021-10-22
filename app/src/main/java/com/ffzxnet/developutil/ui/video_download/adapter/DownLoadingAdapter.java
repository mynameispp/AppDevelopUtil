package com.ffzxnet.developutil.ui.video_download.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.GlideApp;
import com.ffzxnet.developutil.ui.video_download.bean.DownloadVideoInfoBean;
import com.ffzxnet.developutil.utils.video_download.VideoDownloadManager;
import com.ffzxnet.developutil.utils.video_download.model.VideoTaskItem;
import com.ffzxnet.developutil.utils.video_download.model.VideoTaskState;
import com.ffzxnet.developutil.utils.video_download.utils.VideoStorageUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DownLoadingAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private boolean edit_Type;
    private List<DownloadVideoInfoBean> datas;
    private AdapterListen mAdapterListem;
    private boolean isEdit = false;

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public interface AdapterListen {
        void onItemDownLoadingClick(DownloadVideoInfoBean data);
    }

    public void setEdit_Type(boolean b) {
        edit_Type = b;
    }

    public List<DownloadVideoInfoBean> getDatas() {
        return datas;
    }

    public DownLoadingAdapter(List<DownloadVideoInfoBean> datas, AdapterListen mAdapterListem) {
        super();
        if (datas == null) {
            datas = new ArrayList<>();
        }
        this.datas = datas;
        this.mAdapterListem = mAdapterListem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_downloading_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyHolder) holder).setData(datas.get(position), edit_Type);

        holder.itemView.setTag(R.id.tagId_1, datas.get(position));
        holder.itemView.setTag(R.id.tagId_2, position);
        holder.itemView.setTag(R.id.tagId_3, holder);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mAdapterListem != null) {
            DownloadVideoInfoBean data = (DownloadVideoInfoBean) v.getTag(R.id.tagId_1);
            int position = (int) v.getTag(R.id.tagId_2);
            if (edit_Type) {
                data.setSelect(!data.isSelect());
                notifyItemChanged(position);
            } else {
                MyHolder holder = (MyHolder) v.getTag(R.id.tagId_3);
                if (data.getDownLoadUrl().getTaskState() == VideoTaskState.DOWNLOADING) {
                    holder.itemDownloadingInfoSpeed.setText("正在暂停");
                } else {
                    holder.itemDownloadingInfoSpeed.setText("正在连接中");
                }
                mAdapterListem.onItemDownLoadingClick(data);
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_downloading_info_image)
        ImageView itemDownloadingInfoImage;
        @BindView(R.id.item_downloading_info_name)
        TextView itemDownloadingInfoName;
        @BindView(R.id.item_downloading_info_subject_name)
        TextView itemDownloadingInfoSubjectName;
        @BindView(R.id.item_downloading_info_progress)
        ProgressBar itemDownloadingInfoProgress;
        @BindView(R.id.item_downloading_info_speed)
        public TextView itemDownloadingInfoSpeed;
        @BindView(R.id.item_downloading_info_size)
        TextView itemDownloadingInfoSize;
        @BindView(R.id.item_downloading_info_check)
        ImageView itemDownloadingInfoCheck;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setStatus(String message) {
            itemDownloadingInfoSpeed.setText(message);
        }

        private void setData(DownloadVideoInfoBean data, boolean edit_Type) {
            VideoTaskItem videoTaskItem = data.getDownLoadUrl();
            itemDownloadingInfoName.setText(data.getVideoName());
            itemDownloadingInfoSubjectName.setText(data.getSubjectName());
//            setStateText(stateTextView, playBtn, videoTaskItem);
            setDownloadInfoText(data);
            //加载视频图片
            GlideApp.with(itemDownloadingInfoImage)
                    .load(videoTaskItem.getCoverUrl())
                    .into(itemDownloadingInfoImage);

            if (edit_Type) {
                if (data.isSelect()) {
                    itemDownloadingInfoCheck.setImageResource(R.mipmap.icon_edit_video_check_yes);
                } else {
                    itemDownloadingInfoCheck.setImageResource(R.mipmap.icon_edit_video_check_not);
                }
                itemDownloadingInfoCheck.setVisibility(View.VISIBLE);
            } else {
                itemDownloadingInfoCheck.setVisibility(View.GONE);
            }
        }

        private void setStateText(TextView stateView, TextView playBtn, VideoTaskItem item) {
            switch (item.getTaskState()) {
                case VideoTaskState.PENDING:
                case VideoTaskState.PREPARE:
                    playBtn.setVisibility(View.INVISIBLE);
                    stateView.setText("等待中");
                    break;
                case VideoTaskState.START:
                case VideoTaskState.DOWNLOADING:
                    stateView.setText("下载中...");
                    break;
                case VideoTaskState.PAUSE:
                    playBtn.setVisibility(View.INVISIBLE);
                    stateView.setText("下载暂停, 已下载=" + item.getDownloadSizeString());
                    break;
                case VideoTaskState.SUCCESS:
                    playBtn.setVisibility(View.VISIBLE);
                    stateView.setText("下载完成, 总大小=" + item.getDownloadSizeString());
                    break;
                case VideoTaskState.ERROR:
                    playBtn.setVisibility(View.INVISIBLE);
                    stateView.setText("下载错误");
                    break;
                default:
                    playBtn.setVisibility(View.INVISIBLE);
                    stateView.setText("未下载");
                    break;

            }
        }

        public void setDownloadInfoText(DownloadVideoInfoBean data) {
            VideoTaskItem item = data.getDownLoadUrl();
            switch (item.getTaskState()) {
                case VideoTaskState.PENDING:
                    itemDownloadingInfoSpeed.setText("排队中");
                    break;
                case VideoTaskState.PREPARE:
                    itemDownloadingInfoSpeed.setText("下载准备中");
                    break;
                case VideoTaskState.DOWNLOADING:
                    if (item.getTotalSize() == 0) {
                        itemDownloadingInfoSize.setText(item.getDownloadSizeString());
                        data.setVideoSize(item.getDownloadSizeString());
                    } else {
                        itemDownloadingInfoSize.setText(VideoStorageUtils.getSizeStr(item.getTotalSize()));
                    }
                    itemDownloadingInfoSpeed.setText(item.getSpeedString());
                    itemDownloadingInfoProgress.setProgress((int) item.getPercent());
                    Log.e("ddddddd", "进度:" + item.getPercentString() + ", 速度:" + item.getSpeedString()
                            + ", 已下载:" + item.getDownloadSizeString() + ", 大小:" + item.getTotalSize());
                    break;
                case VideoTaskState.SUCCESS:
                    if (item.getTotalSize() == 0) {
                        itemDownloadingInfoSize.setText(data.getVideoSize());
                    } else {
                        itemDownloadingInfoSize.setText(VideoStorageUtils.getSizeStr(item.getTotalSize()));
                    }
                    itemDownloadingInfoSpeed.setText("完成");
                    itemDownloadingInfoProgress.setProgress((int) item.getPercent());
                    Log.e("ddddddd", "完成进度:" + item.getPercentString() + "===" + item.getFilePath());
                    break;
                case VideoTaskState.PAUSE:
                    if (item.getTotalSize() == 0) {
                        itemDownloadingInfoSize.setText(item.getDownloadSizeString());
                    } else {
                        itemDownloadingInfoSize.setText(VideoStorageUtils.getSizeStr(item.getTotalSize()));
                    }
                    itemDownloadingInfoSpeed.setText("暂停");
                    itemDownloadingInfoProgress.setProgress((int) item.getPercent());
                    Log.e("ddddddd", "暂停进度:" + item.getPercentString());
                    break;
                case VideoTaskState.ERROR:
                    VideoDownloadManager.getInstance().pauseDownloadTask(item);
                    itemDownloadingInfoSpeed.setText("下载出错了，点击重试");
                    break;
                case VideoTaskState.ENOSPC:
                    VideoDownloadManager.getInstance().pauseDownloadTask(item.getUrl());
                    itemDownloadingInfoSpeed.setText("手机内存不足");
                    break;
                default:
                    itemDownloadingInfoSpeed.setText("");
                    Log.e("ddddddd", item.getTaskState() + "==下载状态==");
                    break;
            }
        }

    }
}
