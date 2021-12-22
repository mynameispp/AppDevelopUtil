/**
 * Copyright 2017 ChenHao Dendi
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ffzxnet.developutil.ui.contacts_list;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.GlideApp;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.ui.adapter.BaseRVListAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsListAdapter extends BaseRVListAdapter<ContactsListBean> implements View.OnClickListener {

    private List<String> ids;
    private OnContactsBeanClickListener mOnClickListener;

    public interface OnContactsBeanClickListener {
        void onContactsBeanClicked(ContactsListBean data);
    }

    public ContactsListAdapter(List<ContactsListBean> datas, OnContactsBeanClickListener mOnClickListener) {
        super(datas, 1000);
//        setNoBottomView(true);
        ids = new ArrayList<>();
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public int getMyItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_list, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ContactsViewHolder) holder).setData(getDatas().get(position));

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        ContactsListBean data = getDatas().get(position);
        data.setSelect(!data.isSelect());
        if (data.isSelect()) {
            if (!ids.contains(data.getId())) {
                ids.add(data.getId());
            }
        } else {
            ids.remove(data.getId());
        }
        notifyItemChanged(position);
    }

    @Override
    public int onAddTopItemCount() {
        return 0;
    }

    @Override
    public int onAddBottomItemCount() {
        return 0;
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_contact_list_image)
        ImageView itemContactListImage;
        @BindView(R.id.item_contact_list_name)
        TextView itemContactListName;
        @BindView(R.id.item_contact_list_layout)
        LinearLayout itemContactListLayout;

        ContactsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(final ContactsListBean data) {
            if (data.isSelect()) {
                itemContactListLayout.setBackgroundColor(Color.parseColor("#8bb2fa"));
                itemContactListName.setTextColor(MyApplication.getColorByResId(R.color.white));
            } else {
                itemContactListLayout.setBackgroundResource(R.color.white);
                itemContactListName.setTextColor(MyApplication.getColorByResId(R.color.black));
            }
            itemContactListName.setText(data.getName());

            GlideApp.with(itemContactListImage)
                    .load(data.getPhoto())
                    .transform(new CircleCrop())
                    .into(itemContactListImage);
        }
    }

}

