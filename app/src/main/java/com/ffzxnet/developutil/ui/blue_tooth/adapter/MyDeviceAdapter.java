package com.ffzxnet.developutil.ui.blue_tooth.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.ui.adapter.BaseRVListAdapter;
import com.ffzxnet.developutil.utils.tools.ClickTooQucik;
import com.ffzxnet.developutil.utils.tools.SetTextViewDrawable;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyDeviceAdapter extends BaseRVListAdapter<DeviceAdapterBean> implements View.OnClickListener {
    private AdapterListen adapterListen;

    public interface AdapterListen {
        void itemClick(DeviceAdapterBean data, int position);
    }

    public MyDeviceAdapter(List<DeviceAdapterBean> datas, AdapterListen adapterListen) {
        super(datas);
        this.adapterListen = adapterListen;
    }

    @Override
    public int getMyItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_list, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder) holder).setData(getDatas().get(position));

        holder.itemView.setTag(R.id.tagId_1, getDatas().get(position));
        holder.itemView.setTag(R.id.tagId_2, position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (null == adapterListen || ClickTooQucik.isFastClick()) {
            return;
        }
        adapterListen.itemClick((DeviceAdapterBean) v.getTag(R.id.tagId_1), (Integer) v.getTag(R.id.tagId_2));
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
        @BindView(R.id.item_device_list_image)
        ImageView itemDeviceListImage;
        @BindView(R.id.item_device_list_name)
        TextView itemDeviceListName;
        @BindView(R.id.item_device_list_mac)
        TextView itemDeviceListMac;
        @BindView(R.id.item_device_list_status)
        TextView itemDeviceListStatus;
        @BindView(R.id.item_device_list_electricity)
        ImageView itemDeviceListElectricity;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(DeviceAdapterBean data) {
            itemDeviceListName.setText(data.getDeviceName());
            itemDeviceListMac.setText(data.getDeviceMAC());
//            if (data.getDeviceName().contains("V200")) {
//                itemDeviceListImage.setImageResource(R.mipmap.img_product_v200);
//            } else {
            //设备图片
            itemDeviceListImage.setImageResource(R.mipmap.img_product_band);
//            }
            if (data.getStatus() == 1) {
                itemDeviceListStatus.setText(itemView.getContext().getString(R.string.ble_device_status_1));
                SetTextViewDrawable.setLeftView(itemDeviceListStatus, R.mipmap.icon_device_connect
                        , MyApplication.getColorByResId(R.color.colorPrimary));
                //电量信息
                int electricityIcon;
                if (data.getDeviceElectricity() >= 90) {
                    electricityIcon = R.mipmap.icon_device_electricity_100;

                } else if (data.getDeviceElectricity() > 60) {
                    electricityIcon = R.mipmap.icon_device_electricity_80;
                } else if (data.getDeviceElectricity() > 30) {
                    electricityIcon = R.mipmap.icon_device_electricity_50;
                } else if (data.getDeviceElectricity() > 2) {
                    electricityIcon = R.mipmap.icon_device_electricity_20;
                } else {
                    electricityIcon = R.mipmap.icon_device_electricity_0;
                }
                itemDeviceListElectricity.setImageResource(electricityIcon);
                itemDeviceListElectricity.setVisibility(View.VISIBLE);
            } else {
                itemDeviceListStatus.setText(itemView.getContext().getString(R.string.ble_device_status_0));
                SetTextViewDrawable.setLeftView(itemDeviceListStatus, R.mipmap.icon_device_no_connect);
                itemDeviceListElectricity.setVisibility(View.GONE);
            }

        }
    }
}
