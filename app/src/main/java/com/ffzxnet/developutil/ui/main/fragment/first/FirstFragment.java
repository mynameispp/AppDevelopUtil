package com.ffzxnet.developutil.ui.main.fragment.first;

import android.os.Bundle;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class FirstFragment extends BaseFragment implements FirstFragmentAdapter.AdapterListen {
    @BindView(R.id.first_fragment_rv)
    RecyclerView firstFragmentRv;

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

        List<FirstTestBean> datas = new ArrayList<>();
        FirstTestBean item;
        for (int i = 0; i < 20; i++) {
            item = new FirstTestBean();
            item.setTitle("标题" + i);
            item.setContent("内容显示内容显示内容显示内容显示内容显示" + i);
            item.setImage("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF");
            datas.add(item);
        }
        firstFragmentRv.setAdapter(new FirstFragmentAdapter(datas, this));
    }

    @Override
    public void itemClick(FirstTestBean data) {
        ToastUtil.showToastShort(data.getContent());
    }
}
