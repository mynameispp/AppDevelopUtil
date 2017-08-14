package com.ffzxnet.developutil.utils.tools;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * ClassName:FileUtil
 * Description:
 * Created by wei.luo
 * Date 2015-10-12 15:44
 * CopyRight © 2015 51ZhiYe
 */
public class FileUtil {
    /**
     * 反斜杠 “/”
     */
    private static final String BACKSLASH = "/";

    /**
     * sd卡根目录
     */
    public static final String sdcard_root_directory = getSdcardRootDirectory();

    /**
     * 判断是否有sd卡
     *
     * @return
     */
    public static boolean isHasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取应用名称
     *
     * @param context
     * @return
     */
    public static String getCurrentAppPath(Context context) {
        if (!TextUtils.isEmpty(getSdcardRootDirectory())) {
            return getSdcardRootDirectory();
        } else {
            return getRootDirectory(context);
        }
    }

    /**
     * 给定一个路径和文件名，创建并返回一个完整的图片文件地址
     *
     * @param fileRoot
     * @param fileName nullable，为空则获取当前时间
     * @return
     */
    public static final String newImageFileDir(String fileRoot, String fileName) {
        fileName = TextUtils.isEmpty(fileName) ? getTime() : fileName;
        return fileRoot + BACKSLASH + fileName + ".png";
    }

    public static String getSdcardRootDirectory() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取系统根目录
     *
     * @param context
     * @return
     */
    public static String getRootDirectory(Context context) {
        return context.getApplicationInfo().dataDir;
    }

    /**
     * 删空文件夹内的文件
     *
     * @param folderName
     */
    public static void deleteFileInFolder(String folderName) {
        File file = new File(folderName);
        if (file.exists()) {
            if (file.isDirectory()) {
                if (file.listFiles().length != 0) {
                    for (File f : file.listFiles()) {
                        if (f.isDirectory()) {
                            deleteFileInFolder(f.getPath());
                        } else {
                            f.delete();
                        }
                    }
                }
            } else {
                file.delete();
            }
        }
    }

    public static List<String> getFolderContent(String folderDir) {
        File file = new File(folderDir);
        List<String> filesPath = new ArrayList<>();
        if (!file.exists()) {

        } else {
            for (File f : file.listFiles()) {
                if (f.isFile()) {
                    filesPath.add(f.getPath());
                }
            }
        }
        return filesPath;
    }

    public static List<String> getCatgFolderContent(String folderDir, String catg) {
        List<String> allFiles = new ArrayList<>();
        List<String> catgFiles = new ArrayList<>();
        allFiles = getFolderContent(folderDir);
        for (String str : allFiles) {
            if (!TextUtils.isEmpty(str) && str.contains(catg))
                catgFiles.add(str);
        }
        return catgFiles;
    }

    private static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    public static boolean checkFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 删除文件夹
     *
     * @param file
     */
    public static void deleteFolder(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFolder(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            LogUtil.e("删除文件", "文件不存在！");
        }
    }

    /**
     * 删除文件夹内指定的文件
     */
    public static boolean deleteFileName(File folder, String name) {
        if (folder.exists()) { // 判断文件是否存在
            if (folder.isFile() && name.equals(folder.getName())) {
                return folder.delete();
            } else if (folder.isDirectory()) { // 否则如果它是一个目录
                File files[] = folder.listFiles(); // 声明目录下所有的文件 files[];
                int filesSize = files.length;
                for (int i = 0; i < filesSize; i++) { // 遍历目录下所有的文件
                    deleteFileName(files[i], name); // 把每个文件 用这个方法进行迭代
                }
            }
            return true;
        } else {
            LogUtil.e("删除文件", "文件不存在！");
            return true;
        }
    }

    /**
     * 删除文件夹内非指定的文件
     */
    public static boolean deleteFileNoName(File folder, String name) {
        if (folder.exists()) { // 判断文件是否存在
            if (folder.isFile() && !name.equals(folder.getName())) {
                return folder.delete();
            } else if (folder.isDirectory()) { // 否则如果它是一个目录
                File files[] = folder.listFiles(); // 声明目录下所有的文件 files[];
                int filesSize = files.length;
                for (int i = 0; i < filesSize; i++) { // 遍历目录下所有的文件
                    deleteFileNoName(files[i], name); // 把每个文件 用这个方法进行迭代
                }
            }
            return true;
        } else {
            LogUtil.e("删除文件", "文件不存在！");
            return true;
        }
    }

    /**
     * 删除文件夹内非指定的文件
     */
    public static boolean deleteFileNoName(File folder, List<String> name) {
        if (folder.exists()) { // 判断文件是否存在
            if (folder.isFile() && !name.contains(folder.getName())) {
                return folder.delete();
            } else if (folder.isDirectory()) { // 否则如果它是一个目录
                File files[] = folder.listFiles(); // 声明目录下所有的文件 files[];
                int filesSize = files.length;
                for (int i = 0; i < filesSize; i++) { // 遍历目录下所有的文件
                    deleteFileNoName(files[i], name); // 把每个文件 用这个方法进行迭代
                }
            }
            return true;
        } else {
            LogUtil.e("删除文件", "文件不存在！");
            return true;
        }
    }
}
