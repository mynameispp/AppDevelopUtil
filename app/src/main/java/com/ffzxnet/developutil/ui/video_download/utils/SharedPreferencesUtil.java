package com.ffzxnet.developutil.ui.video_download.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ffzxnet.developutil.application.MyApplication;


/**
 * SharedPreferencesUtil by pi
 */
public class SharedPreferencesUtil {
    /**
     * SharedPreferencesUtil 名称
     */
    private static final String SP_NAME = "uyeek_sharedpreferences";

    public static final String SP_DownLoading_List = "SP_DownLoading_List";
    public static final String SP_DownLoadOver_List = "SP_DownLoadOver_List";

    private static SharedPreferencesUtil instance = new SharedPreferencesUtil();

    public SharedPreferencesUtil() {
    }

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new SharedPreferencesUtil();
        }
    }

    public static SharedPreferencesUtil getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    private SharedPreferences getSp() {
        return MyApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public int getInt(String key, int def) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getInt(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putInt(String key, int val) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putInt(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getLong(String key, long def) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getLong(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putLong(String key, long val) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putLong(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(String key, String def) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getString(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putString(String key, String val) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putString(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(String key, boolean def) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getBoolean(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putBoolean(String key, boolean val) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putBoolean(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除单个数据
     *
     * @param key
     * @return
     */
    public boolean remove(String key) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.remove(key);
                return e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 清除所有本地数据
     *
     * @return
     */
    public boolean clear() {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.clear();
                return e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
