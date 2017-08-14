package com.ffzxnet.developutil.utils.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ffzxnet.countrymeet.R;

/**
 * 列表加载更多布局
 * 创建者： feifan.pi 在 2017/7/14.
 */

public class ListNoMoreDataViewHolder extends RecyclerView.ViewHolder {
    private TextView msgTv;

    public ListNoMoreDataViewHolder(View itemView) {
        super(itemView);
        msgTv = (TextView) itemView.findViewById(R.id.item_list_no_more_tv);
    }

    public void setMsg(String msg) {
        msgTv.setText(msg);
    }
}
