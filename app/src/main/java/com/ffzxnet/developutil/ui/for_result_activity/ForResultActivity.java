package com.ffzxnet.developutil.ui.for_result_activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.base.ui.BaseActivityResultContact;
import com.ffzxnet.developutil.constans.MyConstans;

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
        String inputData = getBundle().getString(MyConstans.Key_Title_Name, "获取失败");
        initToolBar("", inputData, true);
    }

    @Override
    protected void onClickTitleBack() {
       goBackByQuick();
    }

    @OnClick(R.id.for_result_btn)
    public void onViewClicked() {
        //返回值
        Intent result = new Intent();
        result.putExtra(MyConstans.KEY_DATA, forResultEd.getText().toString().trim());
        setResult(BaseActivityResultContact.Code1, result);
        finishActivity(this);
    }
}
