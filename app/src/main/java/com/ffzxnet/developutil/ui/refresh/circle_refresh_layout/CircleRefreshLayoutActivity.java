package com.ffzxnet.developutil.ui.refresh.circle_refresh_layout;

import android.os.Bundle;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.ui.refresh.RefreshListTestAdapter;
import com.ffzxnet.developutil.ui.refresh.circle_refresh_layout.code.CircleRefreshLayout;
import com.ffzxnet.developutil.utils.ui.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class CircleRefreshLayoutActivity extends BaseActivity implements CircleRefreshLayout.OnCircleRefreshListener {
    @BindView(R.id.circle_refresh_layout_rv)
    RecyclerView circleRefreshLayoutRv;
    @BindView(R.id.circle_refresh_layout)
    CircleRefreshLayout circleRefreshLayout;

    private RefreshListTestAdapter adapter;
    private int dataIndex = 0;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_circle_refresh_layout;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("","刷新1");
        circleRefreshLayout.setOnRefreshListener(this);
        circleRefreshLayoutRv.setLayoutManager(new LinearLayoutManager(this));
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("测试数据" + dataIndex);
            dataIndex++;
        }
        adapter = new RefreshListTestAdapter(data);
        circleRefreshLayoutRv.setAdapter(adapter);
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    @Override
    public void completeRefresh() {
        ToastUtil.showToastShort("加载完毕");
    }

    @Override
    public void refreshing() {
        circleRefreshLayoutRv.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.add(0,"刷新的测试数据" + dataIndex);
                dataIndex++;
                circleRefreshLayoutRv.scrollToPosition(0);
                circleRefreshLayout.finishRefreshing();
            }
        }, 3000);
    }
}
