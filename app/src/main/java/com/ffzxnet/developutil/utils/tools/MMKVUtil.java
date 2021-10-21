package com.ffzxnet.developutil.utils.tools;

import android.content.Context;

import com.tencent.mmkv.MMKV;

public class MMKVUtil {
    public static void init(Context context) {
        MMKV.initialize(context);
    }

    private static final MMKV kv = MMKV.defaultMMKV();

    public int getInt(String key, int def) {
        return kv.decodeInt(key, def);
    }

    public void putInt(String key, int val) {
        kv.encode(key, val);
    }

    public long getLong(String key, long def) {
        return kv.decodeLong(key, def);
    }

    public void putLong(String key, long val) {
        kv.encode(key, val);
    }

    public String getString(String key, String def) {
        return kv.decodeString(key, def);
    }

    public void putString(String key, String val) {
        kv.encode(key, val);
    }

    public boolean getBoolean(String key, boolean def) {
        return kv.decodeBool(key, def);
    }

    public void putBoolean(String key, boolean val) {
        kv.encode(key, val);
    }

    /**
     * 清除单个数据
     *
     * @param key
     * @return
     */
    public void remove(String key) {
        kv.removeValueForKey(key);
    }

    /**
     * 清除所有本地数据
     *
     * @return
     */
    public void clear() {
        kv.clearAll();
    }
}
