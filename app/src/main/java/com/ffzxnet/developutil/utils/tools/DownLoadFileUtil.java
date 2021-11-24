package com.ffzxnet.developutil.utils.tools;

import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * @author awa.pi
 * @date 创建时间:2018/5/29
 * @description 下载管理
 */

public class DownLoadFileUtil {
    private static final String TAG = "DownLoadManager";

    private static String APK_CONTENTTYPE = "application/vnd.android.package-archive";

    private static String PNG_CONTENTTYPE = "image/png";

    private static String JPG_CONTENTTYPE = "image/jpg";

    private static String JPEG_CONTENTTYPE = "image/jpeg";

    private static String ZIP_CONTENTTYPE = "application/zip";

    private static String Mp3_CONTENTTYPE = "audio/mp3";

    private static String fileSuffix = "";

    /**
     * test Code
     * ApiImp.getInstance().downloadFile("http://a.hiphotos.baidu.com/image/pic/item/d009b3de9c82d1584cab701b8c0a19d8bc3e426a.jpg"
     * , null, new IApiSubscriberCallBack<ResponseBody>() {
     *
     * @param body
     * @param filePath 文件完整路径和文件名称，不需要文件的后缀
     * @return
     * @Override public void onCompleted() {
     * <p>
     * }
     * @Override public void onError(ErrorResponse error) {
     * <p>
     * }
     * @Override public void onNext(ResponseBody responseBody) {
     * String filePath = FileUtil.getSdcardRootDirectory();
     * String fileName =  SystemClock.currentThreadTimeMillis()+"";
     * if (DownLoadManager.writeResponseBodyToDisk(responseBody, filePath, fileName)) {
     * ToastUtil.showToastShort("下载成功");
     * } else {
     * ToastUtil.showToastShort("下载失败，请重新下载");
     * }
     * }
     * });
     * @description 下载文件
     */
    public static boolean writeResponseBodyToDisk(ResponseBody body, String filePath, String fileName) {

        LogUtil.e(TAG, "contentType:>>>>" + body.contentType().toString());

        String type = body.contentType().toString();

        if (type.equals(APK_CONTENTTYPE)) {
            fileSuffix = ".apk";
        } else if (type.equals(PNG_CONTENTTYPE)) {
            fileSuffix = ".png";
        } else if (type.equals(JPG_CONTENTTYPE) || type.equals(JPEG_CONTENTTYPE)) {
            fileSuffix = ".jpg";
        } else if (type.endsWith(ZIP_CONTENTTYPE)) {
            fileSuffix = ".zip";
        } else if (type.endsWith(Mp3_CONTENTTYPE)) {
            fileSuffix = ".mp3";
        } else {
            fileSuffix = ".mp3";
        }

        // 其他类型同上 自己判断加入.....

        if (TextUtils.isEmpty(fileSuffix)) {
            //没适配到对应的资源
            return false;
        }

        try {
            // todo change the file location/name according to your needs
            LogUtil.e(TAG, "path:>>>>" + filePath);
            File path = new File(filePath);
            if (!path.exists()) {
                path.mkdirs();
            }
            String fileAbsolutePath = filePath + fileName;
            File file = new File(fileAbsolutePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            File futureStudioIconFile = new File(fileAbsolutePath);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    LogUtil.e(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();


                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
