package com.ffzxnet.developutil.ui.video_play.cache_video;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.ffzxnet.developutil.utils.tools.FileUtil;

import java.io.File;
import java.lang.ref.SoftReference;

//视频缓存代理
public class VideoProxyCacheManage {
    private static volatile HttpProxyCacheServer cacheServer;

    public synchronized static HttpProxyCacheServer getInstance(Context context) {
        SoftReference<Context> softReference = new SoftReference<>(context);
        HttpProxyCacheServer inst = cacheServer;
        if (inst == null) {
            inst = cacheServer;
            if (inst == null) {
                inst = new HttpProxyCacheServer.Builder(softReference.get())
                        .cacheDirectory(new File(FileUtil.Video))
                        .maxCacheFilesCount(3).build();//最多缓存3个视频
                cacheServer = inst;
            }
        }
        return inst;
    }
}
