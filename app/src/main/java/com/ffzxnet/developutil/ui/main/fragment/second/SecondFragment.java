package com.ffzxnet.developutil.ui.main.fragment.second;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseFragment;
import com.ffzxnet.developutil.base.ui.adapter.GridSpacingItemDecoration;
import com.ffzxnet.developutil.ui.album.AlbumActivity;
import com.ffzxnet.developutil.ui.badge.BadgeTestActivity;
import com.ffzxnet.developutil.ui.blue_tooth.BluetoothActivity;
import com.ffzxnet.developutil.ui.calendar.CalendarActivity;
import com.ffzxnet.developutil.ui.constraint_layout_test.TestConstraintLayoutActivity;
import com.ffzxnet.developutil.ui.contacts_list.ContactsListActivity;
import com.ffzxnet.developutil.ui.custom_view.CustomViewActivity;
import com.ffzxnet.developutil.ui.guide.NewsGuideTestActivity;
import com.ffzxnet.developutil.ui.layout_anima.LayoutAnimaActivity;
import com.ffzxnet.developutil.ui.main.fragment.first.adapter.FirstTestBean;
import com.ffzxnet.developutil.ui.refresh.circle_refresh_layout.CircleRefreshLayoutActivity;
import com.ffzxnet.developutil.ui.refresh.wave_swipe_refresh_layout.WaveSwipeRefreshLayoutActivity;
import com.ffzxnet.developutil.ui.scancode.TestScanCodeActivity;
import com.ffzxnet.developutil.ui.unlock.UnlockActivity;
import com.ffzxnet.developutil.ui.video_download.DownLoadManageActivity;
import com.ffzxnet.developutil.ui.video_play.DKVideoPlayActivity;
import com.ffzxnet.developutil.ui.voice_recorde.VoiceRecordTestActivity;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.RadialSort;

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
        secondFragmentRv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                secondFragmentRv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //动画显示Item
                new Spruce
                        .SpruceBuilder(secondFragmentRv)
                        .sortWith(new RadialSort(50, false, RadialSort.Position.TOP_LEFT))
//                        .excludeViews(new ArrayList<Integer>(), R_L_MODE)//不需要动画的item position
                        .animateWith(DefaultAnimations.dynamicFadeIn(secondFragmentRv),
                                DefaultAnimations.dynamicTranslationUpwards(secondFragmentRv))
                        .start();
            }
        });
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
        item.setContent("多功能相册,视频压缩");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("视频播放");
        item.setContent("DK视频播放");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("桌面角标");
        item.setContent("Logo显示数字角标");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("刷新1");
        item.setContent("顶部刷新-弹起刷新完成收起");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("刷新2");
        item.setContent("顶部刷新-向下水滴动画");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("二维码");
        item.setContent("扫描，生成，本地图片识别");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("解锁");
        item.setContent("手势，指纹解锁，语言切换");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("约束布局");
        item.setContent("ConstraintLayout日常布局使用");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("录音");
        item.setContent("录音，播放本地或远程音频");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("布局动画");
        item.setContent("列表，普通布局动画显示View");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("联系人");
        item.setContent("按首字母分类");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("新手引导");
        item.setContent("第一次使用引导用户使用");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("蓝牙");
        item.setContent("蓝牙搜索，连接，读写，接拒电话，蓝牙控制音乐");
        item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
        datas.add(item);

        item = new FirstTestBean();
        item.setTitle("自定义View");
        item.setContent("圆角图，圆图，波纹等");
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
        } else if (data.getTitle().contains("桌面角标")) {
            redirectActivity(BadgeTestActivity.class);
        } else if (data.getTitle().contains("刷新1")) {
            redirectActivity(CircleRefreshLayoutActivity.class);
        } else if (data.getTitle().contains("刷新2")) {
            redirectActivity(WaveSwipeRefreshLayoutActivity.class);
        } else if (data.getTitle().contains("二维码")) {
            redirectActivity(TestScanCodeActivity.class);
        } else if (data.getTitle().contains("解锁")) {
            redirectActivity(UnlockActivity.class);
        } else if (data.getTitle().contains("约束布局")) {
            redirectActivity(TestConstraintLayoutActivity.class);
        } else if (data.getTitle().contains("录音")) {
            redirectActivity(VoiceRecordTestActivity.class);
        } else if (data.getTitle().contains("布局动画")) {
            redirectActivity(LayoutAnimaActivity.class);
        } else if (data.getTitle().contains("联系人")) {
            redirectActivity(ContactsListActivity.class);
        } else if (data.getTitle().contains("新手引导")) {
            redirectActivity(NewsGuideTestActivity.class);
        } else if (data.getTitle().contains("蓝牙")) {
            redirectActivity(BluetoothActivity.class);
        } else if (data.getTitle().contains("自定义")) {
            redirectActivity(CustomViewActivity.class);
        }
    }
}
