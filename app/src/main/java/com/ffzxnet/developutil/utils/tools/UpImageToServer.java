package com.ffzxnet.developutil.utils.tools;

import com.ffzxnet.countrymeet.bean.UpImgToServiceResponse;
import com.ffzxnet.countrymeet.net.ApiImp;
import com.ffzxnet.countrymeet.utils.ui.ToastUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 上传本地图片给服务器 并返回结果
 *
 * @author pp
 */
public class UpImageToServer {
    private static final String TAG = "头像上传";
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    public static final String SUCCESS = "1成功！";
    public static final String FAILURE = "0";

    public static void uploadFile(File file, UpLoadImageListener2 upLoadImageListener) {
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        // 上传路径
        // String RequestURL =
        // "http://120.25.236.193/test/middlemenPhotoList/photo/mobileUpload?direct=asd";
        String RequestURL = ApiImp.APIURL + "appUploadImage/upload.do";
        //服务端返回的指令
        int serverFailedOrder = 0;
        try {
            if (file != null) {
                URL url = new URL(RequestURL);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setReadTimeout(TIME_OUT);
                conn.setConnectTimeout(TIME_OUT);
                conn.setDoInput(true); // 允许输入流
                conn.setDoOutput(true); // 允许输出流
                conn.setUseCaches(false); // 不允许使用缓存
                conn.setRequestMethod("POST"); // 请求方式
                conn.setRequestProperty("Charset", CHARSET); // 设置编码
                conn.setRequestProperty("connection", "keep-alive");
                conn.setRequestProperty("Content-Type", CONTENT_TYPE
                        + ";boundary=" + BOUNDARY);
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                OutputStream outputSteam = conn.getOutputStream();

                DataOutputStream dos = new DataOutputStream(outputSteam);
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */

                sb.append("Content-Disposition: form-data; name=\"image\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                // sb.append("Content-Disposition: form-data; name=\"direct\""+file.getName()+"\""+LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                long sum = 0;
                while ((len = is.read(bytes)) != -1) {
                    //记录长度
                    sum += len;
                    dos.write(bytes, 0, len);
                    upLoadImageListener.getProgress(getRate(sum, file.length()));
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();
                dos.close();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                if (res == 200) {
                    String result = "";
                    InputStream input = conn.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(
                            input);
                    BufferedReader reader = new BufferedReader(
                            inputStreamReader);// 读字符串用的。
                    String inputLine = null;
                    // 使用循环来读取获得的数据，把数据都村到result中了
                    while (((inputLine = reader.readLine()) != null)) {
                        // 我们在每一行后面加上一个"\n"来换行
                        result += inputLine + "\n";
                    }
                    reader.close();// 关闭输入流
                    conn.disconnect();
                    upLoadImageListener.isSuccess(true, result);
                } else {
                    serverFailedOrder = res;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            upLoadImageListener.isSuccess(false, "");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
            upLoadImageListener.isSuccess(false, "");
        } catch (Exception e) {
            e.printStackTrace();
            upLoadImageListener.isSuccess(false, "");
        }
        upLoadImageListener.isSuccess(false, "");
    }

    /**
     * 根据已上传的长度和上传的总长度进行获取进度条
     *
     * @param sum
     * @param length
     * @return
     */
    private static String getRate(long sum, long length) {
        double rate = sum * 1.0 / length;
        DecimalFormat df = new DecimalFormat("#.00");
        try {
            rate = Double.parseDouble(df.format(rate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rate * 100 + "";
    }

    /**
     * 上传图片回调(线程回调)
     */
    public interface UpLoadImageListener2 {
        /**
         * 获取进度
         *
         * @param rate
         */
        void getProgress(String rate);

        void isSuccess(boolean b, String url);

    }

    /**
     * 上传图片回调（ui回调）
     */
    public interface UpLoadIamgeListener {
        /* * 上传完成回调
         *
         * @param path
         */
        void success(String path);
    }

    public static void upImgToService(final String imgPathUrl, final UpLoadIamgeListener listener) {
        Observable.create(new ObservableOnSubscribe<UpImgToServiceResponse>() {
            @Override
            public void subscribe(final ObservableEmitter<UpImgToServiceResponse> emitter) throws Exception {
                UpImageToServer.uploadFile(new File(imgPathUrl), new UpImageToServer.UpLoadImageListener2() {
                    @Override
                    public void getProgress(String rate) {
                    }

                    @Override
                    public void isSuccess(boolean b, String url) {
                        if (b) {
                            try {
                                JSONObject jsonObject = new JSONObject(url);
                                String infoData = jsonObject.getString("data");
                                UpImgToServiceResponse response = (UpImgToServiceResponse) GsonUtil.toClass(infoData, UpImgToServiceResponse.class);
                                if (null == response) {
                                    emitter.onNext(new UpImgToServiceResponse());
                                } else {
                                    emitter.onNext(response);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                emitter.onNext(new UpImgToServiceResponse());
                            }
                        } else {
                            emitter.onNext(new UpImgToServiceResponse());
                        }
                        emitter.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io())// 此方法为上面发出事件设置线程为IO线程
                .observeOn(AndroidSchedulers.mainThread())// 为消耗事件设置线程为UI线程
                .subscribe(new Consumer<UpImgToServiceResponse>() {
                    @Override
                    public void accept(UpImgToServiceResponse response) throws Exception {
                        if (null == response) {
                            ToastUtil.showToastShort("上传失败");
                        } else {
                            listener.success(response.getImgPath());
                        }
                    }
                });
    }

}
