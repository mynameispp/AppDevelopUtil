package com.ffzxnet.developutil.ui.contacts_list.common;

public class ShareContactsBean implements Abbreviated, Comparable<ShareContactsBean> {
    private final String mName;
    private final String mAbbreviation;
    private final String mInitial;

    ShareContactsBean(String name) {
        this.mName = name;
        this.mAbbreviation = ContactsUtils.getAbbreviation(name);
        this.mInitial = mAbbreviation.substring(0, 1);
    }

    @Override
    public String getInitial() {
        return mInitial;
    }

    public String getName() {
        return mName;
    }

    @Override
    public int compareTo(ShareContactsBean r) {
        if (mAbbreviation.equals(r.mAbbreviation)) {
            return 0;
        }
        boolean flag;
        if ((flag = mAbbreviation.startsWith("#")) ^ r.mAbbreviation.startsWith("#")) {
            return flag ? 1 : -1;
        }
        return getInitial().compareTo(r.getInitial());
    }
}