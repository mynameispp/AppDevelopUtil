package com.ffzxnet.developutil.ui.main.fragment.second;

import android.os.Bundle;
import android.view.View;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseFragment;
import com.ffzxnet.developutil.base.ui.adapter.GridSpacingItemDecoration;
import com.ffzxnet.developutil.ui.album.AlbumActivity;
import com.ffzxnet.developutil.ui.badge.BadgeTestActivity;
import com.ffzxnet.developutil.ui.calendar.CalendarActivity;
import com.ffzxnet.developutil.ui.main.fragment.first.adapter.FirstTestBean;
import com.ffzxnet.developutil.ui.video_download.DownLoadManageActivity;
import com.ffzxnet.developutil.ui.video_play.DKVideoPlayActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class SecondFragment extends BaseFragment implements SecondFragmentAdapter.AdapterListen {

    @BindView(R.id.second_fragment_rv)
    RecyclerView secondFragmentRv;

    private SecondFragmentAdapter adapter;

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

        addMenuData();
    }

    private void addMenuData() {
        List<FirstTestBean> datas = new ArrayList<>();
        FirstTestBean item;

        item = new FirstTestBean();
        item.setTitle("日历");
        item.setContent("日历界面");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);


        item = new FirstTestBean();
        item.setTitle("下载视频");
        item.setContent("下载m3u8视频和合并");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("相册");
        item.setContent("多功能相册");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("视频播放");
        item.setContent("DK视频播放");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("桌面角标");
        item.setContent("桌面角标数字显示");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        adapter = new SecondFragmentAdapter(datas, this);
        secondFragmentRv.setAdapter(adapter);
    }

    @Override
    public void itemClick(FirstTestBean data) {
        if (data.getTitle().contains("日历")) {
            redirectActivity(CalendarActivity.class);
        } else if (data.getTitle().contains("下载")) {
            redirectActivity(DownLoadManageActivity.class);
        } else if (data.getTitle().contains("相册")) {
            redirectActivity(AlbumActivity.class);
        } else if (data.getTitle().contains("视频播放")) {
            redirectActivity(DKVideoPlayActivity.class);
        }else if (data.getTitle().contains("桌面角标")){
            redirectActivity(BadgeTestActivity.class);
        }
    }
}
