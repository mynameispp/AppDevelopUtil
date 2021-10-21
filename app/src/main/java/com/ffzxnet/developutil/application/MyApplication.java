package com.ffzxnet.developutil.application;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.utils.tools.ScreenUtils;
import com.ffzxnet.developutil.utils.tools.MMKVUtil;

import java.util.Locale;

import androidx.core.content.ContextCompat;

/**
 * 创建者： feifan.pi 在 2017/5/5.
 */

public class MyApplication extends Application {
    //默认中文
    public static Locale language;
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
//        if (initOOM()) {
//            return;
//        }
        mContext = getApplicationContext();
        MMKVUtil.init(mContext);
        //修改app语言
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            language = getResources().getConfiguration().getLocales().get(0);
        } else {
            language = getResources().getConfiguration().locale;
        }
        MyConstans.Screen_Width = ScreenUtils.getScreenWidth(mContext);
        MyConstans.Screen_Height = ScreenUtils.getScreenHeight(mContext);
        MyConstans.Screen_Status_Height = ScreenUtils.getStatusHeight(mContext);
    }


    /**
     * 初始化监听内存溢出框架
     */
//    private boolean initOOM() {
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return true;
//        }
//        LeakCanary.install(this);
//        return false;
//    }

    public static String getStringByResId(int strId) {
        return MyApplication.getContext().getResources().getString(strId);
    }

    public static int getColorByResId(int colorId) {
        return ContextCompat.getColor(MyApplication.getContext(), colorId);
    }

    public static Drawable getDrawableByResId(int drawableId) {
        return ContextCompat.getDrawable(MyApplication.getContext(), drawableId);
    }
}
