package com.ffzxnet.developutil.ui.scancode.decoding;

import android.content.Intent;
import android.net.Uri;

import com.google.zxing.BarcodeFormat;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

final class DecodeFormatManager {
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");

    static final Vector<BarcodeFormat> PRODUCT_FORMATS = new Vector<>(5);

    static {
        PRODUCT_FORMATS.add(BarcodeFormat.UPC_A);
        PRODUCT_FORMATS.add(BarcodeFormat.UPC_E);
        PRODUCT_FORMATS.add(BarcodeFormat.EAN_13);
        PRODUCT_FORMATS.add(BarcodeFormat.EAN_8);
    }

    static final Vector<BarcodeFormat> ONE_D_FORMATS = new Vector<>(PRODUCT_FORMATS.size() + 4);

    static {
        ONE_D_FORMATS.addAll(PRODUCT_FORMATS);
        ONE_D_FORMATS.add(BarcodeFormat.CODE_39);
        ONE_D_FORMATS.add(BarcodeFormat.CODE_93);
        ONE_D_FORMATS.add(BarcodeFormat.CODE_128);
        ONE_D_FORMATS.add(BarcodeFormat.ITF);
    }

    static final Vector<BarcodeFormat> QR_CODE_FORMATS = new Vector<>(1);

    static {
        QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE);
    }

    static final Vector<BarcodeFormat> DATA_MATRIX_FORMATS = new Vector<>(1);

    static {
        DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX);
    }

    static Vector<BarcodeFormat> parseDecodeFormats(Intent intent) {
        List<String> scanFormats = null;
        String scanFormatsString = intent.getStringExtra("SCAN_FORMATS");
        if (scanFormatsString != null)
            scanFormats = Arrays.asList(COMMA_PATTERN.split(scanFormatsString));
        return parseDecodeFormats(scanFormats, intent.getStringExtra("SCAN_MODE"));
    }

    static Vector<BarcodeFormat> parseDecodeFormats(Uri inputUri) {
        List<String> formats = inputUri.getQueryParameters("SCAN_FORMATS");
        if (formats != null && formats.size() == 1 && formats.get(0) != null)
            formats = Arrays.asList(COMMA_PATTERN.split(formats.get(0)));
        return parseDecodeFormats(formats, inputUri.getQueryParameter("SCAN_MODE"));
    }

    private static Vector<BarcodeFormat> parseDecodeFormats(Iterable<String> scanFormats, String decodeMode) {
        if (scanFormats != null) {
            Vector<BarcodeFormat> formats = new Vector<>();
            try {
                for (String format : scanFormats)
                    formats.add(BarcodeFormat.valueOf(format));
                return formats;
            } catch (IllegalArgumentException illegalArgumentException) {}
        }
        if (decodeMode != null) {
            if ("PRODUCT_MODE".equals(decodeMode))
                return PRODUCT_FORMATS;
            if ("QR_CODE_MODE".equals(decodeMode))
                return QR_CODE_FORMATS;
            if ("DATA_MATRIX_MODE".equals(decodeMode))
                return DATA_MATRIX_FORMATS;
            if ("ONE_D_MODE".equals(decodeMode))
                return ONE_D_FORMATS;
        }
        return null;
    }
}
