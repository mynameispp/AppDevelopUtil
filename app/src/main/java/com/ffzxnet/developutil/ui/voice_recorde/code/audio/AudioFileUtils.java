package com.ffzxnet.developutil.ui.voice_recorde.code.audio;

import android.text.TextUtils;

import com.ffzxnet.developutil.utils.tools.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理录音文件的类
 */

public class AudioFileUtils {

    public static String getPcmFileAbsolutePath(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            throw new NullPointerException("fileName isEmpty");
        }
        String mAudioRawPath = "";
        if (!fileName.endsWith(".pcm")) {
            fileName = fileName + ".pcm";
        }
        String fileBasePath = FileUtil.AudioRecorderPcmPath;
        File file = new File(fileBasePath);
        //创建目录
        if (!file.exists()) {
            file.mkdirs();
        }
        mAudioRawPath = fileBasePath + fileName;

        return mAudioRawPath;
    }

    public static String getWavFileAbsolutePath(String fileName) {
        if (fileName == null) {
            throw new NullPointerException("fileName can't be null");
        }
        String mAudioWavPath = "";
        if (!fileName.endsWith(".wav")) {
            fileName = fileName + ".wav";
        }
        String fileBasePath = FileUtil.AudioRecorderWavPath;
        File file = new File(fileBasePath);
        //创建目录
        if (!file.exists()) {
            file.mkdirs();
        }
        mAudioWavPath = fileBasePath + fileName;
        return mAudioWavPath;
    }

    /**
     * 获取全部pcm文件列表
     *
     * @return
     */
    public static List<File> getPcmFiles() {
        List<File> list = new ArrayList<>();
        String fileBasePath = FileUtil.AudioRecorderPcmPath;

        File rootFile = new File(fileBasePath);
        if (!rootFile.exists()) {
        } else {

            File[] files = rootFile.listFiles();
            for (File file : files) {
                list.add(file);
            }
        }
        return list;
    }

    /**
     * 获取全部wav文件列表
     *
     * @return
     */
    public static List<File> getWavFiles() {
        List<File> list = new ArrayList<>();
        String fileBasePath = FileUtil.AudioRecorderWavPath;

        File rootFile = new File(fileBasePath);
        if (!rootFile.exists()) {
        } else {
            File[] files = rootFile.listFiles();
            for (File file : files) {
                list.add(file);
            }
        }
        return list;
    }
}
