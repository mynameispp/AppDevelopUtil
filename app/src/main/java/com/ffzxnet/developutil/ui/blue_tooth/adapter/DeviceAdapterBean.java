package com.ffzxnet.developutil.ui.blue_tooth.adapter;

import com.ffzxnet.developutil.base.net.BaseResponse;

public class DeviceAdapterBean extends BaseResponse {
    private String deviceMAC;
    private String deviceName;
    private int deviceIcon;
    private int status;//连接状态
    private int deviceElectricity;//电量

    public String getDeviceMAC() {
        return deviceMAC;
    }

    public void setDeviceMAC(String deviceMAC) {
        this.deviceMAC = deviceMAC;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getDeviceIcon() {
        return deviceIcon;
    }

    public void setDeviceIcon(int deviceIcon) {
        this.deviceIcon = deviceIcon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeviceElectricity() {
        return deviceElectricity;
    }

    public void setDeviceElectricity(int deviceElectricity) {
        this.deviceElectricity = deviceElectricity;
    }
}
