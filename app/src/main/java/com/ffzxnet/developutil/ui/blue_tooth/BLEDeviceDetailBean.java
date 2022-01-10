package com.ffzxnet.developutil.ui.blue_tooth;

import com.ffzxnet.developutil.base.net.BaseResponse;

import java.util.UUID;

//蓝牙连接信息
public class BLEDeviceDetailBean extends BaseResponse {
    public static final int TYPE_SERVICE = 0;
    public static final int TYPE_CHARACTER = 1;

    public int type;

    public UUID uuid;

    public UUID service;

    public BLEDeviceDetailBean(int type, UUID uuid, UUID service) {
        this.type = type;
        this.uuid = uuid;
        this.service = service;
    }
}
