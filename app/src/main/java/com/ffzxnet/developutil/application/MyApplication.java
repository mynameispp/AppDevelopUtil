package com.ffzxnet.developutil.application;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.ui.video_play.my_ijk.MyVideoPlayerFactory;
import com.ffzxnet.developutil.utils.tools.ScreenUtils;
import com.ffzxnet.developutil.utils.video_download.VideoDownloadConfig;
import com.ffzxnet.developutil.utils.video_download.VideoDownloadManager;
import com.ffzxnet.developutil.utils.video_download.common.DownloadConstants;
import com.ffzxnet.developutil.utils.video_download.utils.VideoStorageUtils;

import java.io.File;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import xyz.doikki.videoplayer.player.VideoViewConfig;
import xyz.doikki.videoplayer.player.VideoViewManager;

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
        super.onCreate();
//        if (initOOM()) {
//            return;
//        }
        mContext = getApplicationContext();
        //修改app语言
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            language = getResources().getConfiguration().getLocales().get(0);
        } else {
            language = getResources().getConfiguration().locale;
        }
        //手机屏幕基础信息
        MyConstans.Screen_Width = ScreenUtils.getScreenWidth(mContext);
        MyConstans.Screen_Height = ScreenUtils.getScreenHeight(mContext);
        MyConstans.Screen_Status_Height = ScreenUtils.getStatusHeight(mContext);

        //下载工具初始化
        initDownloadUtil();

        //DK播放器初始化
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                //使用使用自定义IjkPlayer解码
                .setPlayerFactory(MyVideoPlayerFactory.create())
                .build());
    }

    //下载工具初始化
    private void initDownloadUtil() {
        File file = VideoStorageUtils.getVideoCacheDir(this);
        if (!file.exists()) {
            file.mkdir();
        }
        VideoDownloadConfig config = new VideoDownloadManager.Build(this)
                .setCacheRoot(file.getAbsolutePath())
                .setTimeOut(DownloadConstants.READ_TIMEOUT, DownloadConstants.CONN_TIMEOUT)
                .setConcurrentCount(1)//下载总数
                .setIgnoreCertErrors(false)
                .setShouldM3U8Merged(false)
                .buildConfig();
        VideoDownloadManager.getInstance().initConfig(config);
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
