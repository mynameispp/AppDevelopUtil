package com.ffzxnet.developutil.ui.scancode.tools;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class PathUtil {
    public static String pathPrefix;
    public final static String historyPathName = "/chat/";
    public final static String imagePathName = "/image/";
    public final static String voicePathName = "/voice/";
    public final static String filePathName = "/file/";
    public final static String videoPathName = "/video/";
    public final static String netdiskDownloadPathName = "/netdisk/";
    public final static String meetingPathName = "/meeting/";
    //protected final static String netdiskHost = EaseMob.EASEMOB_STORAGE_URL + "/share/";

    private static File storageDir = null;

    private static PathUtil instance = null;

    private File voicePath = null;
    private File imagePath = null;
    private File historyPath = null;
    private File videoPath = null;
    private File filePath;

    private PathUtil() {
    }

    public static PathUtil getInstance() {
        if (instance == null) {
            instance = new PathUtil();
        }
        return instance;
    }

    //initialize directions used by user data
    public void initDirs(String appKey, String userName, Context applicationContext) {
        String appPackageName = applicationContext.getPackageName();
        pathPrefix = "/Android/data/" + appPackageName + "/";
        voicePath = generateVoicePath(appKey, userName, applicationContext);
        if (!voicePath.exists()) {
            voicePath.mkdirs();
        }
        imagePath = generateImagePath(appKey, userName, applicationContext);
        //System.err.println("image path:" + imagePath.getAbsolutePath());
        if (!imagePath.exists()) {
            imagePath.mkdirs();
        }

        historyPath = generateHistoryPath(appKey, userName, applicationContext);
        if (!historyPath.exists()) {
            historyPath.mkdirs();
        }

        videoPath = generateVideoPath(appKey, userName, applicationContext);
        if(!videoPath.exists()) {
            videoPath.mkdirs();
        }
        filePath = generateFiePath(appKey, userName, applicationContext);
        if(!filePath.exists())
            filePath.mkdirs();
    }

    public File getImagePath() {
        return imagePath;
    }

    public File getVoicePath() {
        return voicePath;
    }
    public File getFilePath() {
        return filePath;
    }

    public File getVideoPath() {
        return videoPath;
    }

    public File getHistoryPath() {
        return historyPath;
    }


    private static File getStorageDir(Context applicationContext) {
        if (storageDir == null) {
            //try to use sd card if possible
            File sdPath = Environment.getExternalStorageDirectory();
            if (sdPath.exists()) {
                return sdPath;
            }
            //use application internal storage instead
            storageDir = applicationContext.getFilesDir();
        }
        return storageDir;
    }

    private static File generateImagePath(String appKey, String userName, Context applicationContext) {
        String path = null;
        if(appKey == null)
            path = pathPrefix + userName + imagePathName;
        else
            path = pathPrefix + appKey + "/" + userName + imagePathName;
        return new File(getStorageDir(applicationContext), path);
    }

    private static File generateVoicePath(String appKey, String userName, Context applicationContext) {
        String path = null;
        if(appKey == null)
            path = pathPrefix + userName + voicePathName;
        else
            path = pathPrefix + appKey + "/" + userName + voicePathName;

        return new File(getStorageDir(applicationContext), path);
    }

    private static File generateFiePath(String appKey, String userName, Context applicationContext){
        String path = null;
        if(appKey == null)
            path = pathPrefix + userName + filePathName;
        else
            path = pathPrefix + appKey + "/" + userName + filePathName;

        return new File(getStorageDir(applicationContext), path);
    }

    private static File generateVideoPath(String appKey, String userName, Context applicationContext){
        String path = null;
        if(appKey == null)
            path = pathPrefix + userName + videoPathName;
        else
            path = pathPrefix + appKey + "/"+ userName + videoPathName;

        return new File(getStorageDir(applicationContext), path);
    }


    private static File generateHistoryPath(String appKey, String userName, Context applicationContext) {
        String path = null;
        if(appKey == null)
            path = pathPrefix + userName + historyPathName;
        else
            path = pathPrefix + appKey + "/" + userName + historyPathName;

        return new File(getStorageDir(applicationContext), path);
    }

    /*
    public static File getPushMessagePath(String appKey, String userId, Context applicationContext) {
        File filepath = new File(getHistoryPath(appKey, userId, applicationContext), userId + File.separator + "PushMsg.db");
        return filepath;
    }*/

    //Create a temp file relative to the file specified. Make sure the temp file is deleted afterward
    public static File getTempPath(File file) {
        return new File(file.getAbsoluteFile()+ ".tmp");
    }
}
