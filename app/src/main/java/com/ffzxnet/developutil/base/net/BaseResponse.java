package com.ffzxnet.developutil.base.net;



import com.ffzxnet.developutil.utils.tools.GsonUtil;

import java.io.Serializable;

/**
 * 创建者： feifan.pi 在 2017/3/6.
 */

public class BaseResponse implements Serializable {
    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
