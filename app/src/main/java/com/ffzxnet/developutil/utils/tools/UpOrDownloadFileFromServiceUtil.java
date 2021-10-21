package com.ffzxnet.developutil.utils.tools;

import android.text.TextUtils;

import com.ffzxnet.developutil.base.net.BaseApiResultData;
import com.ffzxnet.developutil.bean.UpLoadRequest;
import com.ffzxnet.developutil.net.ApiImp;
import com.ffzxnet.developutil.net.ErrorResponse;
import com.ffzxnet.developutil.net.IApiSubscriberCallBack;
import com.ffzxnet.developutil.utils.ui.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

//上传或下载文件到服务器
public class UpOrDownloadFileFromServiceUtil {
    private static final String TAG = "DownLoadManager";

    private static String APK_CONTENTTYPE = "application/vnd.android.package-archive";

    private static String PNG_CONTENTTYPE = "image/png";

    private static String JPG_CONTENTTYPE = "image/jpg";

    private static String JPEG_CONTENTTYPE = "image/jpeg";

    private static String ZIP_CONTENTTYPE = "application/zip";

    private static String Mp3_CONTENTTYPE = "audio/mp3";

    private static String fileSuffix = "";


    //上传文件
    public static void upLoadToService(final String filePath) {
        File file = new File(filePath);//filePath 图片地址
//        String token = "ASDDSKKK19990SDDDSS";//用户token
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
//                .addFormDataPart(ParamKey.KEY_TOKEN, token);//ParamKey.KEY_TOKEN 自定义参数key常量类，即参数名
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("fileName", file.getName(), imageBody);//fileName 后台接收图片流的参数名
        List<MultipartBody.Part> parts = builder.build().parts();
        ApiImp.getInstance().upLoadFile(new UpLoadRequest(parts.get(0)), new IApiSubscriberCallBack<BaseApiResultData<String>>() {
            @Override
            public void onCompleted() {
//                mView.showLoadingDialog(false);
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastLong(error.getMessage());
            }

            @Override
            public void onNext(BaseApiResultData<String> stringBaseApiResultData) {
                //服务器返回结果
            }
        });
    }

    //下载文件
    private  int downSuccessVoiceSize=0;
    public void downloadTopicVoices(final List<String> voiceList) {
//        mView.showLoadingDialog(true, "下载音频中...");
        final String filePath = FileUtil.DownLoadFile;
        for (final String s : voiceList) {
            String fileName = s.substring(s.lastIndexOf("/") + 1);
            File file = new File(filePath + fileName);
            if (file.exists()) {
                //下载过的不下载
                downSuccessVoiceSize++;
                if (downSuccessVoiceSize == voiceList.size()) {
                    //全部下载完
//                    mView.showLoadingDialog(false);
//                    mView.downloadTopicVoic(true, "", data, voiceList);
                }
                continue;
            }
            ApiImp.getInstance().downloadFile(s, new IApiSubscriberCallBack<ResponseBody>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(ErrorResponse error) {
//                    mView.showLoadingDialog(false);
//                    mView.downloadTopicVoic(false, "下载音频失败，请稍后重试", data, voiceList);
                }

                @Override
                public void onNext(ResponseBody responseBody) {
                    String fileName = s.substring(s.lastIndexOf("/") + 1);
                    if (writeResponseBodyToDisk(responseBody, filePath, fileName)) {
                        //下载成功
                        downSuccessVoiceSize++;
                        if (downSuccessVoiceSize == voiceList.size()) {
                            //全部下载完
//                            mView.showLoadingDialog(false);
//                            mView.downloadTopicVoic(true, "", data, voiceList);
                        }
                    } else {
//                        mView.showLoadingDialog(false);
//                        mView.downloadTopicVoic(false, "下载失败，请稍后重试", data, voiceList);
                    }
                }
            });
        }
    }

    //将流写入本地
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
