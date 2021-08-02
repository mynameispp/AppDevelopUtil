package com.ffzxnet.developutil.utils.ui.city_dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.ui.adapter.BaseRVListAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 创建者： feifan.pi 在 2017/6/22.
 */

public class CityDialogAdapter extends BaseRVListAdapter<CityDialogBean> implements View.OnClickListener {
    private List<CityDialogBean> cityDialogBeens;
    private ItemClickListen itemClickListen;
    //被选中的item
    private int oldCheckPosition = -1;

    public CityDialogAdapter(List<CityDialogBean> datas, ItemClickListen itemClickListen) {
        super(datas);
        this.itemClickListen = itemClickListen;
    }

    public void changeDatas(List<CityDialogBean> changeDatas) {
        cityDialogBeens.clear();
        oldCheckPosition = -1;
        cityDialogBeens.addAll(changeDatas);
        notifyDataSetChanged();
    }

    public void clearDatas() {
        cityDialogBeens.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getMyItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_city, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        CityDialogBean bean = cityDialogBeens.get(position);
        bean.setAdapterPosition(position);
        myHolder.itemName.setText(bean.getName());
        myHolder.itemName.setTag(bean);
        myHolder.itemName.setOnClickListener(this);

        //交互颜色
        if (oldCheckPosition == position) {
            myHolder.itemName.setTextColor(MyApplication.getColorByResId(R.color.colorAccent));
        } else {
            myHolder.itemName.setTextColor(MyApplication.getColorByResId(R.color.black_66));
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

    @Override
    public void onClick(View v) {
        CityDialogBean bean = (CityDialogBean) v.getTag();
        if (oldCheckPosition != bean.getAdapterPosition()) {
            //改变选中颜色
            int closeCheck = oldCheckPosition;
            oldCheckPosition = bean.getAdapterPosition();
            notifyItemChanged(closeCheck);
            notifyItemChanged(oldCheckPosition);
        }
        itemClickListen.itemCityClcik(bean);
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private TextView itemName;

        public MyHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.item_dialog_city_tv);
        }
    }

    interface ItemClickListen {
        void itemCityClcik(CityDialogBean bean);
    }
}
