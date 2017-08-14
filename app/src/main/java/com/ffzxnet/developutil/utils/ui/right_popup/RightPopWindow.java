package com.ffzxnet.developutil.utils.ui.right_popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.utils.tools.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Whisky on 2017/7/14.
 * toolBar上右边的弹窗
 */

public class RightPopWindow extends PopupWindow {

    private boolean hasAll = false; //是否有全部的选项
    private OnItemClickCallBackListener mOnItemClickCallBackListener;
    private Context mContext;

    public RightPopWindow(Activity context, boolean hasAll, OnItemClickCallBackListener onItemClickListener) {
        super(context);
        this.mContext = context;
        this.hasAll = hasAll;
        this.mOnItemClickCallBackListener = onItemClickListener;

        ListView listView = initListView(context, hasAll);
        //这一句不加会报错
        ((ViewGroup) listView.getParent()).removeView(listView);
        setContentView(listView);
        setWidth(ScreenUtils.dip2px(context, 145));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 外围可点击
        setOutsideTouchable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (mOnItemClickCallBackListener != null) {
                    mOnItemClickCallBackListener.onItemClick(position);
                }
                RightPopWindow.this.dismiss();
            }
        });
    }

    @NonNull
    private ListView initListView(Context context, boolean hasAll) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.right_popup_window, null);
        ListView listView = (ListView) inflate.findViewById(R.id.lv_right_view);
        List<String> groups = new ArrayList<>();
        addItems(groups, hasAll);
        PopupAdapter popupAdapter = new PopupAdapter(groups, context);
        listView.setAdapter(popupAdapter);
        return listView;
    }

    /**
     * 添加条目
     * @param groups
     * @param hasAll
     */
    private void addItems(List<String> groups, boolean hasAll) {
        if (hasAll) {
            groups.add("全部");
        }
        groups.add("谈天说地");
        groups.add("老乡帮衬");
        groups.add("商务合作");
        groups.add("意见投票");
    }

    public void show(View view ){
        this.showAsDropDown(view , -ScreenUtils.dip2px(mContext , 125) , ScreenUtils.dip2px(mContext , 17));
    }

    class PopupAdapter extends BaseAdapter {

        Context mContext;
        List<String> mGroups;

        public PopupAdapter(List<String> groups, Context context) {
            mContext = context;
            mGroups = groups;
        }

        @Override
        public int getCount() {
            if (mGroups != null) {
                return mGroups.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_right_popup_view_list, null);
                holder = new ViewHolder();

                convertView.setTag(holder);

                holder.groupItem = (TextView) convertView.findViewById(R.id.groupItem);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.groupItem.setText(mGroups.get(position));

            return convertView;
        }

        class ViewHolder {
            TextView groupItem;
        }
    }

    public interface OnItemClickCallBackListener {

        void onItemClick(int position);
    }
}
