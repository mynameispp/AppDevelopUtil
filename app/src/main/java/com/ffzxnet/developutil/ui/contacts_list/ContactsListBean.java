package com.ffzxnet.developutil.ui.contacts_list;

import android.text.TextUtils;

import com.ffzxnet.developutil.base.net.BaseResponse;
import com.ffzxnet.developutil.ui.contacts_list.common.Abbreviated;
import com.ffzxnet.developutil.ui.contacts_list.common.ContactsUtils;

public class ContactsListBean extends BaseResponse implements Abbreviated, Comparable<ContactsListBean> {
    private String name;
    private String photo;
    private String id;
    //本地辅助字段
    private String mAbbreviation;
    private String mInitial;
    private boolean select;

    @Override
    public String getInitial() {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        this.mAbbreviation = ContactsUtils.getAbbreviation(name);
        this.mInitial = mAbbreviation.substring(0, 1);
        return mInitial;
    }

    @Override
    public int compareTo(ContactsListBean o) {
        if (mAbbreviation.equals(o.mAbbreviation)) {
            return 0;
        }
        boolean flag;
        if ((flag = mAbbreviation.startsWith("#")) ^ o.mAbbreviation.startsWith("#")) {
            return flag ? 1 : -1;
        }
        return getInitial().compareTo(o.getInitial());
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
