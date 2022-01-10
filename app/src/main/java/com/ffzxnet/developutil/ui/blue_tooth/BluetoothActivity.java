package com.ffzxnet.developutil.ui.blue_tooth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.base.ui.CheckPermissionDialogCallBak;
import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.evenbus.MyEventbus;
import com.ffzxnet.developutil.ui.blue_tooth.adapter.DeviceAdapterBean;
import com.ffzxnet.developutil.ui.blue_tooth.adapter.MyDeviceAdapter;
import com.ffzxnet.developutil.ui.blue_tooth.service.BLEDeviceService;
import com.ffzxnet.developutil.ui.blue_tooth.service.NotificationCollectorService;
import com.ffzxnet.developutil.utils.tools.LogUtil;
import com.ffzxnet.developutil.utils.ui.ToastUtil;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.receiver.listener.BluetoothBondListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import static android.media.AudioManager.STREAM_MUSIC;

public class BluetoothActivity extends BaseActivity implements MyDeviceAdapter.AdapterListen {
    @BindView(R.id.bluetooth_device_rv)
    RecyclerView bluetoothDeviceRv;
    @BindView(R.id.toolbar_right_tv)
    TextView toolbarRightTv;

    private MyDeviceAdapter adapter;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_bluetooth;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        initToolBar("", "蓝牙");
        bluetoothDeviceRv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyDeviceAdapter(null, this);
        adapter.setEmptyMsg("");
        bluetoothDeviceRv.setAdapter(adapter);

        openBLE();
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    @Override
    protected void onClickRightBtn(View view) {
        super.onClickRightBtn(view);
        //停止查找
        if ((Boolean) view.getTag()) {
            toolbarRightTv.setTag(false);
            toolbarRightTv.setText(getStringByResId(R.string.ble_search_star));
            MyApplication.getBluetoothClient().stopSearch();
        } else {
            toolbarRightTv.setTag(true);
            toolbarRightTv.setText(getStringByResId(R.string.ble_search_stop));
            searchDevice();
        }
    }

    private void openBLE() {
        bluetoothDeviceRv.post(new Runnable() {
            @Override
            public void run() {
                if (MyApplication.getBluetoothClient().isBleSupported()) {
                    CheckPermissionDialog(new CheckPermissionDialogCallBak() {
                                              @Override
                                              public void hasPermission(boolean success) {
                                                  if (success) {
                                                      //禁止电池优化
                                                      ignoreBatteryOptimization();
                                                      //获取通知栏使用权限
                                                      String string = Settings.Secure.getString(getContentResolver(),
                                                              "enabled_notification_listeners");
                                                      if (!string.contains(NotificationCollectorService.class.getName())) {
                                                          startActivity(new Intent(
                                                                  "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                                                      }
                                                      //蓝牙
                                                      if (MyApplication.getBluetoothClient().isBluetoothOpened()) {
                                                          //开始搜索
                                                          searchDevice();
                                                      } else {
                                                          //没有打开蓝牙,请求用户打开蓝牙
                                                          MyApplication.getBluetoothClient().registerBluetoothStateListener(mBluetoothStateListener);
                                                          MyApplication.getBluetoothClient().openBluetooth();
                                                      }
                                                  }
                                              }
                                          }, Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ANSWER_PHONE_CALLS
                            , Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            , Manifest.permission.BODY_SENSORS);
                } else {
                    //不支持蓝牙
                    ToastUtil.showToastLong(getString(R.string.ble_search_not_supp));
                }
            }
        });
    }

    //搜素蓝牙
    private void searchDevice() {
        toolbarRightTv.setTag(true);
        toolbarRightTv.setText(getStringByResId(R.string.ble_search_stop));
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 2)   // 先扫BLE设备3次，每次3s
//                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
//                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();

        MyApplication.getBluetoothClient().search(request, new SearchResponse() {
            List<String> deviceMac;
            List<DeviceAdapterBean> DeviceAdapterBeans;

            @Override
            public void onSearchStarted() {
                showLoadingDialog(true, getStringByResId(R.string.ble_searching));
                deviceMac = new ArrayList<>();
                DeviceAdapterBeans = new ArrayList<>();
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                if (!TextUtils.isEmpty(device.getName())
                        && !device.getName().equalsIgnoreCase("null")
                        && !deviceMac.contains(device.getAddress())) {
                    deviceMac.add(device.getAddress());
                    DeviceAdapterBean bean = new DeviceAdapterBean();
                    bean.setDeviceName(device.getName());
                    bean.setDeviceMAC(device.getAddress());
//                    if (device.getAddress().contains("60:C9")) {
//                        Beacon beacon = new Beacon(device.scanRecord);
//                        LogUtil.e("蓝牙设备信息=", String.format("beacon for %s\n%s", device.getAddress(), beacon.toString()));
//                    }
                    DeviceAdapterBeans.add(bean);
                }
                LogUtil.e("蓝牙设备信息=", String.format("beacon for %s", device.getAddress()));
            }

            @Override
            public void onSearchStopped() {
                LogUtil.e("蓝牙设备信息=", "停止");
                toolbarRightTv.setTag(false);
                toolbarRightTv.setText(getStringByResId(R.string.ble_search_star));
                adapter.setEmptyMsg(getStringByResId(R.string.ble_searching_empty));
                adapter.setDatas(DeviceAdapterBeans);
                showLoadingDialog(false);
            }

            @Override
            public void onSearchCanceled() {
                adapter.setEmptyMsg(getStringByResId(R.string.ble_searching_empty));
                adapter.setDatas(DeviceAdapterBeans);
                showLoadingDialog(false);
            }
        });
    }

    @Override
    public void itemClick(DeviceAdapterBean data, int position) {
        //连接或查看设备信息
        if (!TextUtils.isEmpty(MyConstans.BleMAC) && !MyConstans.BleMAC.equalsIgnoreCase(data.getDeviceMAC()) &&
                MyApplication.getBluetoothClient().getConnectStatus(MyConstans.BleMAC) == Constants.STATUS_DEVICE_CONNECTED) {
            //连接其他蓝牙设备前先断开当前连接
            MyApplication.getBluetoothClient().disconnect(MyConstans.BleMAC);
            int size = 0;
            for (DeviceAdapterBean adapterData : adapter.getDatas()) {
                if (adapterData.getDeviceMAC().equalsIgnoreCase(MyConstans.BleMAC)) {
                    adapterData.setStatus(0);
                    adapter.notifyItemChanged(size);
                    break;
                }
                size++;
            }
        }
        if (MyApplication.getBluetoothClient().getConnectStatus(data.getDeviceMAC()) != Constants.STATUS_DEVICE_CONNECTED) {
            //连接设备
            showLoadingDialog(true, getStringByResId(R.string.ble_connection_now));
            BleConnectOptions options = new BleConnectOptions.Builder()
                    .setConnectRetry(3)   // 连接如果失败重试3次
                    .setConnectTimeout(10000)   // 连接超时30s
                    .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                    .setServiceDiscoverTimeout(10000)  // 发现服务超时20s
                    .build();
            //注册连接监听
            MyApplication.getBluetoothClient().registerConnectStatusListener(data.getDeviceMAC(), mBleConnectStatusListener);
            //开始连接
            MyApplication.getBluetoothClient().connect(data.getDeviceMAC(), options, new BleConnectResponse() {
                @Override
                public void onResponse(int code, BleGattProfile ble) {
                    if (code == Constants.REQUEST_SUCCESS) {
                        setGattProfile(data.getDeviceMAC(), ble);
                        if (adapter != null && position < adapter.getDatas().size()) {
                            DeviceAdapterBean data = adapter.getDatas().get(position);
                            data.setStatus(1);
                            adapter.deleteData(position);
                            adapter.add(0, data);
                            adapter.notifyItemRangeChanged(0, position + 1);
                            bluetoothDeviceRv.scrollToPosition(0);
                        }
                    } else {
                        if (adapter != null && position < adapter.getDatas().size()) {
                            adapter.getDatas().get(position).setStatus(0);
                            adapter.notifyItemChanged(position);
                        }
                    }
//                    LogUtil.e("蓝牙连接", code + "==="
//                            + data.getService("0000ff03-0000-1000-8000-00805f9b34fb").getUUID());
                    showLoadingDialog(false);
                }
            });
        } else {
            //进入设备信息
            ToastUtil.showToastLong("进入设备信息");
        }
    }

    //解析连接设备的UUID
    public void setGattProfile(String mac, BleGattProfile profile) {
        List<BLEDeviceDetailBean> items = new ArrayList<>();

        List<BleGattService> services = profile.getServices();

        for (BleGattService service : services) {
            items.add(new BLEDeviceDetailBean(BLEDeviceDetailBean.TYPE_SERVICE, service.getUUID(), null));
            List<BleGattCharacter> characters = service.getCharacters();
            for (BleGattCharacter character : characters) {
                items.add(new BLEDeviceDetailBean(BLEDeviceDetailBean.TYPE_CHARACTER, character.getUuid(), service.getUUID()));
            }
        }
        Intent intent = new Intent(this, BLEDeviceService.class);
        intent.putExtra(MyConstans.KEY_TYPE, 1);
        intent.putExtra(MyConstans.KEY_LIST_DATA, (Serializable) items);
        intent.putExtra(MyConstans.KEY_DATA, mac);
        startService(intent);
    }

    //监听连接蓝牙状态
    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == Constants.STATUS_CONNECTED) {
                MyConstans.BleMAC = mac;
                MyApplication.getBluetoothClient().unregisterConnectStatusListener(mac, mBleConnectStatusListener);
            } else if (status == Constants.STATUS_DISCONNECTED) {
                //断开连接
                MyConstans.BleMAC = "";
            }
        }
    };

    //蓝牙开关监听
    private final BluetoothStateListener mBluetoothStateListener = new BluetoothStateListener() {
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {
            if (openOrClosed) {
                //已打开蓝牙
                searchDevice();
                MyApplication.getBluetoothClient().unregisterBluetoothStateListener(mBluetoothStateListener);
            }
        }

    };
    //蓝牙监听设备配对状态变化
    private final BluetoothBondListener mBluetoothBondListener = new BluetoothBondListener() {
        @Override
        public void onBondStateChanged(String mac, int bondState) {
            // bondState = Constants.BOND_NONE, BOND_BONDING, BOND_BONDED

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //监听设备配对状态变化
        MyApplication.getBluetoothClient().registerBluetoothBondListener(mBluetoothBondListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //取消监听设备配对状态变化
        MyApplication.getBluetoothClient().unregisterBluetoothBondListener(mBluetoothBondListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //断开连接,测试使用
        MyApplication.getBluetoothClient().disconnect(MyConstans.BleMAC);
    }

    /**
     * 忽略电池优化
     */
    private void ignoreBatteryOptimization() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        Boolean hasIgnored = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasIgnored = powerManager.isIgnoringBatteryOptimizations(getPackageName());
            //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
            if (!hasIgnored) {
                //未加入电池优化的白名单 则弹出系统弹窗供用户选择(这个弹窗也是一个页面)
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } else {
                //已加入电池优化的白名单 则进入系统电池优化页面
//                Intent powerUsageIntent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
//                ResolveInfo resolveInfo = getPackageManager().resolveActivity(powerUsageIntent, 0);
//                //判断系统是否有这个页面
//                if (resolveInfo != null) {
//                    startActivity(powerUsageIntent);
//                }
            }
        }
    }

    //===================来电接听和挂断=================================================================================
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void InCallPhone(MyEventbus.InCallEvent event) {
        //来电话了,做出对应操作
        Observable.timer(5, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete() {
                        LogUtil.e("电话监听", "IncomingCallActivity====拒接");
                        rejectCall();
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void acceptCall() {
        //接听电话
        TelecomManager telMag = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
        if (null != telMag) {
            telMag.acceptRingingCall();
        }
    }


    public void rejectCall() {
        //挂断电话
        TelecomManager telMag = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
        if (null != telMag) {
            telMag.endCall();
        }
    }

    //=====================控制手机音乐播放====================================================================
    private void musicClick(int id) {
//        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(
//                Context.AUDIO_SERVICE);
//        int keyCode = 0;
//        switch (id)){
//            case R.id.up:
//                //上一曲
//                keyCode = KeyEvent.KEYCODE_MEDIA_PREVIOUS;
//                break;
//            case R.id.play_pause:
//                //播放暂停
//                keyCode = KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
//                break;
//            case R.id.next:
//                //下一曲
//                keyCode = KeyEvent.KEYCODE_MEDIA_NEXT;
//                break;
//            case R.id.voice_up:
//                //增大音量
//                if (null == audioManager) {
//                    return;
//                }
//                //最大音量
//                int max = audioManager.getStreamMaxVolume(STREAM_MUSIC);
//                //当前音量
//                int current = audioManager.getStreamVolume(STREAM_MUSIC);
//                int add = current + 1;
//                if (add > max) {
//                    add = max;
//                }
//                audioManager.setStreamVolume(STREAM_MUSIC, add, AudioManager.FLAG_SHOW_UI);
//                return;
//            case R.id.voice_down:
//                //减小音量
//                if (null == audioManager) {
//                    return;
//                }
//                //最大音量
//                int max1 = audioManager.getStreamMaxVolume(STREAM_MUSIC);
//                //当前音量
//                int current1 = audioManager.getStreamVolume(STREAM_MUSIC);
//                int down = current1 - 1;
//                if (down < 0) {
//                    down = 0;
//                }
//                audioManager.setStreamVolume(STREAM_MUSIC, down, AudioManager.FLAG_SHOW_UI);
//                return;
//        }
//
//        KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
//        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
//        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
//        sendOrderedBroadcast(intent, null);
//
//        keyEvent = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
//        intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
//        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
//        sendOrderedBroadcast(intent, null);
    }
}
