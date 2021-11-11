package com.ffzxnet.developutil.ui.video_play.cache_video;

import android.text.TextUtils;


import androidx.collection.LruCache;
import xyz.doikki.videoplayer.player.ProgressManager;

//保存上次播放位置
public class ProgressManagerImpl extends ProgressManager {

    //保存100条记录
    private static LruCache<Integer, Long> mCache = new LruCache<>(100);

    @Override
    public void saveProgress(String url, long progress) {
        if (TextUtils.isEmpty(url)) return;
        if (progress == 0) {
            clearSavedProgressByUrl(url);
            return;
        }
        mCache.put(url.hashCode(), progress);
    }

    @Override
    public long getSavedProgress(String url) {
        if (TextUtils.isEmpty(url)) return 0;
        Long pro = mCache.get(url.hashCode());
        if (pro == null) return 0;
        return pro;
    }

    public void clearAllSavedProgress() {
        mCache.evictAll();
    }

    public void clearSavedProgressByUrl(String url) {
        mCache.remove(url.hashCode());
    }
}
