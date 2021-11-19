package com.ffzxnet.developutil.ui.video_download.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.adapter.BaseRVListAdapter;
import com.ffzxnet.developutil.ui.video_download.bean.DownloadVideoInfoBean;
import com.ffzxnet.developutil.ui.video_download.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DownLoadOverAdapter extends BaseRVListAdapter<DownloadVideoInfoBean> implements View.OnClickListener {

    private AdapterListen mAdapterListem;
    private boolean isEdit = false;

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }


    public interface AdapterListen {
        void onItemDownLoadOverClick(DownloadVideoInfoBean data);
    }

    public DownLoadOverAdapter(List<DownloadVideoInfoBean> datas, AdapterListen mAdapterListem) {
        super(datas);
        this.mAdapterListem = mAdapterListem;
    }

    @Override
    public int getMyItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download_over_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder) holder).setData(getDatas().get(position), isEdit);

        holder.itemView.setTag(R.id.tagId_1, getDatas().get(position));
        holder.itemView.setTag(R.id.tagId_2, holder);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int onAddTopItemCount() {
        return 0;
    }

    @Override
    public int onAddBottomItemCount() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        if (mAdapterListem != null) {
            DownloadVideoInfoBean data = (DownloadVideoInfoBean) v.getTag(R.id.tagId_1);
            if (isEdit) {
                data.setSelect(!data.isSelect());
                MyHolder myHolder = (MyHolder) v.getTag(R.id.tagId_2);
                if (data.isSelect()) {
                    myHolder.itemDownloadOverInfoCheck.setImageResource(R.mipmap.icon_edit_video_check_yes);
                } else {
                    myHolder.itemDownloadOverInfoCheck.setImageResource(R.mipmap.icon_edit_video_check_not);
                }
            } else {
                mAdapterListem.onItemDownLoadOverClick(data);
            }
        }
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_download_over_info_image)
        ImageView itemDownloadOverInfoImage;
        @BindView(R.id.item_download_over_info_progress)
        ProgressBar itemDownloadOverInfoProgress;
        @BindView(R.id.item_download_over_info_last_play_time)
        TextView itemDownloadOverInfoLastPlayTime;
        @BindView(R.id.item_download_over_info_videos_count)
        TextView itemDownloadOverInfoVideosCount;
        @BindView(R.id.item_download_over_info_name)
        TextView itemDownloadOverInfoName;
        @BindView(R.id.item_download_over_info_subject_name)
        TextView itemDownloadOverInfoSubjectName;
        @BindView(R.id.item_download_over_info_size)
        TextView itemDownloadOverInfoSize;
        @BindView(R.id.item_download_over_info_check)
        ImageView itemDownloadOverInfoCheck;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(DownloadVideoInfoBean data, boolean isEdit) {
            itemDownloadOverInfoName.setText(data.getVideoName());
            itemDownloadOverInfoSubjectName.setText(data.getSubjectName());
            itemDownloadOverInfoSize.setText(data.getVideoSize());

            if (data.getLastPlayTime() > 0) {
                String lastTime = TimeUtils.formatTime(data.getLastPlayTime());
                String videoTime = TimeUtils.formatTime(data.getVideoTime());
                itemDownloadOverInfoLastPlayTime.setText(lastTime + "/" + videoTime);
                itemDownloadOverInfoProgress.setProgress((int) (data.getLastPlayTime() / data.getVideoTime()));
            }

            if (data.getVideoCount() > 0) {
                itemDownloadOverInfoVideosCount.setText(data.getVideoCount() + "");
                itemDownloadOverInfoVideosCount.setVisibility(View.VISIBLE);
            } else {
                itemDownloadOverInfoVideosCount.setVisibility(View.GONE);
            }

            if (isEdit) {
                itemDownloadOverInfoCheck.setVisibility(View.VISIBLE);
                if (data.isSelect()) {
                    itemDownloadOverInfoCheck.setImageResource(R.mipmap.icon_edit_video_check_yes);
                } else {
                    itemDownloadOverInfoCheck.setImageResource(R.mipmap.icon_edit_video_check_not);
                }
            } else {
                itemDownloadOverInfoCheck.setVisibility(View.GONE);
            }

            //加载视频图片
            Glide.with(itemDownloadOverInfoImage)
                    .load(data.getVideoImage())
                    .placeholder(R.mipmap.icon_default_video_bg)
                    .into(itemDownloadOverInfoImage);
        }
    }
}
