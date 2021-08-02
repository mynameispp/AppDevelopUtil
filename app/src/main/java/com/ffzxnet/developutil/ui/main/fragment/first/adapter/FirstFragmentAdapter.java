package com.ffzxnet.developutil.ui.main.fragment.first.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.GlideApp;
import com.ffzxnet.developutil.base.ui.adapter.BaseRVListAdapter;
import com.ffzxnet.developutil.utils.tools.ClickTooQucik;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstFragmentAdapter extends BaseRVListAdapter<FirstTestBean> implements View.OnClickListener {

    private AdapterListen mAdapterListen;

    public interface AdapterListen {
        void itemClick(FirstTestBean data);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_test, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder)holder).setData(getDatas().get(position));

        holder.itemView.setTag(getDatas().get(position));
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!ClickTooQucik.isFastClick() && mAdapterListen != null) {
            mAdapterListen.itemClick((FirstTestBean)v.getTag());
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
        @BindView(R.id.item_main_test_image)
        ImageView itemMainTestImage;
        @BindView(R.id.item_main_test_title)
        TextView itemMainTestTitle;
        @BindView(R.id.item_main_test_content)
        TextView itemMainTestContent;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(FirstTestBean data) {
            itemMainTestTitle.setText(data.getTitle());
            itemMainTestContent.setText(data.getContent());

            GlideApp.with(itemMainTestImage)
                    .asBitmap()
                    .load(data.getImage())
                    .placeholder(R.mipmap.icon_default_post_img)
                    .transform(new CircleCrop())
                    .into(itemMainTestImage);
        }
    }
}
