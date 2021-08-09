package com.ffzxnet.developutil.ui.for_result_activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ForResultActivity extends BaseActivity {
    @BindView(R.id.for_result_ed)
    EditText forResultEd;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_for_result;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "StartActivityForResult", false);
    }

    @Override
    protected void onClickTitleBack() {
    }

    @OnClick(R.id.for_result_btn)
    public void onViewClicked() {
        //返回值
        Intent result = new Intent();
        result.putExtra("value", forResultEd.getText().toString().trim());
        setResult(110, result);
        finishActivity(this);
    }
}
