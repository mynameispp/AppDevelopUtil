package com.ffzxnet.developutil.ui.scancode.camera;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.view.SurfaceHolder;

import java.io.IOException;

public final class CameraManager {
    private static final String TAG = CameraManager.class.getSimpleName();

    private static final int MIN_FRAME_WIDTH = 240;

    private static final int MIN_FRAME_HEIGHT = 240;

    private static final int MAX_FRAME_WIDTH = 480;

    private static final int MAX_FRAME_HEIGHT = 360;

    private static CameraManager cameraManager;

    static final int SDK_INT;

    private final Context context;

    private final CameraConfigurationManager configManager;

    private Camera camera;

    private Rect framingRect;

    private Rect framingRectInPreview;

    private boolean initialized;

    private boolean previewing;

    private final boolean useOneShotPreviewCallback;

    private final MyCameraPreviewCallback myCameraPreviewCallback;

    private final AutoFocusCallback autoFocusCallback;

    static int sdkInt;

    static {
        try {
            sdkInt = Integer.parseInt(Build.VERSION.SDK);
        } catch (NumberFormatException nfe) {
            sdkInt = 10000;
        }
        SDK_INT = sdkInt;
    }

    public static void init(Context context) {
        if (cameraManager == null)
            cameraManager = new CameraManager(context);
    }

    public static CameraManager get() {
        return cameraManager;
    }

    private CameraManager(Context context) {
        this.context = context;
        this.configManager = new CameraConfigurationManager(context);
        this.useOneShotPreviewCallback = (Integer.parseInt(Build.VERSION.SDK) > 3);
        this.myCameraPreviewCallback = new MyCameraPreviewCallback(this.configManager, this.useOneShotPreviewCallback);
        this.autoFocusCallback = new AutoFocusCallback();
    }

    public void openDriver(SurfaceHolder holder) throws IOException {
        if (this.camera == null) {
            this.camera = Camera.open();
            if (this.camera == null)
                throw new IOException();
            this.camera.setPreviewDisplay(holder);
            if (!this.initialized) {
                this.initialized = true;
                this.configManager.initFromCameraParameters(this.camera);
            }
            this.configManager.setDesiredCameraParameters(this.camera);
        }
    }

    public void closeDriver() {
        if (this.camera != null) {
            FlashlightManager.closeFlashLight(camera);
            this.camera.release();
            this.camera = null;
        }
    }

    public boolean switchFlashLight(){
       return FlashlightManager.OpenFlash(camera);
    }

    public void startPreview() {
        if (this.camera != null && !this.previewing) {
            this.camera.startPreview();
            this.previewing = true;
        }
    }

    public void stopPreview() {
        if (this.camera != null && this.previewing) {
            if (!this.useOneShotPreviewCallback)
                this.camera.setPreviewCallback(null);
            this.camera.stopPreview();
            this.myCameraPreviewCallback.setHandler(null, 0);
            this.autoFocusCallback.setHandler(null, 0);
            this.previewing = false;
        }
    }

    public void requestPreviewFrame(Handler handler, int message) {
        if (this.camera != null && this.previewing) {
            this.myCameraPreviewCallback.setHandler(handler, message);
            if (this.useOneShotPreviewCallback) {
                this.camera.setOneShotPreviewCallback(this.myCameraPreviewCallback);
            } else {
                this.camera.setPreviewCallback(this.myCameraPreviewCallback);
            }
        }
    }

    public void requestAutoFocus(Handler handler, int message) {
        if (this.camera != null && this.previewing) {
            this.autoFocusCallback.setHandler(handler, message);
            this.camera.autoFocus(this.autoFocusCallback);
        }
    }

    public Rect getFramingRect() {
        Point screenResolution = this.configManager.getScreenResolution();
        if (screenResolution == null)
            return null;
        if (this.framingRect == null) {
            if (this.camera == null)
                return null;
            int width = screenResolution.x * 7 / 10;
            int height = screenResolution.y * 7 / 10;
            if (height >= width) {
                height = width;
            } else {
                width = height;
            }
            int leftOffset = (screenResolution.x - width) / 2;//扫码框横向居中
            int topOffset = (screenResolution.y - height) / 2 - 300;//扫码框距离布局中间向上300dp
            this.framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
        }
        return this.framingRect;
    }

    public Rect getFramingRectInPreview() {
        if (this.framingRectInPreview == null) {
            Rect rect = new Rect(getFramingRect());
            Point cameraResolution = this.configManager.getCameraResolution();
            Point screenResolution = this.configManager.getScreenResolution();
            rect.left = rect.left * cameraResolution.y / screenResolution.x;
            rect.right = rect.right * cameraResolution.y / screenResolution.x;
            rect.top = rect.top * cameraResolution.x / screenResolution.y;
            rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
            this.framingRectInPreview = rect;
        }
        return this.framingRectInPreview;
    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = getFramingRectInPreview();
        int previewFormat = this.configManager.getPreviewFormat();
        String previewFormatString = this.configManager.getPreviewFormatString();
        switch (previewFormat) {
            case 16:
            case 17:
                return new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height);
        }
        if ("yuv420p".equals(previewFormatString))
            return new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height);
        throw new IllegalArgumentException("Unsupported picture format: " + previewFormat + '/' + previewFormatString);
    }

    public Context getContext() {
        return this.context;
    }
}
