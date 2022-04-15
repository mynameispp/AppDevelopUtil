package com.ffzxnet.developutil.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.ui.unlock.code.language.LanguageUtil;
import com.ffzxnet.developutil.ui.video_play.my_ijk.MyVideoPlayerFactory;
import com.ffzxnet.developutil.utils.tools.FileUtil;
import com.ffzxnet.developutil.utils.tools.MMKVUtil;
import com.ffzxnet.developutil.utils.tools.ScreenUtils;
import com.ffzxnet.developutil.utils.video_download.VideoDownloadConfig;
import com.ffzxnet.developutil.utils.video_download.VideoDownloadManager;
import com.ffzxnet.developutil.utils.video_download.common.DownloadConstants;
import com.ffzxnet.developutil.utils.video_download.utils.VideoStorageUtils;
import com.inuker.bluetooth.library.BluetoothClient;
import com.mabeijianxi.smallvideorecord2.DeviceUtils;
import com.mabeijianxi.smallvideorecord2.JianXiCamera;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.wind.me.xskinloader.SkinInflaterFactory;

import java.io.File;
import java.util.HashMap;
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
    private static BluetoothClient bluetoothClient;

    public static Context getContext() {
        return mContext;
    }

    //蓝牙
    public static BluetoothClient getBluetoothClient() {
        if (null == bluetoothClient) {
            bluetoothClient = new BluetoothClient(mContext);
        }
        return bluetoothClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (initOOM()) {
//            return;
//        }
        mContext = getApplicationContext();
        //换肤初始化
        SkinInflaterFactory.setFactory(LayoutInflater.from(this));
        //修改app语言
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            language = getResources().getConfiguration().getLocales().get(0);
        } else {
            language = getResources().getConfiguration().locale;
        }
        initLanguage();
        //检查是否同意隐私条款
        checkAgreePrivacyPolicy();

    }

    public void checkAgreePrivacyPolicy() {
        if (!MMKVUtil.getInstance().getBoolean(MMKVUtil.Agree_Privacy_Policy, false)) {
            //用户没有同意隐私政策
            return;
        }
        //手机屏幕基础信息
        MyConstans.Screen_Width = ScreenUtils.getScreenWidth(mContext);
        MyConstans.Screen_Height = ScreenUtils.getScreenHeight(mContext);
        MyConstans.Screen_Status_Height = ScreenUtils.getStatusHeight(mContext);
        //检测应用白名单，用来提醒Activity防劫持提醒
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                AntiHijackingUtil.checkActivity(mContext);
//            }
//        }).start();
        //下载工具初始化
        initDownloadUtil();
        //DK播放器初始化
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                //使用使用自定义IjkPlayer解码
                .setPlayerFactory(MyVideoPlayerFactory.create())
                .build());
        //短视频和压缩
        initSmallVideo();
        //腾讯浏览器
        initTencentWebView();
    }

    //腾讯浏览器
    private void initTencentWebView() {
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
    }

    //短视频和压缩
    private void initSmallVideo() {
        // 设置拍摄视频缓存路径
        File dcim = new File(FileUtil.VideoPath);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                JianXiCamera.setVideoCachePath(dcim + "/local_compress/");
            } else {
                JianXiCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/local_compress/");
            }
        } else {
            JianXiCamera.setVideoCachePath(dcim + "/local_compress/");
        }
        // 初始化拍摄，遇到问题可选择开启此标记，以方便生成日志
        JianXiCamera.initialize(false, null);
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
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initLanguage();
    }

    /**
     * 设置app语言
     */
    private void initLanguage() {
        String language = MMKVUtil.getInstance().getString(MMKVUtil.CURRENT_APP_LANGUAGE, "");
        if (TextUtils.isEmpty(language)) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            LanguageUtil.changeAppLanguage(MyApplication.getContext(), language);
        }
    }

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
