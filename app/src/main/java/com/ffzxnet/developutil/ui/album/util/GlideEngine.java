package com.ffzxnet.developutil.ui.album.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.ffzxnet.developutil.application.GlideApp;
import com.huantansheng.easyphotos.engine.ImageEngine;

import androidx.annotation.NonNull;

public class GlideEngine implements ImageEngine {
    private static GlideEngine glideEngine;

    public static ImageEngine getInstance() {
        GlideEngine ii = glideEngine;
        if (ii == null) {
            synchronized (GlideEngine.class) {
                ii = glideEngine;
                if (ii == null) {
                    ii = new GlideEngine();
                    glideEngine = ii;
                }
            }
        }
        return ii;
    }

    @Override
    public void loadPhoto(@NonNull Context context, @NonNull Uri uri, @NonNull ImageView imageView) {
        GlideApp.with(imageView)
                .load(uri)
                .into(imageView);
    }

    @Override
    public void loadGifAsBitmap(@NonNull Context context, @NonNull Uri gifUri, @NonNull ImageView imageView) {
        GlideApp.with(imageView)
                .asBitmap()
                .load(gifUri)
                .into(imageView);
    }

    @Override
    public void loadGif(@NonNull Context context, @NonNull Uri gifUri, @NonNull ImageView imageView) {
        GlideApp.with(imageView)
                .asGif()
                .load(gifUri)
                .into(imageView);
    }

    @Override
    public Bitmap getCacheBitmap(@NonNull Context context, @NonNull Uri uri, int width, int height) throws Exception {
        return GlideApp.with(context).asBitmap().load(uri).submit(width, height).get();
    }
}
