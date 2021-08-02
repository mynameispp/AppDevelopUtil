package com.ffzxnet.developutil.application;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.ffzxnet.developutil.utils.tools.FileUtil;

import androidx.annotation.NonNull;

/**
 * @author by PI
 * describe
 */
@com.bumptech.glide.annotation.GlideModule
public class MyGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        glideBuilder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
        int diskCacheSizeBytes = 1024 * 1024 * 100;// 本地缓存 100 MB
        glideBuilder.setDiskCache(new InternalCacheDiskCacheFactory(context, FileUtil.GlidePathCache, diskCacheSizeBytes));
        glideBuilder.setDefaultRequestOptions(
                new RequestOptions()
                        .format(DecodeFormat.PREFER_RGB_565)
                        .disallowHardwareConfig());
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }


}
