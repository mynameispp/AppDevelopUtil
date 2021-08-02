package com.ffzxnet.developutil.net;

import com.ffzxnet.developutil.base.net.BaseRequest;

/**
 * Created by Whisky on 2017/8/4.
 */

public class GetGroupSingleRequest extends BaseRequest{

    private String id;
    private String appId;

    public GetGroupSingleRequest(String id , String appId) {
        this.id = id;
        this.appId = appId;
    }
}
