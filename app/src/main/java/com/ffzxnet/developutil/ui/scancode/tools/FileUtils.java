package com.ffzxnet.developutil.ui.scancode.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.core.content.FileProvider;

public class FileUtils {
    public static String[] fileTypes = new String[] { "apk", "avi", "bmp", "chm", "dll", "doc", "docx", "dos", "gif",
            "html", "jpeg", "jpg", "movie", "mp3","dat", "mp4", "mpe", "mpeg", "mpg", "pdf", "png", "ppt", "pptx", "rar",
            "txt", "wav", "wma", "wmv", "xls", "xlsx", "xml", "zip" };

    public static File[] loadFiles(File directory){
        File[] listFiles = directory.listFiles();
        if(listFiles == null)
            listFiles = new File[]{};

        ArrayList<File> tempFolder = new ArrayList<File>();
        ArrayList<File> tempFile = new ArrayList<File>();
        for (File file : listFiles) {
            if (file.isDirectory()) {
                tempFolder.add(file);
            } else if (file.isFile()) {
                tempFile.add(file);
            }
        }
        // sort list
        Comparator<File> comparator = new MyComparator();
        Collections.sort(tempFolder, comparator);
        Collections.sort(tempFile, comparator);

        File[] datas = new File[tempFolder.size() + tempFile.size()];
        System.arraycopy(tempFolder.toArray(new File[tempFolder.size()]), 0, datas, 0, tempFolder.size());
        System.arraycopy(tempFile.toArray(new File[tempFile.size()]), 0, datas, tempFolder.size(), tempFile.size());

        return datas;
    }

    /**
     * Determine the type of file
     * @param f
     * @return
     */
    public static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();

        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(end);
        return type;
    }

    public static String getMIMEType(String fileName) {
        String type = "";

        String end = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(end);
        return type;
    }


    private static final String TAG = "FileUtils";

    /**
     * Please use easeui module/EaseCompat#openFile(File f, Activity context) for instead.
     *
     * @param f
     * @param context
     */
    @Deprecated
    public static void openFile(File f, Activity context) {
        /* get MimeType */
        String type = FileUtils.getMIMEType(f);
        /* get uri */
        Uri uri = getUriForFile(context, f);
        openFile(uri, type, context);
    }

    /**
     * Please use easeui module/EaseCompat#openFile(File f, String type, Activity context) for instead.
     *
     * @param uri
     * @param type
     * @param context
     */
    @Deprecated
    public static void openFile(Uri uri,String type,Activity context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setAction(Intent.ACTION_VIEW);
        /* set intent's file and MimeType */
        intent.setDataAndType(uri, type);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Can't find proper app to open this file", Toast.LENGTH_LONG).show();
        }
    }

    // custom comparator
    public static class MyComparator implements Comparator<File> {
        @Override
        public int compare(File lhs, File rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }

    }

    public static Uri getUriForFile(Context context, File file) {
        // Build.VERSION_CODES.N = 24, 直接用会导致ant打包不成功
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }
    }
}
