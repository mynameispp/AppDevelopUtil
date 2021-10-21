package com.ffzxnet.developutil.base.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ffzxnet.developutil.R;

import androidx.recyclerview.widget.RecyclerView;


/**
 * 列表加载更多布局
 * 创建者： feifan.pi 在 2017/7/14.
 */

public class ListBottomViewHolder extends RecyclerView.ViewHolder {
    private TextView msgTv;
    private ImageView loadingImg;
    private ProgressBar loadingPB;
    private boolean hasImage = false;

    public ListBottomViewHolder(View itemView) {
        super(itemView);
        msgTv = itemView.findViewById(R.id.item_list_no_more_tv);
        loadingImg = itemView.findViewById(R.id.item_list_no_more_image);
        loadingPB = itemView.findViewById(R.id.item_list_no_more_progress_bar);
    }

    public void setMsg(String msg) {
        msgTv.setText(msg);
    }

    public void setMsgBg(int resId) {
        loadingImg.setImageResource(resId);
//        msgTv.setPadding(35, 10, 35, 10);
        hasImage = true;
    }

    public void setMsgColor(int color) {
        msgTv.setTextColor(color);
    }

    /**
     * 显示加载图片或者加载框
     *
     * @param visible true/false
     */
    public void setloadingPBVisible(boolean visible) {
        if (visible) {
            if (hasImage) {
                loadingImg.setVisibility(View.VISIBLE);
            } else {
                loadingPB.setVisibility(View.VISIBLE);
            }
        } else {
            loadingImg.setVisibility(View.GONE);
            loadingPB.setVisibility(View.GONE);
        }
    }

    public int getLoadingPBVisible() {
        if (hasImage) {
            return loadingImg.getVisibility();
        } else {
            return loadingPB.getVisibility();
        }
    }
}
