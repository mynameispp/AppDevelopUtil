package com.ffzxnet.developutil.ui.main.fragment.second;

import android.os.Bundle;
import android.view.View;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseFragment;
import com.ffzxnet.developutil.base.ui.adapter.GridSpacingItemDecoration;
import com.ffzxnet.developutil.ui.calendar.CalendarActivity;
import com.ffzxnet.developutil.ui.main.fragment.first.adapter.FirstFragmentAdapter;
import com.ffzxnet.developutil.ui.main.fragment.first.adapter.FirstTestBean;
import com.ffzxnet.developutil.ui.video_download.DownLoadManageActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class SecondFragment extends BaseFragment implements FirstFragmentAdapter.AdapterListen {

    @BindView(R.id.second_fragment_rv)
    RecyclerView secondFragmentRv;

    private FirstFragmentAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_second;
    }

    @Override
    protected void onMyCreateView(View rootView, Bundle savedInstanceState) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == adapter.getDatas().size()) {
                    //合并底部加载布局，让它显示居中
                    return 2;
                }
                return 1;
            }
        });
        secondFragmentRv.setLayoutManager(gridLayoutManager);
        //添加分割线
        secondFragmentRv.addItemDecoration(new GridSpacingItemDecoration(2, 10, true));

        List<FirstTestBean> datas = new ArrayList<>();
        FirstTestBean item;

        item = new FirstTestBean();
        item.setTitle("日历");
        item.setContent("日历界面");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);


        item = new FirstTestBean();
        item.setTitle("下载视频");
        item.setContent("下载视频和播放");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        adapter = new FirstFragmentAdapter(datas, this);
        secondFragmentRv.setAdapter(adapter);
    }

    @Override
    public void itemClick(FirstTestBean data) {
        if (data.getTitle().contains("日历")) {
            redirectActivity(CalendarActivity.class);
        }if (data.getTitle().contains("下载")) {
            redirectActivity(DownLoadManageActivity.class);
        }
    }
}
