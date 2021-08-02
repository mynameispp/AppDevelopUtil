package com.ffzxnet.developutil.utils.tools;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.ffzxnet.developutil.application.MyApplication;


/**
 * SharedPreferencesUtil by pifeifna
 */
public class SharedPreferencesUtil {
    /**
     * SharedPreferencesUtil 名称
     */
    private static final String SP_NAME = "xiangyu_sharedpreferences";
    /**
     * 第一次使用
     */
    public static final String KEY_FIRST_TIME_USE = "first_time_use";
    /**
     * 用户信息
     */
    public static final String KEY_USER_INFO = "userInfo";
    /**
     * 最近访问的城市
     */
    public static final String KEY_HISTORY_CITYS = "KEY_HISTORY_CITYS";
    /**
     * 省市县地址
     */
    public static final String KEY_CITYS_ADDRESS = "KEY_CITYS_ADDRESS";

    /**
     * 上次刷新帖子列表的时间
     */
    public static final String KEY_START_TIME = "KEY_START_TIME";
    //==================================================================================================

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

    private android.content.SharedPreferences getSp() {
        return MyApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public int getInt(String key, int def) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getInt(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putInt(String key, int val) {
        try {
            android.content.SharedPreferences sp = getSp();
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
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getLong(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putLong(String key, long val) {
        try {
            android.content.SharedPreferences sp = getSp();
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
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getString(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putString(String key, String val) {
        try {
            android.content.SharedPreferences sp = getSp();
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
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getBoolean(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putBoolean(String key, boolean val) {
        try {
            android.content.SharedPreferences sp = getSp();
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
            android.content.SharedPreferences sp = getSp();
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
            android.content.SharedPreferences sp = getSp();
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
