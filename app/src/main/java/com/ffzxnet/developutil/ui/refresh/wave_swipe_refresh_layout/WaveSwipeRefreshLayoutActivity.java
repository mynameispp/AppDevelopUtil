package com.ffzxnet.developutil.ui.refresh.wave_swipe_refresh_layout;

import android.graphics.Color;
import android.os.Bundle;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.ui.refresh.RefreshListTestAdapter;
import com.ffzxnet.developutil.ui.refresh.wave_swipe_refresh_layout.code.WaveSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class WaveSwipeRefreshLayoutActivity extends BaseActivity implements WaveSwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.wave_refresh_layout_rv)
    RecyclerView waveRefreshLayoutRv;
    @BindView(R.id.wave_refresh_layout)
    WaveSwipeRefreshLayout waveRefreshLayout;

    private RefreshListTestAdapter adapter;
    private int dataIndex = 0;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_wave_refresh_layout;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "刷新2");
        //圆圈颜色
        waveRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        //水滴背景色
//        waveRefreshLayout.setWaveColor(Color.argb(255, 255, 0, 0));
//        waveRefreshLayout.setWaveColor(0xFF000000+new Random().nextInt(0xFFFFFF));
        waveRefreshLayout.setWaveColor(MyApplication.getColorByResId(R.color.colorPrimary));
        //水滴高度
//        float scale = ((new Random().nextFloat()) / 100f);
//        waveRefreshLayout.setMaxDropHeight((int) (waveRefreshLayout.getHeight() * scale));
        //设置刷新监听
        waveRefreshLayout.setOnRefreshListener(this);
        //测试数据
        waveRefreshLayoutRv.setLayoutManager(new LinearLayoutManager(this));
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("测试数据" + dataIndex);
            dataIndex++;
        }
        adapter = new RefreshListTestAdapter(data);
        waveRefreshLayoutRv.setAdapter(adapter);
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    @Override
    public void onRefresh() {
        waveRefreshLayoutRv.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.add(0, "刷新的测试数据" + dataIndex);
                dataIndex++;
                waveRefreshLayoutRv.scrollToPosition(0);
                waveRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }
}
