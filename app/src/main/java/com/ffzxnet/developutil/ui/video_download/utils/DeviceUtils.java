package com.ffzxnet.developutil.ui.video_download.utils;

import android.os.Environment;
import android.os.StatFs;

public class DeviceUtils {

    // 获得手机存储内存大小
    public static long getBlocktTotal() {
        String path = Environment.getExternalStorageDirectory().getPath();
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSizeLong();
        long totalBlocks = statFs.getBlockCountLong();
        return (totalBlocks * blockSize / 1024L / 1024L / 1024L);
    }

    // 获得可用的内存
    public static long getBlockUse() {
        String path = Environment.getExternalStorageDirectory().getPath();
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSizeLong();
        long totalBlocks = statFs.getBlockCountLong();
        long availableBlocks = statFs.getAvailableBlocksLong();
        long useBlocks = availableBlocks * blockSize;
        return (useBlocks / 1024L / 1024L / 1024L);
    }

    public static String getBlockSurplus() {
        return getBlockUse() + "GB";
    }
}
