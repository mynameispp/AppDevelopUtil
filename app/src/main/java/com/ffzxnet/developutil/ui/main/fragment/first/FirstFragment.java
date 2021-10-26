package com.ffzxnet.developutil.ui.main.fragment.first;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.ui.BaseFragment;
import com.ffzxnet.developutil.base.ui.adapter.LinearLaySpacingItemDecoration;
import com.ffzxnet.developutil.ui.main.fragment.first.adapter.FirstFragmentAdapter;
import com.ffzxnet.developutil.ui.main.fragment.first.adapter.FirstTestBean;
import com.ffzxnet.developutil.utils.ui.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class FirstFragment extends BaseFragment implements FirstFragmentAdapter.AdapterListen {
    @BindView(R.id.first_fragment_rv)
    RecyclerView firstFragmentRv;

    private FirstFragmentAdapter adapter;
    //是否在刷新
    private boolean refreshingData = false;
    private int itemIndex = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    protected void onMyCreateView(View rootView, Bundle savedInstanceState) {
        firstFragmentRv.setLayoutManager(new LinearLayoutManager(mContext));
        //添加分割线
        firstFragmentRv.addItemDecoration(new LinearLaySpacingItemDecoration(LinearLaySpacingItemDecoration.VERTICAL_LIST,
                1, MyApplication.getColorByResId(R.color.gray_CC)));

        firstFragmentRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    if (lastItemPosition == (layoutManager.getItemCount() - 1)
                            && (adapter != null && !adapter.isNoMoreData()) && !refreshingData) {
                        //监听上拉
                        refreshingData = true;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData();
                            }
                        }, 3000);
                    }
                }
            }
        });

        getData();
    }

    private void getData() {
        List<FirstTestBean> datas = new ArrayList<>();
        FirstTestBean item;
        int size = 20;
        if (adapter != null && adapter.getDatas().size() > 30) {
            //用来测试加载完所有数据
            size = 15;
        }
        for (int i = 0; i < size; i++) {
            item = new FirstTestBean();
            item.setTitle("标题" + itemIndex);
            item.setContent("内容显示内容显示内容显示内容显示内容显示" + itemIndex);
            item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
            datas.add(item);
            itemIndex++;
        }
        if (adapter == null) {
            adapter = new FirstFragmentAdapter(datas, this);
            firstFragmentRv.setAdapter(adapter);
        } else {
            adapter.addDatas(datas);
        }
        refreshingData = false;
    }

    @Override
    public void itemClick(FirstTestBean data) {
        ToastUtil.showToastShort(data.getContent());
    }

    @Override
    public void itemDeleteClick(FirstTestBean data, int position) {
        if (adapter != null && adapter.getDatas().size() > position) {
            adapter.deleteData(position);
        }
    }
}
