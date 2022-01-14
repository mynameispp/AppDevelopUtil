package com.ffzxnet.developutil.ui.custom_view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.adapter.BaseRVListAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomViewAdapter extends BaseRVListAdapter<CustomViewAdapterBean> {

    public CustomViewAdapter(List<CustomViewAdapterBean> datas) {
        super(datas);
        setNoBottomView(true);
        setNoEmptyView(true);
    }

    @Override
    public int getMyItemViewType(int position) {
        return getDatas().get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType==1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_view_left, parent, false);
            return new MyHolder(view);
        }else if (viewType==2){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_view_right_top, parent, false);
            return new MyHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_view_right_bottom, parent, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder) holder).setData(getDatas().get(position));
    }

    @Override
    public int onAddTopItemCount() {
        return 0;
    }

    @Override
    public int onAddBottomItemCount() {
        return 0;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_custom_view_title)
        TextView itemCustomViewTitle;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(CustomViewAdapterBean data) {
            itemCustomViewTitle.setText(data.getTitle());
        }
    }
}
