package com.ffzxnet.developutil.ui.contacts_list;

import android.os.Bundle;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.base.ui.adapter.LinearLaySpacingItemDecoration;
import com.ffzxnet.developutil.ui.contacts_list.common.FloatingBarItemDecoration;
import com.ffzxnet.developutil.ui.contacts_list.common.IndexBar;
import com.ffzxnet.developutil.utils.ui.ToastUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 联系人 - 按首字母分类
 */
public class ContactsListActivity extends BaseActivity implements ContactsListAdapter.OnContactsBeanClickListener {
    @BindView(R.id.contacts_list_rv)
    RecyclerView contactsListRv;
    @BindView(R.id.contacts_list_rv_index_bar)
    IndexBar contactsListRvIndexBar;

    private ContactsListAdapter adapter;
    private LinearLayoutManager contactsLayoutManage;
    private LinkedHashMap<Integer, String> mHeaderList;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_contacts_list;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "联系人");

        setData();
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    private void setData() {
        List<ContactsListBean> contactsListBeans = new ArrayList<>();
        ContactsListBean data;
        for (int i = 0; i < 56; i++) {
            data = new ContactsListBean();
            if (i < 10) {
                data.setName("王学生" + i);
            } else if (i < 20) {
                data.setName("林学生" + i);
            } else if (i < 30) {
                data.setName("赵学生" + i);
            } else if (i < 40) {
                data.setName("燕学生" + i);
            } else {
                data.setName("1学生" + i);
            }

            data.setPhoto("https://img1.baidu.com/it/u=3755297117,609162545&fm=26&fmt=auto");
            contactsListBeans.add(data);
        }
        if (adapter == null) {
            adapter = new ContactsListAdapter(contactsListBeans, this);
            contactsLayoutManage = new LinearLayoutManager(this);
            contactsListRv.setLayoutManager(contactsLayoutManage);
            //字母分割标题数据
            mHeaderList = new LinkedHashMap<>();
            int size = contactsListBeans.size();
            List<String> name = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if (!name.contains(contactsListBeans.get(i).getInitial())) {
                    name.add(contactsListBeans.get(i).getInitial());
                    mHeaderList.put(i, contactsListBeans.get(i).getInitial());
                }
            }
            //字母分割标题 End

            contactsListRv.addItemDecoration(new LinearLaySpacingItemDecoration(LinearLaySpacingItemDecoration.VERTICAL_LIST
                    , 1, R.color.gray_CC));
            contactsListRv.addItemDecoration(
                    new FloatingBarItemDecoration(this, mHeaderList
                            , R.color.gray_CC, R.dimen.size_dp_26, R.color.colorPrimaryDark, R.dimen.size_sp_16, R.dimen.size_dp_16));
            contactsListRv.setAdapter(adapter);

            //右边字母列表
            contactsListRvIndexBar.setNavigators(new ArrayList<>(mHeaderList.values()));
            contactsListRvIndexBar.setOnTouchingLetterChangedListener(new IndexBar.OnTouchingLetterChangeListener() {
                @Override
                public void onTouchingLetterChanged(String s) {
                    for (Integer position : mHeaderList.keySet()) {
                        if (s.equals(mHeaderList.get(position))) {
                            contactsLayoutManage.scrollToPositionWithOffset(position, 0);
                            return;
                        }
                    }
                }

                @Override
                public void onTouchingStart(String s) {
                    ToastUtil.showToastShort(s);
                }

                @Override
                public void onTouchingEnd(String s) {
                }
            });
        }
    }

    @Override
    public void onContactsBeanClicked(ContactsListBean data) {

    }
}
