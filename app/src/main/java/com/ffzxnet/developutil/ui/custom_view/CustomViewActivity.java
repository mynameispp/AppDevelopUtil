package com.ffzxnet.developutil.ui.custom_view;

import android.os.Bundle;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.GlideApp;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.utils.ui.RippleView;
import com.ffzxnet.developutil.utils.ui.RoundImageView;

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
