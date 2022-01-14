package com.ffzxnet.developutil.ui.custom_view;

import android.os.Bundle;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.GlideApp;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.utils.ui.RippleView;
import com.ffzxnet.developutil.utils.ui.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class CustomViewActivity extends BaseActivity {
    @BindView(R.id.round_image_view_round)
    RoundImageView roundImageViewRound;
    @BindView(R.id.round_image_view_round_fix)
    RoundImageView roundImageViewRoundFix;
    @BindView(R.id.round_image_view_circle_borde)
    RoundImageView roundImageViewCircleBorde;
    @BindView(R.id.ripple_view)
    RippleView rippleView;
    @BindView(R.id.ripple_view2)
    RippleView rippleView2;
    @BindView(R.id.round_image_view_circle)
    RoundImageView roundImageViewCircle;
    @BindView(R.id.custom_view_left_rv)
    RecyclerView customViewLeftRv;
    @BindView(R.id.custom_view_right_top_rv)
    RecyclerView customViewRightTopRv;
    @BindView(R.id.custom_view_right_bottom_rv)
    RecyclerView customViewRightBottomRv;

    RecyclerView.OnScrollListener scrollListeners1;
    RecyclerView.OnScrollListener scrollListeners2;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_custom_view;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "自定义View");

        String imageUrl = "https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF";

        GlideApp.with(roundImageViewCircle)
                .load(imageUrl)
                .into(roundImageViewCircle);
        GlideApp.with(roundImageViewRound)
                .load(imageUrl)
                .into(roundImageViewRound);
        GlideApp.with(roundImageViewRoundFix)
                .load(imageUrl)
                .into(roundImageViewRoundFix);
        GlideApp.with(roundImageViewCircleBorde)
                .load(imageUrl)
                .into(roundImageViewCircleBorde);

        //=============================================
        List<CustomViewAdapterBean> leftData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CustomViewAdapterBean item = new CustomViewAdapterBean();
            item.setTitle("测试" + i);
            item.setType(1);
            leftData.add(item);
        }
        CustomViewAdapter adapter1 = new CustomViewAdapter(leftData);

        List<CustomViewAdapterBean> rightTopData = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            CustomViewAdapterBean item = new CustomViewAdapterBean();
            item.setTitle("Top" + i);
            item.setType(2);
            rightTopData.add(item);
        }
        CustomViewAdapter adapter2 = new CustomViewAdapter(rightTopData);

        List<CustomViewAdapterBean> rightBottomData = new ArrayList<>();
        for (int i = 0; i < 230; i++) {
            CustomViewAdapterBean item = new CustomViewAdapterBean();
            item.setTitle("Bottom" + i);
            item.setType(3);
            rightBottomData.add(item);
        }
        CustomViewAdapter adapter3 = new CustomViewAdapter(rightBottomData);

        customViewLeftRv.setNestedScrollingEnabled(false);
        customViewLeftRv.setLayoutManager(new LinearLayoutManager(this));
        customViewRightTopRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        customViewRightBottomRv.setLayoutManager(new GridLayoutManager(this, 23));

        scrollListeners1 = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                customViewRightBottomRv.removeOnScrollListener(scrollListeners2);
                customViewRightBottomRv.scrollBy(dx, dy);
                customViewRightBottomRv.addOnScrollListener(scrollListeners2);
            }
        };
        scrollListeners2 = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                customViewLeftRv.removeOnScrollListener(scrollListeners1);
                customViewLeftRv.scrollBy(dx, dy);
                customViewLeftRv.addOnScrollListener(scrollListeners1);
            }
        };
        customViewLeftRv.addOnScrollListener(scrollListeners1);
        customViewRightBottomRv.addOnScrollListener(scrollListeners2);

        customViewLeftRv.setAdapter(adapter1);
        customViewRightTopRv.setAdapter(adapter2);
        customViewRightBottomRv.setAdapter(adapter3);
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        rippleView.start();
        rippleView2.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        rippleView.stop();
        rippleView2.stop();
    }
}
