package com.ffzxnet.developutil.utils.tools;

import android.content.Context;

import com.ffzxnet.developutil.application.MyApplication;
import com.tencent.mmkv.MMKV;

public class MMKVUtil {
    private static MMKVUtil instance = new MMKVUtil();
    private static MMKV kv;

    //===============================================================================
    //首次进入App
    public static final String Key_First_Open_App="Key_First_Open_App";
    //===============================================================================


    public MMKVUtil() {
        MMKV.initialize(MyApplication.getContext());
        kv = MMKV.defaultMMKV();
    }

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new MMKVUtil();
        }
    }

    public static MMKVUtil getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    public static void getInit(Context context) {
        MMKV.initialize(context);
    }

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
