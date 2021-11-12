package com.ffzxnet.developutil.ui.scancode.tools;

import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

public class UriUtils {
    private static final String TAG = UriUtils.class.getSimpleName();

    /**
     * 判断文件是否存在
     * @param context
     * @param fileUri
     * @return
     */
    public static boolean isFileExistByUri(Context context, Uri fileUri) {
        if(fileUri == null) {
            return false;
        }
        //target 小于Q
        if(!VersionUtils.isTargetQ(context)) {
            String filePath = getFilePath(context, fileUri);
            if(!TextUtils.isEmpty(filePath)) {
                return new File(filePath).exists();
            }
            return false;
        }
        //target 大于等于Q
        if(uriStartWithFile(fileUri)) {
            String path = fileUri.getPath();
            boolean exists = new File(path).exists();
            long length = new File(path).length();
            Log.d(TAG, "file uri exist = "+exists + " file length = "+ length);
            return exists;
        }else if(!uriStartWithContent(fileUri)) {
            return fileUri.toString().startsWith("/") && new File(fileUri.toString()).exists();
        }else {
            DocumentFile documentFile = DocumentFile.fromSingleUri(context, fileUri);
            return documentFile != null && documentFile.exists();
        }
    }

    /**
     * 判断uri是否以file开头
     * @param fileUri
     * @return
     */
    public static boolean uriStartWithFile(Uri fileUri) {
        return "file".equalsIgnoreCase(fileUri.getScheme()) && fileUri.toString().length() > 7;
    }

    /**
     * 判断uri是否以content开头
     * @param fileUri
     * @return
     */
    public static boolean uriStartWithContent(Uri fileUri) {
        return "content".equalsIgnoreCase(fileUri.getScheme());
    }

    /**
     * 获取文件名
     * @param context
     * @param fileUri
     * @return
     */
    public static String getFileNameByUri(Context context, Uri fileUri) {
        if(fileUri == null) {
            return "";
        }
        //target 小于Q
        if(!VersionUtils.isTargetQ(context)) {
            String filePath = getFilePath(context, fileUri);
            if(!TextUtils.isEmpty(filePath) && new File(filePath).exists()) {
                return new File(filePath).getName();
            }
            return "";
        }
        //target 大于Q
        if(uriStartWithFile(fileUri)) {
            File file = new File(fileUri.getPath());
            return file.exists() ? file.getName() : "";
        }
        if(!uriStartWithContent(fileUri)) {
            if(fileUri.toString().startsWith("/") && new File(fileUri.toString()).exists()) {
                return new File(fileUri.toString()).getName();
            }
            return "";
        }

        return getFilenameByDocument(context, fileUri);
    }

    /**
     * get filename from DocumentFile
     * @param context
     * @param uri
     * @return
     */
    public static String getFilenameByDocument(Context context, Uri uri) {
        DocumentFile documentFile = getDocumentFile(context, uri);
        if(documentFile == null) {
            return "";
        }
        return documentFile.getName();
    }

    public static long getFileLength(Context context, Uri fileUri) {
        if(fileUri == null) {
            return 0;
        }
        //target 小于Q
        if(!VersionUtils.isTargetQ(context)) {
            String filePath = getFilePath(context, fileUri);
            if(!TextUtils.isEmpty(filePath) && new File(filePath).exists()) {
                return new File(filePath).length();
            }
            return 0;
        }
        //target 大于Q
        if(uriStartWithFile(fileUri) && new File(fileUri.getPath()).exists()) {
            return new File(fileUri.getPath()).length();
        }
        if(!uriStartWithContent(fileUri) && fileUri.toString().startsWith("/") && new File(fileUri.toString()).exists()) {
            return new File(fileUri.toString()).length();
        }
        DocumentFile documentFile = getDocumentFile(context, fileUri);
        if(documentFile == null) {
            return 0;
        }
        return documentFile.length();
    }

    public static String getFileMimeType(Context context, Uri fileUri) {
        if(fileUri == null) {
            return null;
        }
        //target 小于Q
        if(!VersionUtils.isTargetQ(context)) {
            String filePath = getFilePath(context, fileUri);
            if(!TextUtils.isEmpty(filePath)) {
                return FileUtils.getMIMEType(new File(filePath));
            }
            return null;
        }
        //target 大于Q
        if(uriStartWithFile(fileUri)) {
            return FileUtils.getMIMEType(new File(fileUri.getPath()));
        }
        if(!uriStartWithContent(fileUri)) {
            if(fileUri.toString().startsWith("/") && new File(fileUri.toString()).exists()) {
                return FileUtils.getMIMEType(new File(fileUri.toString()));
            }else {
                return null;
            }
        }
        DocumentFile documentFile = getDocumentFile(context, fileUri);
        if(documentFile == null) {
            return null;
        }
        return documentFile.getType();
    }

    public static int getVideoOrAudioDuration(Context context, Uri mediaUri) {
        String[] projection = {MediaStore.Video.Media.DURATION};
        Cursor cursor = context.getContentResolver().query(mediaUri, projection, null,
                null, null);
        long duration = 0;
        if (cursor != null && cursor.moveToFirst()) {
            duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
            cursor.close();
            cursor=null;
        }
        if(duration <= 0) {
            MediaMetadataRetriever retriever = null;
            String durationString = "";
            try {
                retriever = new MediaMetadataRetriever();
                retriever.setDataSource(context, mediaUri);
                durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                duration = Integer.valueOf(durationString);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } finally {
                if(retriever != null) {
                    retriever.release();
                }
            }
        }
        if(duration <= 0) {
            duration = 0;
        }
        Log.d(TAG, "duration:"+duration);
        return (int)duration;
    }

    private static DocumentFile getDocumentFile(Context context, Uri uri) {
        if(uri == null) {
            Log.e(TAG, "uri is null");
            return null;
        }
        DocumentFile documentFile = DocumentFile.fromSingleUri(context, uri);
        if(documentFile == null) {
            Log.e(TAG, "DocumentFile is null");
            return null;
        }
        return documentFile;
    }

    public static String getUriString(Uri uri) {
        if(uri == null) {
            return null;
        }
        return uri.toString();
    }

    public static Uri getLocalUriFromString(String url){
        if(TextUtils.isEmpty(url)) {
            return null;
        }
        if(url.startsWith("content")) {
            return Uri.parse(url);
        }else if(url.startsWith("file") && url.length() > 7) {
            return Uri.fromFile(new File(Uri.parse(url).getPath()));
        }else if(url.startsWith("/")) {
            return Uri.fromFile(new File(url));
        }
        return null;
    }

    public static String getMimeType(Context context, Uri fileUri) {
        String filePath = getFilePath(context, fileUri);
        if(!TextUtils.isEmpty(filePath) && new File(filePath).exists()) {
            return getMimeType(new File(filePath));
        }
        return context.getContentResolver().getType(fileUri);
    }

    public static String getMimeType(File sourceFile){
        return FileUtils.getMIMEType(sourceFile);
    }

    public static String getMimeType(String fileName) {
        if (fileName.endsWith(".3gp") || fileName.endsWith(".amr")) {
            return "audio/3gp";
        }if(fileName.endsWith(".jpe")||fileName.endsWith(".jpeg")||fileName.endsWith(".jpg")){
            return "image/jpeg";
        }if(fileName.endsWith(".amr")){
            return "audio/amr";
        }if(fileName.endsWith(".mp4")){
            return "video/mp4";
        }if(fileName.endsWith(".mp3")){
            return "audio/mpeg";
        }else {
            return "application/octet-stream";
        }
    }

    /**
     * get file path, maybe the param of path is the Uri's string type.
     * @param context
     * @param path
     * @return
     */
    public static String getFilePath(Context context, String path) {
        if(TextUtils.isEmpty(path)) {
            return path;
        }
        Uri uri = Uri.parse(path);
        return UriUtils.getFilePath(context, uri);
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getFilePath(final Context context, final Uri uri) {
        if(uri == null) {
            return "";
        }
        //sdk版本在29之前的
        if(!VersionUtils.isTargetQ(context)) {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if (uriStartWithContent(uri)) {
                return getDataColumn(context, uri, null, null);
            }
        }
        // FileProvider
        if(isFileProvider(context, uri)) {
            return getFPUriToPath(context, uri);
        }
        // other FileProvider
        else if(isOtherFileProvider(context, uri)) {
            return copyFileProviderUri(context, uri);
        }
        // start with "file"
        else if (uriStartWithFile(uri)) {
            return uri.getPath();
        }
        // start with "/"
        else if(uri.toString().startsWith("/")) {//如果是路径的话，返回路径
            return uri.toString();
        }
        // sdk29之后的资源路径（content开头的资源路径）及其他情况
        return "";
    }

    /**
     * 从FileProvider获取文件
     * @param context
     * @param uri
     * @return
     */
    private static String copyFileProviderUri(Context context, Uri uri) {
        //如果是分享过来的文件，则将其写入到私有目录下
        String[] subs = uri.toString().split("/");
        String filename = null;
        if(subs.length > 0) {
            filename = subs[subs.length -1];
        }else {
            return "";
        }
        String filePath = PathUtil.getInstance().getFilePath() + File.separator + filename;
        if(new File(filePath).exists()) {
            return filePath;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = context.getContentResolver().openInputStream(uri);
            out = new FileOutputStream(filePath);
            byte[] tmp = new byte[2048];
            int l;
            while ((l = in.read(tmp)) != -1) {
                out.write(tmp, 0, l);
            }
            Log.d(TAG, "other fileProvider file path = "+filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
                if(out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new File(filePath).exists() ? filePath : "";
    }

    /**
     * 从FileProvider获取文件路径
     * @param context
     * @param uri
     * @return
     */
    private static String getFPUriToPath(Context context, Uri uri) {
        try {
            List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
            if (packs != null) {
                String fileProviderClassName = FileProvider.class.getName();
                for (PackageInfo pack : packs) {
                    ProviderInfo[] providers = pack.providers;
                    if (providers != null) {
                        for (ProviderInfo provider : providers) {
                            if (uri.getAuthority().equals(provider.authority)) {
                                if (provider.name.equalsIgnoreCase(fileProviderClassName)) {
                                    Class<FileProvider> fileProviderClass = FileProvider.class;
                                    try {
                                        Method getPathStrategy = fileProviderClass.getDeclaredMethod("getPathStrategy", Context.class, String.class);
                                        getPathStrategy.setAccessible(true);
                                        Object invoke = getPathStrategy.invoke(null, context, uri.getAuthority());
                                        if (invoke != null) {
                                            String PathStrategyStringClass = FileProvider.class.getName() + "$PathStrategy";
                                            Class<?> PathStrategy = Class.forName(PathStrategyStringClass);
                                            Method getFileForUri = PathStrategy.getDeclaredMethod("getFileForUri", Uri.class);
                                            getFileForUri.setAccessible(true);
                                            Object invoke1 = getFileForUri.invoke(invoke, uri);
                                            if (invoke1 instanceof File) {
                                                String filePath = ((File) invoke1).getAbsolutePath();
                                                return filePath;
                                            }
                                        }
                                    } catch (NoSuchMethodException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * the uri is from current app's FileProvider
     * @param context
     * @param uri
     * @return
     */
    public static boolean isFileProvider(Context context, Uri uri) {
        return (context.getApplicationInfo().packageName + ".fileProvider").equalsIgnoreCase(uri.getAuthority());
    }

    /**
     * if the uri is from other app
     * @param context
     * @param uri
     * @return
     */
    public static boolean isOtherFileProvider(Context context, Uri uri) {
        String scheme = uri.getScheme();
        String authority = uri.getAuthority();
        if(TextUtils.isEmpty(scheme) || TextUtils.isEmpty(authority)) {
            return false;
        }
        return !(context.getApplicationInfo().packageName + ".fileProvider").equalsIgnoreCase(uri.getAuthority())
                && "content".equalsIgnoreCase(uri.getScheme())
                && authority.contains(".fileProvider".toLowerCase());
    }
}
