package com.ffzxnet.developutil.ui.scancode.decoding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.LuminanceSource;

import java.io.FileNotFoundException;

public final class RGBLuminanceSource extends LuminanceSource {
    private final byte[] luminances;

    public RGBLuminanceSource(String path) throws FileNotFoundException {
        this(loadBitmap(path));
    }

    public RGBLuminanceSource(Bitmap bitmap) {
        super(bitmap.getWidth(), bitmap.getHeight());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        this.luminances = new byte[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                int pixel = pixels[offset + x];
                int r = pixel >> 16 & 0xFF;
                int g = pixel >> 8 & 0xFF;
                int b = pixel & 0xFF;
                if (r == g && g == b) {
                    this.luminances[offset + x] = (byte) r;
                } else {
                    this.luminances[offset + x] = (byte) (r + g + g + b >> 2);
                }
            }
        }
    }

    public byte[] getRow(int y, byte[] row) {
        if (y < 0 || y >= getHeight())
            throw new IllegalArgumentException("Requested row is outside the image: " + y);
        int width = getWidth();
        if (row == null || row.length < width)
            row = new byte[width];
        System.arraycopy(this.luminances, y * width, row, 0, width);
        return row;
    }

    public byte[] getMatrix() {
        return this.luminances;
    }

    private static Bitmap loadBitmap(String path) throws FileNotFoundException {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap == null)
            throw new FileNotFoundException("Couldn't open " + path);
        return bitmap;
    }
}
