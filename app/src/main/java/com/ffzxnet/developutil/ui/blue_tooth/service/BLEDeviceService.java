package com.ffzxnet.developutil.ui.blue_tooth.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.ui.blue_tooth.BLEDeviceDetailBean;
import com.ffzxnet.developutil.utils.tools.LogUtil;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.List;
import java.util.UUID;

import androidx.annotation.Nullable;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

public class BLEDeviceService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int style = intent.getIntExtra(MyConstans.KEY_TYPE, 0);
        List<BLEDeviceDetailBean> items = (List<BLEDeviceDetailBean>) intent.getSerializableExtra(MyConstans.KEY_LIST_DATA);
        String mac = intent.getStringExtra(MyConstans.KEY_DATA);
        if (style == 1) {
            //开启通知监听
            if (items != null) {
                for (BLEDeviceDetailBean item : items) {

                    if (item.type == BLEDeviceDetailBean.TYPE_CHARACTER) {
//            case R.id.read://读取
                        MyApplication.getBluetoothClient().read(mac, item.service, item.uuid, mReadRsp);
//            case R.id.write://写入
//                    MyApplication.getBluetoothClient().write(mac,item.service, item.uuid,
//                            ByteUtils.stringToBytes(mEtInput.getText().toString()), mWriteRsp);
//            case R.id.notify:
                        MyApplication.getBluetoothClient().notify(mac, item.service, item.uuid, mNotifyRsp);
//            case R.id.unnotify:
//                    MyApplication.getBluetoothClient().unnotify(mac,item.service, item.uuid, mUnnotifyRsp);
                    }
                }
            }
        } else if (style == 2) {
            if (items != null) {
                for (BLEDeviceDetailBean item : items) {
                    if (item.type == BLEDeviceDetailBean.TYPE_CHARACTER) {
//            case R.id.read://读取
//                        MyApplication.getBluetoothClient().read(mac, item.service, item.uuid, mReadRsp);
//            case R.id.write://写入
//                    MyApplication.getBluetoothClient().write(mac,item.service, item.uuid,
//                            ByteUtils.stringToBytes(mEtInput.getText().toString()), mWriteRsp);
//            case R.id.notify:
//                        MyApplication.getBluetoothClient().notify(mac, item.service, item.uuid, mNotifyRsp);
//            case R.id.unnotify: //关闭通知监听
                        MyApplication.getBluetoothClient().unnotify(mac, item.service, item.uuid, mUnnotifyRsp);
                    }
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private final BleReadResponse mReadRsp = new BleReadResponse() {
        @Override
        public void onResponse(int code, byte[] data) {
            if (code == REQUEST_SUCCESS) {
                LogUtil.e("蓝牙设备读取", ByteUtils.byteToString(data));
            } else {
                LogUtil.e("蓝牙设备读取", "失败");
            }
        }
    };

    private final BleWriteResponse mWriteRsp = new BleWriteResponse() {
        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                LogUtil.e("蓝牙设备写入", "成功");
            } else {
                LogUtil.e("蓝牙设备写入", "失败");
            }
        }
    };

    private final BleNotifyResponse mNotifyRsp = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
//            if (service.equals(mService) && character.equals(mCharacter)) {
            LogUtil.e("蓝牙设备通知", "成功 SUUID" + service + "=CUUID=" + character
                    + "===" + ByteUtils.byteToString(value));
//            }
        }

        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                LogUtil.e("蓝牙设备通知", "开启成功");
            } else {
                LogUtil.e("蓝牙设备通知", "开启失败");
            }
        }
    };

    private final BleUnnotifyResponse mUnnotifyRsp = new BleUnnotifyResponse() {
        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                LogUtil.e("蓝牙设备通知", "关闭成功");
            } else {
                LogUtil.e("蓝牙设备通知", "关闭成功");
            }
        }
    };

}
