package com.ffzxnet.developutil.utils.ui.city_dialog;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ffzxnet.countrymeet.R;
import com.ffzxnet.countrymeet.application.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者： feifan.pi 在 2017/6/22.
 */

public class CityDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<CityDialogBean> cityDialogBeens;
    private ItemClickListen itemClickListen;
    //被选中的item
    private int oldCheckPosition = -1;

    public CityDialogAdapter(List<CityDialogBean> cityDialogBeens, ItemClickListen itemClickListen) {
        this.cityDialogBeens = new ArrayList<>();
        this.cityDialogBeens.addAll(cityDialogBeens);
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_city, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        CityDialogBean bean = cityDialogBeens.get(position);
        bean.setAdapterPosition(position);
        myHolder.itemName.setText(bean.getName());
        myHolder.itemName.setTag(bean);
        myHolder.itemName.setOnClickListener(this);

        //交互颜色
        if (oldCheckPosition == position) {
            myHolder.itemName.setTextColor(ContextCompat.getColor(MyApplication.getContext(), R.color.colorAccent));
        } else {
            myHolder.itemName.setTextColor(ContextCompat.getColor(MyApplication.getContext(), R.color.black_66));
        }
    }

    @Override
    public int getItemCount() {
        return cityDialogBeens.size();
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
