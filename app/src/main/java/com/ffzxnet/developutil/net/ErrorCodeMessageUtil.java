package com.ffzxnet.developutil.net;

import android.util.SparseArray;

/**
 * 根据后台返回的Code显示对应的提示语句
 * 创建者： awa.pi 在 2018/5/15.
 */

public class ErrorCodeMessageUtil {
    public static SparseArray<String> errorCodeMessageMap;

    static {
        //添加提示语句
        errorCodeMessageMap = new SparseArray<>();
        errorCodeMessageMap.put(10032, "你已申请过，请等待群管理员审核");
        errorCodeMessageMap.put(4010, "你的验证码已失效");
    }

    public static String getMessageByErrorCode(int code) {
        return errorCodeMessageMap.get(code);
    }
}
