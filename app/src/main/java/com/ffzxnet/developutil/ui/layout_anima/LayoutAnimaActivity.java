package com.ffzxnet.developutil.ui.layout_anima;

import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.GlideApp;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.utils.tools.LogUtil;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.SpruceAnimator;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.ContinuousSort;
import com.willowtreeapps.spruce.sort.ContinuousWeightedSort;
import com.willowtreeapps.spruce.sort.CorneredSort;
import com.willowtreeapps.spruce.sort.DefaultSort;
import com.willowtreeapps.spruce.sort.InlineSort;
import com.willowtreeapps.spruce.sort.LinearSort;
import com.willowtreeapps.spruce.sort.RadialSort;
import com.willowtreeapps.spruce.sort.RandomSort;
import com.willowtreeapps.spruce.sort.SnakeSort;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.willowtreeapps.spruce.exclusion.ExclusionHelper.NORMAL_MODE;
import static com.willowtreeapps.spruce.exclusion.ExclusionHelper.R_L_MODE;

/**
 * 布局动画
 * 添加动画库的同时，需要添加Kotlin的支持，因为部分代码是Kotlin写的，
 * 详情配置参考项目的build和app的build
 */
public class LayoutAnimaActivity extends BaseActivity {
    @BindView(R.id.layout_anima_rv)
    RecyclerView layoutAnimaRv;
    @BindView(R.id.layout_anima_lv)
    LinearLayout layoutAnimaLv;
    @BindView(R.id.layout_anima_rl)
    RelativeLayout layoutAnimaRl;
    @BindView(R.id.image5)
    ImageView image5;
    @BindView(R.id.image6)
    ImageView image6;

    private SpruceAnimator spruceAnimator;
    private LayoutAnimaAdapter adapter;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_layout_anima;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "布局动画");
        //测试数据
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            datas.add("测试数据" + i);
        }
        adapter = new LayoutAnimaAdapter(datas);

        GlideApp.with(image5)
                .load("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF")
                .into(image5);
        GlideApp.with(image6)
                .load("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF")
                .into(image6);
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    @OnClick({R.id.layout_anima_recycler_btn, R.id.layout_anima_recycler_btn2, R.id.layout_anima_recycler_btn3, R.id.layout_anima_recycler_btn4, R.id.layout_anima_recycler_btn5, R.id.layout_anima_recycler_btn6, R.id.layout_anima_recycler_btn7, R.id.layout_anima_recycler_btn8, R.id.layout_anima_recycler_btn9
            , R.id.layout_anima_view_group_btn, R.id.layout_anima_view_group_btn2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_anima_recycler_btn:
                layoutAnimaRv.setVisibility(View.VISIBLE);
                layoutAnimaLv.setVisibility(View.GONE);
                layoutAnimaRl.setVisibility(View.GONE);
                layoutAnimaRv.setLayoutManager(new LinearLayoutManager(this) {
                    @Override
                    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                        super.onLayoutChildren(recycler, state);
                        LogUtil.e("ddddddddd", "onLayoutChildren");
                        spruceAnimator = new Spruce
                                .SpruceBuilder(layoutAnimaRv)
                                .sortWith(new DefaultSort(50))
                                .excludeViews(new ArrayList<Integer>(), R_L_MODE)//不需要动画的item position
                                .animateWith(DefaultAnimations.dynamicFadeIn(layoutAnimaRv),
                                        DefaultAnimations.dynamicTranslationUpwards(layoutAnimaRv))
                                .start();
                    }

                    @Override
                    public void onLayoutCompleted(RecyclerView.State state) {
                        super.onLayoutCompleted(state);
                        LogUtil.e("ddddddddd", "onLayoutCompleted");
                    }
                });
                layoutAnimaRv.setAdapter(adapter);
                break;
            case R.id.layout_anima_recycler_btn2:
                layoutAnimaRv.setLayoutManager(new GridLayoutManager(this, 4) {
                    @Override
                    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                        super.onLayoutChildren(recycler, state);
                        spruceAnimator = new Spruce
                                .SpruceBuilder(layoutAnimaRv)
                                .sortWith(new ContinuousSort(50, false, ContinuousSort.Position.TOP_LEFT))
                                .excludeViews(new ArrayList<Integer>(), R_L_MODE)//不需要动画的item position
                                .animateWith(DefaultAnimations.dynamicFadeIn(layoutAnimaRv),
                                        DefaultAnimations.dynamicTranslationUpwards(layoutAnimaRv))
                                .start();
                    }
                });
                layoutAnimaRv.setAdapter(adapter);
                layoutAnimaRv.setVisibility(View.VISIBLE);
                layoutAnimaLv.setVisibility(View.GONE);
                layoutAnimaRl.setVisibility(View.GONE);

                break;
            case R.id.layout_anima_recycler_btn3:
                layoutAnimaRv.setLayoutManager(new GridLayoutManager(this, 4) {
                    @Override
                    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                        super.onLayoutChildren(recycler, state);
                        spruceAnimator = new Spruce
                                .SpruceBuilder(layoutAnimaRv)
                                .sortWith(new ContinuousWeightedSort(50,
                                        false, RadialSort.Position.TOP_LEFT, 0.5, 0.5))
                                .excludeViews(new ArrayList<Integer>(), R_L_MODE)//不需要动画的item position
                                .animateWith(DefaultAnimations.dynamicFadeIn(layoutAnimaRv),
                                        DefaultAnimations.dynamicTranslationUpwards(layoutAnimaRv))
                                .start();
                    }
                });
                layoutAnimaRv.setAdapter(adapter);
                layoutAnimaRv.setVisibility(View.VISIBLE);
                layoutAnimaLv.setVisibility(View.GONE);
                layoutAnimaRl.setVisibility(View.GONE);
                break;
            case R.id.layout_anima_recycler_btn4:
                layoutAnimaRv.setLayoutManager(new GridLayoutManager(this, 4) {
                    @Override
                    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                        super.onLayoutChildren(recycler, state);
                        spruceAnimator = new Spruce
                                .SpruceBuilder(layoutAnimaRv)
                                .sortWith(new CorneredSort(50, false, CorneredSort.Corner.TOP_LEFT))
                                .excludeViews(new ArrayList<Integer>(), R_L_MODE)//不需要动画的item position
                                .animateWith(DefaultAnimations.dynamicFadeIn(layoutAnimaRv),
                                        DefaultAnimations.dynamicTranslationUpwards(layoutAnimaRv))
                                .start();
                    }
                });
                layoutAnimaRv.setAdapter(adapter);
                layoutAnimaRv.setVisibility(View.VISIBLE);
                layoutAnimaLv.setVisibility(View.GONE);
                layoutAnimaRl.setVisibility(View.GONE);
                break;
            case R.id.layout_anima_recycler_btn5:
                layoutAnimaRv.setLayoutManager(new GridLayoutManager(this, 4) {
                    @Override
                    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                        super.onLayoutChildren(recycler, state);
                        spruceAnimator = new Spruce
                                .SpruceBuilder(layoutAnimaRv)
                                .sortWith(new LinearSort(50, false, LinearSort.Direction.LEFT_TO_RIGHT))
                                .excludeViews(new ArrayList<Integer>(), R_L_MODE)//不需要动画的item position
                                .animateWith(DefaultAnimations.dynamicFadeIn(layoutAnimaRv),
                                        DefaultAnimations.dynamicTranslationUpwards(layoutAnimaRv))
                                .start();
                    }
                });
                layoutAnimaRv.setAdapter(adapter);
                layoutAnimaRv.setVisibility(View.VISIBLE);
                layoutAnimaLv.setVisibility(View.GONE);
                layoutAnimaRl.setVisibility(View.GONE);
                break;
            case R.id.layout_anima_recycler_btn6:
                layoutAnimaRv.setLayoutManager(new GridLayoutManager(this, 4) {
                    @Override
                    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                        super.onLayoutChildren(recycler, state);
                        spruceAnimator = new Spruce
                                .SpruceBuilder(layoutAnimaRv)
                                .sortWith(new InlineSort(50, false, InlineSort.Corner.TOP_LEFT))
                                .excludeViews(new ArrayList<Integer>(), R_L_MODE)//不需要动画的item position
                                .animateWith(DefaultAnimations.dynamicFadeIn(layoutAnimaRv),
                                        DefaultAnimations.dynamicTranslationUpwards(layoutAnimaRv))
                                .start();
                    }
                });
                layoutAnimaRv.setAdapter(adapter);
                layoutAnimaRv.setVisibility(View.VISIBLE);
                layoutAnimaLv.setVisibility(View.GONE);
                layoutAnimaRl.setVisibility(View.GONE);
                break;
            case R.id.layout_anima_recycler_btn7:
                layoutAnimaRv.setLayoutManager(new GridLayoutManager(this, 4) {
                    @Override
                    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                        super.onLayoutChildren(recycler, state);
                        spruceAnimator = new Spruce
                                .SpruceBuilder(layoutAnimaRv)
                                .sortWith(new SnakeSort(50, false, SnakeSort.Corner.TOP_LEFT))
                                .excludeViews(new ArrayList<Integer>(), R_L_MODE)//不需要动画的item position
                                .animateWith(DefaultAnimations.dynamicFadeIn(layoutAnimaRv),
                                        DefaultAnimations.dynamicTranslationUpwards(layoutAnimaRv))
                                .start();
                    }
                });
                layoutAnimaRv.setAdapter(adapter);
                layoutAnimaRv.setVisibility(View.VISIBLE);
                layoutAnimaLv.setVisibility(View.GONE);
                layoutAnimaRl.setVisibility(View.GONE);
                break;
            case R.id.layout_anima_recycler_btn8:
                layoutAnimaRv.setLayoutManager(new GridLayoutManager(this, 4) {
                    @Override
                    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                        super.onLayoutChildren(recycler, state);
                        spruceAnimator = new Spruce
                                .SpruceBuilder(layoutAnimaRv)
                                .sortWith(new RadialSort(50, false, RadialSort.Position.TOP_LEFT))
                                .excludeViews(new ArrayList<Integer>(), R_L_MODE)//不需要动画的item position
                                .animateWith(DefaultAnimations.dynamicFadeIn(layoutAnimaRv),
                                        DefaultAnimations.dynamicTranslationUpwards(layoutAnimaRv))
                                .start();
                    }
                });
                layoutAnimaRv.setAdapter(adapter);
                layoutAnimaRv.setVisibility(View.VISIBLE);
                layoutAnimaLv.setVisibility(View.GONE);
                layoutAnimaRl.setVisibility(View.GONE);
                break;
            case R.id.layout_anima_recycler_btn9:
                layoutAnimaRv.setLayoutManager(new GridLayoutManager(this, 4) {
                    @Override
                    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                        super.onLayoutChildren(recycler, state);
                        spruceAnimator = new Spruce
                                .SpruceBuilder(layoutAnimaRv)
                                .sortWith(new RandomSort(50))
                                .excludeViews(new ArrayList<Integer>(), R_L_MODE)//不需要动画的item position
                                .animateWith(DefaultAnimations.dynamicFadeIn(layoutAnimaRv),
                                        DefaultAnimations.dynamicTranslationUpwards(layoutAnimaRv))
                                .start();
                    }
                });
                layoutAnimaRv.setAdapter(adapter);
                layoutAnimaRv.setVisibility(View.VISIBLE);
                layoutAnimaLv.setVisibility(View.GONE);
                layoutAnimaRl.setVisibility(View.GONE);
                break;
            case R.id.layout_anima_view_group_btn:
                layoutAnimaRv.setVisibility(View.GONE);
                layoutAnimaLv.setVisibility(View.VISIBLE);
                layoutAnimaRl.setVisibility(View.GONE);
                spruceAnimator = new Spruce
                        .SpruceBuilder(layoutAnimaLv)
                        .sortWith(new DefaultSort(100))
                        .excludeViews(new ArrayList<>(), NORMAL_MODE)//不需要动画的item id
                        .animateWith(DefaultAnimations.dynamicFadeIn(layoutAnimaLv),
                                DefaultAnimations.dynamicTranslationUpwards(layoutAnimaLv))
                        .addInterpolator(new LinearInterpolator())
                        .start();
                break;
            case R.id.layout_anima_view_group_btn2:
                layoutAnimaRv.setVisibility(View.GONE);
                layoutAnimaLv.setVisibility(View.GONE);
                layoutAnimaRl.setVisibility(View.VISIBLE);
                spruceAnimator = new Spruce
                        .SpruceBuilder(layoutAnimaRl)
                        .sortWith(new SnakeSort(100, true, SnakeSort.Corner.BOTTOM_RIGHT))
                        .excludeViews(new ArrayList<>(), NORMAL_MODE)//不需要动画的item id
                        .animateWith(DefaultAnimations.dynamicFadeIn(layoutAnimaRl),
                                DefaultAnimations.dynamicTranslationUpwards(layoutAnimaRl))
                        .start();
                break;
        }
    }
}
