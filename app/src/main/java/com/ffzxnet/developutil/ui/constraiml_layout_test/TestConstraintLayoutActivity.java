package com.ffzxnet.developutil.ui.constraiml_layout_test;

import android.os.Bundle;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;

public class TestConstraintLayoutActivity extends BaseActivity {
    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_constraint_layout;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "ConstraintLayout布局使用");
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }
}
