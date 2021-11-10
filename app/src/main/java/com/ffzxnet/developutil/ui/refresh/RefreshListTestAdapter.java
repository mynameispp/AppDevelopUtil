package com.ffzxnet.developutil.ui.refresh;

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

public class RefreshListTestAdapter extends BaseRVListAdapter<String> {

    public RefreshListTestAdapter(List datas) {
        super(datas);
    }

    @Override
    public int getMyItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refresh_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).setData(getDatas().get(position));
    }

    @Override
    public int onAddTopItemCount() {
        return 0;
    }

    @Override
    public int onAddBottomItemCount() {
        return 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_refresh_list_txt)
        TextView itemRefreshListTxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(String data) {
            itemRefreshListTxt.setText(data);
        }
    }
}
