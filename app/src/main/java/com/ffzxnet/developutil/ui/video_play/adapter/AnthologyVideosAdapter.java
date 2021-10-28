package com.ffzxnet.developutil.ui.video_play.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.adapter.BaseRVListAdapter;
import com.ffzxnet.developutil.utils.tools.ClickTooQucik;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AnthologyVideosAdapter extends BaseRVListAdapter<AnthologyVideosBean> implements View.OnClickListener {

    private AdapterListen adapterListen;

    public interface AdapterListen {
        void onAnthologyVideosClick(AnthologyVideosBean data);
    }

    public AnthologyVideosAdapter(List<AnthologyVideosBean> datas, AdapterListen adapterListen) {
        super(datas);
        this.adapterListen = adapterListen;
        setNoBottomView(true);
        setNoEmptyView(true);
    }

    @Override
    public int getMyItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anthology_video, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder) holder).setData(getDatas().get(position));

        holder.itemView.setTag(getDatas().get(position));
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (ClickTooQucik.isFastClick() || null == adapterListen) {
            return;
        }
        adapterListen.onAnthologyVideosClick((AnthologyVideosBean) v.getTag());
    }

    @Override
    public int onAddTopItemCount() {
        return 0;
    }

    @Override
    public int onAddBottomItemCount() {
        return 0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_anthology_video_num)
        TextView itemAnthologyVideoNum;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(AnthologyVideosBean data) {
            itemAnthologyVideoNum.setText(data.getAnthologyTitle());
        }
    }
}
