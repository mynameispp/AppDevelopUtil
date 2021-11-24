package com.ffzxnet.developutil.ui.voice_recorde;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.GlideApp;
import com.ffzxnet.developutil.base.ui.adapter.BaseRVListAdapter;
import com.ffzxnet.developutil.ui.voice_recorde.code.RecorderInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VoiceRecordTestAdapter extends BaseRVListAdapter<RecorderInfo> implements View.OnClickListener {

    public interface AdapterListen {
        void itemVoiceClick(ImageView playAnimaImage, RecorderInfo itemData);
    }

    private AdapterListen adapterListen;

    public VoiceRecordTestAdapter(List<RecorderInfo> datas, AdapterListen adapterListen) {
        super(datas);
        setNoBottomView(true);
        this.adapterListen = adapterListen;
    }

    @Override
    public int getMyItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voice_record, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.setData(getDatas().get(position));

        myViewHolder.itemView.setTag(R.id.tagId_1, getDatas().get(position));
        myViewHolder.itemView.setTag(R.id.tagId_2, myViewHolder.itemVoiceRecordPlayAnima);
        myViewHolder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (adapterListen != null) {
            adapterListen.itemVoiceClick((ImageView) v.getTag(R.id.tagId_2), (RecorderInfo) v.getTag(R.id.tagId_1));
        }
    }

    @Override
    public int onAddTopItemCount() {
        return 0;
    }

    @Override
    public int onAddBottomItemCount() {
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_voice_record_image)
        ImageView itemVoiceRecordImage;
        @BindView(R.id.item_voice_record_play_txt)
        TextView itemVoiceRecordPlayTxt;
        @BindView(R.id.item_voice_record_play_anima)
        ImageView itemVoiceRecordPlayAnima;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(RecorderInfo data) {
            itemVoiceRecordPlayTxt.setText("语音时长：" + data.getVoiceTimeLength() + "秒");

            GlideApp.with(itemVoiceRecordImage)
                    .load("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF")
                    .transform(new CircleCrop())
                    .into(itemVoiceRecordImage);
            //默认状态图片
            itemVoiceRecordPlayAnima.setTag(R.id.tagId_1, R.mipmap.voice_play_anima_white_yellow_4);
            //播放时的动画，也可以是单张图
            itemVoiceRecordPlayAnima.setTag(R.id.tagId_2, R.drawable.voice_play_anima_yellow);
        }
    }
}
