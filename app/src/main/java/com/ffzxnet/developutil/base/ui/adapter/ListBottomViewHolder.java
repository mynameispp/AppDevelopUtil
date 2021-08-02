package com.ffzxnet.developutil.base.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ffzxnet.developutil.R;

import androidx.recyclerview.widget.RecyclerView;


/**
 * 列表加载更多布局
 * 创建者： feifan.pi 在 2017/7/14.
 */

public class ListBottomViewHolder extends RecyclerView.ViewHolder {
    private TextView msgTv;
    private ImageView loadingPB;

    public ListBottomViewHolder(View itemView) {
        super(itemView);
        msgTv = (TextView) itemView.findViewById(R.id.item_list_no_more_tv);
        loadingPB = (ImageView) itemView.findViewById(R.id.item_list_no_more_progress_bar);
    }

    public void setMsg(String msg) {
        msgTv.setText(msg);
    }

    public void setMsgBg(int resId) {
        msgTv.setBackgroundResource(resId);
        msgTv.setPadding(35, 10, 35, 10);
    }

    public void setMsgColor(int color) {
        msgTv.setTextColor(color);
    }

    /**
     * 显示加载图片或者加载框
     * @param visible true/false
     */
    public void setloadingPBVisible(boolean visible) {
        if (visible) {
            loadingPB.setVisibility(View.VISIBLE);
        } else {
            loadingPB.setVisibility(View.GONE);
        }
    }

    public int getLoadingPBVisible() {
        return loadingPB.getVisibility();
    }
}
