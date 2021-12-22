package com.ffzxnet.developutil.ui.constraint_layout_test;

import android.os.Bundle;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.DefaultSort;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;

import static com.willowtreeapps.spruce.exclusion.ExclusionHelper.NORMAL_MODE;

public class TestConstraintLayoutActivity extends BaseActivity {
    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_constraint_layout;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "ConstraintLayout布局使用");
        ConstraintLayout constraintLayout = findViewById(R.id.constraint_layout_root);
//        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                constraintLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                List<Integer> ids = new ArrayList<>();
//                ids.add(R.id.toolbar_title);
//                new Spruce
//                        .SpruceBuilder(constraintLayout)
//                        .sortWith(new DefaultSort(50))
//                        .excludeViews(ids, NORMAL_MODE)//不需要动画的item id
//                        .animateWith(DefaultAnimations.dynamicFadeIn(constraintLayout),
//                                DefaultAnimations.dynamicTranslationUpwards(constraintLayout))
//                        .start();
//            }
//        });
        //动画显示Item
        List<Integer> ids = new ArrayList<>();
        ids.add(R.id.toolbar_title);
        ids.add(R.id.title1);
        new Spruce
                .SpruceBuilder(constraintLayout)
                .sortWith(new DefaultSort(20))
                .excludeViews(ids, NORMAL_MODE)//不需要动画的item id
                .animateWith(DefaultAnimations.dynamicFadeIn(constraintLayout),
                        DefaultAnimations.dynamicTranslationUpwards(constraintLayout))
                .start();
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }
}
