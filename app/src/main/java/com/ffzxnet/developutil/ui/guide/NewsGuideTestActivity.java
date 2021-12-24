package com.ffzxnet.developutil.ui.guide;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.ui.guide.code.NewbieGuide;
import com.ffzxnet.developutil.ui.guide.code.core.Controller;
import com.ffzxnet.developutil.ui.guide.code.listener.OnLayoutInflatedListener;
import com.ffzxnet.developutil.ui.guide.code.listener.OnPageChangedListener;
import com.ffzxnet.developutil.ui.guide.code.model.GuidePage;

import butterknife.BindView;
import butterknife.OnClick;

public class NewsGuideTestActivity extends BaseActivity {
    @BindView(R.id.news_guide_test_1)
    TextView newsGuideTest1;
    @BindView(R.id.news_guide_test_2)
    TextView newsGuideTest2;
    @BindView(R.id.news_guide_test_3)
    Button newsGuideTest3;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_news_guide_test;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "新手引导界面");
        showNewsGuide();
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    @OnClick(R.id.news_guide_test_3)
    public void onViewClicked() {
        showNewsGuide();
    }

    private void showNewsGuide() {
        NewbieGuide.with(this)
                .setLabel("guide1")
//                        .setShowCounts(3)//控制次数
                .alwaysShow(true)//总是显示，调试时可以打开
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(newsGuideTest1)
                        .setLayoutRes(R.layout.view_news_guide)
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, Controller controller) {
                                //改变提醒位置
                                TextView textView = view.findViewById(R.id.view_news_guide_txt);
                                textView.setY(newsGuideTest1.getY() + 100);
                                textView.setText("第一步引导");
                            }
                        }))
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(newsGuideTest2)
                        .setLayoutRes(R.layout.view_news_guide)
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, Controller controller) {
                                TextView textView = view.findViewById(R.id.view_news_guide_txt);
                                RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) textView.getLayoutParams();
                                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                                textView.setY(newsGuideTest2.getY() - newsGuideTest2.getMeasuredHeight() - 200);
                                textView.setText("第二步引导");
                            }
                        }))
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(newsGuideTest3)
                        .setLayoutRes(R.layout.view_news_guide)
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, Controller controller) {
                                TextView textView = view.findViewById(R.id.view_news_guide_txt);
                                RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) textView.getLayoutParams();
                                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                textView.setY(newsGuideTest3.getY() - newsGuideTest3.getMeasuredHeight() - 200);
                                textView.setText("第三步引导");
                            }
                        }))
                .setOnPageChangedListener(new OnPageChangedListener() {
                    @Override
                    public void onPageChanged(int page) {

                    }
                })
                .show();
    }
}
