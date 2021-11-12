package com.ffzxnet.developutil.ui.splash;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.GlideApp;
import com.ffzxnet.developutil.base.ui.adapter.BaseRVListAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

/**
 * 创建者： feifan.pi 在 2017/7/19.
 */

public class SplashPageAdapter extends BaseRVListAdapter<Integer> {
    public SplashPageAdapter(List<Integer> datas) {
        super(datas);
        setNoBottomView(true);
        setNoEmptyView(true);
    }

    @Override
    public int getMyItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_splash, parent, false);
        return new MyHolder(view);
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

    static class MyHolder extends RecyclerView.ViewHolder {
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(int bgId) {
            //https://t7.baidu.com/it/u=1112482012,2228745551&fm=193&f=GIF
            GlideApp.with(itemView)
                    .load("https://t7.baidu.com/it/u=185124239,2801447434&fm=193&f=GIF")
                    .into((ImageView) itemView);
        }
    }
}
