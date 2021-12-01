package com.ffzxnet.developutil.ui.scancode.camera;

import android.hardware.Camera;

import com.ffzxnet.developutil.utils.tools.LogUtil;

final class FlashlightManager {

    //参考二维码工具的闪光灯
    public static boolean OpenFlash(Camera camera) {
        //true=打开,false=关闭
        boolean openOrClose;
        try {
            Camera.Parameters parameters = camera.getParameters();
            if (parameters.getFlashMode().equals("torch")) {
                parameters.setFlashMode("off");
                openOrClose = false;
            } else {
                parameters.setFlashMode("torch");
                openOrClose = true;
            }
            camera.setParameters(parameters);
            return openOrClose;
        } catch (Exception e) {
            LogUtil.e("打开闪光灯失败", e.getMessage());
        }
        return false;
    }

    public static void closeFlashLight(Camera camera) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            if (parameters.getFlashMode().equals("off")) {
                return;
            }
            parameters.setFlashMode("off");
            camera.setParameters(parameters);
        } catch (Exception e) {
            LogUtil.e("打开闪光灯失败", e.getMessage());
        }
    }
}

