package com.ffzxnet.developutil.ui.scancode.decoding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.ffzxnet.developutil.ui.scancode.activity.BaseCaptureActivity;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.ui.scancode.camera.CameraManager;
import com.ffzxnet.developutil.ui.scancode.camera.PlanarYUVLuminanceSource;

import java.util.Hashtable;

final class DecodeHandler extends Handler {
    private static final String TAG = DecodeHandler.class.getSimpleName();

    private final BaseCaptureActivity activity;

    private final MultiFormatReader multiFormatReader;

    DecodeHandler(BaseCaptureActivity activity, Hashtable<DecodeHintType, Object> hints) {
        this.multiFormatReader = new MultiFormatReader();
        this.multiFormatReader.setHints(hints);
        this.activity = activity;
    }

    public void handleMessage(Message message) {
        int what = message.what;
        if (what == R.id.decode) {
            decode((byte[]) message.obj, message.arg1, message.arg2);
        } else if (what == R.id.quit) {
            Looper.myLooper().quit();
        }
    }

    private void decode(byte[] data, int width, int height) {
        long start = System.currentTimeMillis();
        Result rawResult = null;
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * height + height - y - 1] = data[x + y * width];
        }
        int tmp = width;
        width = height;
        height = tmp;
        PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(rotatedData, width, height);
        BinaryBitmap bitmap = new BinaryBitmap((Binarizer) new HybridBinarizer((LuminanceSource) source));
        try {
            rawResult = this.multiFormatReader.decodeWithState(bitmap);
        } catch (ReaderException readerException) {

        } finally {
            this.multiFormatReader.reset();
        }
        if (rawResult != null) {
            long end = System.currentTimeMillis();
            Log.d(TAG, "Found barcode (" + (end - start) + " ms):\n" + rawResult.toString());
            Message message = Message.obtain(this.activity.getHandler(), R.id.decode_succeeded, rawResult);
            Bundle bundle = new Bundle();
            bundle.putParcelable("barcode_bitmap", (Parcelable) source.renderCroppedGreyscaleBitmap());
            message.setData(bundle);
            message.sendToTarget();
        } else {
            Message message = Message.obtain(this.activity.getHandler(), R.id.decode_failed);
            message.sendToTarget();
        }
    }
}

