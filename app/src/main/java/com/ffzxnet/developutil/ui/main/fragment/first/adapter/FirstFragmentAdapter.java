package com.ffzxnet.developutil.ui.main.fragment.first.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.GlideApp;
import com.ffzxnet.developutil.base.ui.adapter.BaseRVListAdapter;
import com.ffzxnet.developutil.utils.tools.ClickTooQucik;
import com.ffzxnet.developutil.utils.ui.RoundImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstFragmentAdapter extends BaseRVListAdapter<FirstTestBean> implements View.OnClickListener {

    private AdapterListen mAdapterListen;

    public interface AdapterListen {
        void itemClick(FirstTestBean data);

        void itemDeleteClick(FirstTestBean data, int position);
    }

    public FirstFragmentAdapter(List<FirstTestBean> datas, AdapterListen mAdapterListen) {
        super(datas);
        this.mAdapterListen = mAdapterListen;
    }

    @Override
    public int getMyItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_first_test, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.setData(getDatas().get(position));

        myHolder.itemMainfirstLeftContent.setTag(getDatas().get(position));
        myHolder.itemMainfirstLeftContent.setOnClickListener(this);

        myHolder.itemMainfirstDelete.setTag(R.id.tagId_1, getDatas().get(position));
        myHolder.itemMainfirstDelete.setTag(R.id.tagId_2, position);
        myHolder.itemMainfirstDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!ClickTooQucik.isFastClick() && mAdapterListen != null) {
            if (v.getId() == R.id.item_main_first_left_content) {
                mAdapterListen.itemClick((FirstTestBean) v.getTag());
            } else if (v.getId() == R.id.item_main_first_delete) {
                mAdapterListen.itemDeleteClick((FirstTestBean) v.getTag(R.id.tagId_1), (Integer) v.getTag(R.id.tagId_2));
            }
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

    static class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_main_first_image)
        RoundImageView itemMainfirstImage;
        @BindView(R.id.item_main_first_title)
        TextView itemMainfirstTitle;
        @BindView(R.id.item_main_first_content)
        TextView itemMainfirstContent;
        @BindView(R.id.item_main_first_left_content)
        RelativeLayout itemMainfirstLeftContent;
        @BindView(R.id.item_main_first_delete)
        TextView itemMainfirstDelete;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(FirstTestBean data) {
            itemMainfirstTitle.setText(data.getTitle());
            itemMainfirstContent.setText(data.getContent());

            GlideApp.with(itemMainfirstImage)
                    .asBitmap()
                    .load(data.getImage())
                    .placeholder(R.mipmap.icon_default_post_img)
                    .into(itemMainfirstImage);
        }
    }
}
