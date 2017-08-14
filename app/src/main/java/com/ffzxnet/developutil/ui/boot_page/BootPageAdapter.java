package com.ffzxnet.developutil.ui.boot_page;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 创建者： feifan.pi 在 2017/7/19.
 */

public class BootPageAdapter extends PagerAdapter{
    List<SimpleDraweeView> images;

    public BootPageAdapter(List<SimpleDraweeView> images) {
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 添加一个 页卡

        container.addView(images.get(position));

        return images.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 删除
        container.removeView(images.get(position));
    }
}
