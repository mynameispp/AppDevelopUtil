package com.ffzxnet.developutil.utils.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.ffzxnet.developutil.application.MyApplication;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;


public class FileUtil {
    /**
     * 反斜杠 “/”
     */
    private static final String BACKSLASH = "/";
    /**
     * App拍照图片路径
     */
    public static final String CameraPath = FileUtil.getSdcardRootDirectory("Camera");
    public static final String HttpCachePath = FileUtil.getSdcardRootDirectory("HttpCache");
    public static final String GlideCachePath = FileUtil.getSdcardRootDirectory("GlideCache");
    public static final String DownLoadFilePath = FileUtil.getSdcardRootDirectory("DownLoadFile");
    public static final String VideoPath = FileUtil.getSdcardRootDirectory("Video");
    public static final String RecorderPath = FileUtil.getSdcardRootDirectory("recorder");
    /**
     * 录音pcm格式
     */
    public static final String AudioRecorderPcmPath = FileUtil.getSdcardRootDirectory("pcm");
    /**
     * 录音wav格式
     */
    public static final String AudioRecorderWavPath = FileUtil.getSdcardRootDirectory("wav");

    public static String getSdcardRootDirectory(@NonNull String addFlod) {
        if (null == MyApplication.getContext()) {
            return "";
        }
        String rootPath = "";
        try {
            if (null != MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)) {
                //内部存储
                rootPath = Objects.requireNonNull(MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)).getAbsolutePath() + "/" + addFlod + "/";
            } else if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //外部存储
                rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + addFlod + "/";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(rootPath)) {
            File file = new File(rootPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return rootPath;
    }
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
    public static boolean deleteFileInFolder(String folderName) {
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
        return true;
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

    /**
     * 获取指定文件夹内相似名字的文件
     * @param folderDir
     * @param catg
     * @return
     */
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

    /**
     * 获取文件夹内的文件名称
     *
     * @param folderName
     */
    public static List<String> getFileNameInFolder(String folderName) {
        List<String> fileNames = new ArrayList<>();
        File file = new File(folderName);
        if (file.exists()) {
            if (file.isDirectory()) {
                if (null != file.listFiles() && file.listFiles().length != 0) {
                    for (File f : file.listFiles()) {
                        if (f.isDirectory()) {
                            getFileNameInFolder(f.getPath());
                        } else {
                            fileNames.add(f.getName());
                        }
                    }
                }
            } else {
                fileNames.add(file.getName());
            }
        }
        return fileNames;
    }

    /**
     * 删除指定文件
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 将文件转换为字节
     *
     * @param path 文件路径
     * @return
     * @throws IOException
     */
    public static byte[] fileToBytes(String path) throws IOException {
        InputStream inputStream = new FileInputStream(path);
        BufferedInputStream in = new BufferedInputStream(inputStream);
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] temp = new byte[1024];
        int size = 0;
        while ((size = in.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        in.close();
        byte[] content = out.toByteArray();
        return content;
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     */
    public static String getRealFilePathFromUri(final String url) {
        Uri uri = Uri.parse(url);
        return getRealFilePathFromUri(uri);
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     */
    public static String getRealFilePathFromUri(final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(scheme)) {
            Cursor cursor = MyApplication.getContext().getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
