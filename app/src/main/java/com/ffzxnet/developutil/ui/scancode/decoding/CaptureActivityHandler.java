package com.ffzxnet.developutil.ui.scancode.decoding;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPointCallback;
import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.ui.scancode.activity.BaseCaptureActivity;
import com.ffzxnet.developutil.ui.scancode.camera.CameraManager;
import com.ffzxnet.developutil.ui.scancode.view.ViewfinderResultPointCallback;

import java.util.Vector;

public final class CaptureActivityHandler extends Handler {
    private static final String TAG = CaptureActivityHandler.class.getSimpleName();

    private final BaseCaptureActivity activity;

    private final DecodeThread decodeThread;

    private State state;

    private enum State {
        PREVIEW, SUCCESS, DONE;
    }

    public CaptureActivityHandler(BaseCaptureActivity activity, Vector<BarcodeFormat> decodeFormats, String characterSet) {
        this.activity = activity;
        this
                .decodeThread = new DecodeThread(activity, decodeFormats, characterSet, (ResultPointCallback)new ViewfinderResultPointCallback(activity.getViewfinderView()));
        this.decodeThread.start();
        this.state = State.SUCCESS;
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    public void handleMessage(Message message) {
        int what = message.what;
        if (what == R.id.auto_focus) {
            if (this.state == State.PREVIEW)
                CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
        } else if (what == R.id.restart_preview) {
            Log.d(TAG, "Got restart preview message");
            restartPreviewAndDecode();
        } else if (what == R.id.decode_succeeded) {
            Log.d(TAG, "Got decode succeeded message");
            this.state = State.SUCCESS;
            Bundle bundle = message.getData();
            Bitmap barcode = (bundle == null) ? null : (Bitmap)bundle.getParcelable("barcode_bitmap");
            this.activity.handleDecode((Result)message.obj, barcode);
        } else if (what == R.id.decode_failed) {
            this.state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(this.decodeThread.getHandler(), R.id.decode);
        } else if (what == R.id.return_scan_result) {
            Log.d(TAG, "Got return scan result message");
            this.activity.setResult(-1, (Intent)message.obj);
            this.activity.finish();
        } else if (what == R.id.launch_product_query) {
            Log.d(TAG, "Got product query message");
            String url = (String)message.obj;
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            intent.addFlags(524288);
            this.activity.startActivity(intent);
        }
    }

    public void quitSynchronously() {
        this.state = State.DONE;
        CameraManager.get().stopPreview();
        Message quit = Message.obtain(this.decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();
        try {
            this.decodeThread.join();
        } catch (InterruptedException interruptedException) {}
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    private void restartPreviewAndDecode() {
        if (this.state == State.SUCCESS) {
            this.state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(this.decodeThread.getHandler(), R.id.decode);
            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            this.activity.drawViewfinder();
        }
    }
}
