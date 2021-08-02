package com.ffzxnet.developutil.bean;


import com.ffzxnet.developutil.base.net.BaseResponse;

public class UserInfoBean extends BaseResponse {
    private String userId;
    private String userName;
    private String userNickname;
    private int userType;// 0 超级管理员 1学生 2老师 3家长 4其它外部用户 5公司管理员
    private String companyId;
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
