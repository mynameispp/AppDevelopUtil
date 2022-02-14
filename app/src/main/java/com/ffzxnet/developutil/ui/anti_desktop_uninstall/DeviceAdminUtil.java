package com.ffzxnet.developutil.ui.anti_desktop_uninstall;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DeviceAdminUtil {
    public static DeviceAdminUtil deviceAdminUtil;
    private DevicePolicyManager devicePolicyManager;
    public ComponentName componentName;
    private Context context;

    public static synchronized DeviceAdminUtil getInstance() {
        if (deviceAdminUtil == null) {
            deviceAdminUtil = new DeviceAdminUtil();
        }
        return deviceAdminUtil;
    }

    public void init(Context context) {
        this.context = context;
        devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(context, MyAdminReceiver.class);
//        setDeviceAdminActive(true);
    }

    /**
     * 判断App是否获得设备管理权限
     *
     * @return
     */
    public boolean isAdminActive() {
        return devicePolicyManager.isAdminActive(componentName);
    }

    public void lockScreen() {
        if (isAdminActive() && devicePolicyManager != null) {
            devicePolicyManager.lockNow();
        }
    }

    private void setDeviceAdminActive(boolean active) {
        try {
            if (devicePolicyManager != null && componentName != null) {
                Method method = devicePolicyManager.getClass().getDeclaredMethod("setActiveAdmin", ComponentName.class, boolean.class);
                method.setAccessible(true);
                method.invoke(devicePolicyManager, componentName, active);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
