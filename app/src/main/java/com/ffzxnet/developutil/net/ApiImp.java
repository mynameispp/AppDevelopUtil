package com.ffzxnet.developutil.net;


import com.ffzxnet.developutil.BuildConfig;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.mvp.BaseNetView;
import com.ffzxnet.developutil.base.net.BaseApiResultData;
import com.ffzxnet.developutil.bean.LoginRequestBean;
import com.ffzxnet.developutil.bean.UpLoadRequest;
import com.ffzxnet.developutil.net.cookie.PersistentCookieStore;
import com.ffzxnet.developutil.net.retrofit_gson_factory.CustomGsonConverterFactory;
import com.ffzxnet.developutil.utils.tools.FileUtil;
import com.ffzxnet.developutil.utils.tools.LogUtil;
import com.ffzxnet.developutil.utils.tools.NetworkUtils;
import com.trello.rxlifecycle4.LifecycleTransformer;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

/**
 * 创建者： feifan.pi 在 2017/2/20.
 */

public class ApiImp {
    // 是否为线上环境
    public static boolean IS_PRODUCTION_ENVIRONMENT = BuildConfig.BuildTypes;
    //请求服务地址
    public static String APPBASEURL = BuildConfig.BASE_API;//接口地址
    //单例实体对象
    private static volatile ApiImp apiImp = null;
    /**
     * 接口的服务类
     */
    private final ApiService apiService;

    //单例模式
    public static ApiImp getInstance() {
        ApiImp inst = apiImp;  // <<< 在这里创建临时变量
        if (inst == null) {
            synchronized (ApiImp.class) {
                inst = apiImp;
                if (inst == null) {
                    inst = new ApiImp();
                    apiImp = inst;
                }
            }
        }
        return inst;
    }

    /**
     * 初始化Retrofit配置
     */
    public ApiImp() {
        final OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        //添加管理cookie的实现
        mBuilder.cookieJar(new CookiesManager());
        //打印日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                LogUtil.e("RetrofitLog", "RetrofitLog = " + message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //缓存大小
        File httpCacheDirectory = new File(FileUtil.HttpCache);//这里为了方便直接把文件放在了SD卡根目录的HttpCache中，一般放在context.getCacheDir()中
        int cacheSize = 20 * 1024 * 1024;//设置缓存文件大小为20M
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        final OkHttpClient okHttpClient = mBuilder
                .readTimeout(10, TimeUnit.SECONDS)//设置超时
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时
                .writeTimeout(60, TimeUnit.SECONDS)//设置超时
                .addNetworkInterceptor(getNetWorkInterceptor())//设置有网时的请求处理
                .addInterceptor(loggingInterceptor)//设置请求打印日志
                .addInterceptor(getRequestHeadInterceptor()) //设置统一请求头
                .addInterceptor(getOffLineInterceptor())//设置无网时的请求处理
                .cache(cache)//设置缓存大小
                .build();
        final Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(APPBASEURL)
                .build();
        //服务器请求接口
        apiService = retrofit.create(ApiService.class);
    }

    /**
     * 设置请求头处理
     *
     * @return
     */
    private Interceptor getRequestHeadInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("DEVICE_ID", "DEVICE_ID")
                        .header("Token", "Token");
                Request request = requestBuilder.build();
                Response response = chain.proceed(request);
                //重定向
//                if ((response.code() == 404 ||response.code() == 500 )
//                        && !response.request().url().toString().contains("getdomain")){
////                    String url = "http://39.102.44.215:8088/getdomain";
//
//                    String url = "http://39.102.44.215:8089/getdomain";
//                    OkHttpClient okHttpClient = new OkHttpClient();
//                    final Request request2 = new Request.Builder()
//                            .url(url)
//                            .build();
//                    final Call call = okHttpClient.newCall(request2);
//                    Response response2 = call.execute();
//                    if (response2.body() != null) {
//                        String nUrl = response2.body().string();
//                        LogUtil.e("uuuuuuuuuuuu", "run: " + nUrl);
//                        if (!TextUtils.isEmpty(nUrl)) {
//                            BackServiceUrlResponse da = (BackServiceUrlResponse) GsonUtil.toClass(nUrl, BackServiceUrlResponse.class);
//                            if (da != null && da.getData() != null && !TextUtils.isEmpty(da.getData().getDomain())) {
//                                String[] aa = da.getData().getDomain().split(",");
//                                if (aa.length >= 1) {
//                                    APPBASEURL = "http://" + aa[0];
//                                    LogUtil.e("uuuuuuuuuuuu", "run: " + APPBASEURL);
//                                }
//                            }
//                        }
//                    }
////                    Request.Builder requestBuilder2 = original.newBuilder()
////                            .header("JPAUTH", AuthCode.authcodeEncode("jgnb", "UhnRS5ebz4a8rfhIllEk").trim())
////                            .header("user-agent", "jianpian-android/" + PackageUtils.getVersionCode());
////                    Request requestn = requestBuilder2.build();
////                    requestn.url().
////                    return chain.proceed(requestn);
//                }
                return response;

            }
        };
    }

    /**
     * 自动管理Cookies
     */
    private class CookiesManager implements CookieJar {
        private final PersistentCookieStore cookieStore = PersistentCookieStore.getInstance();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                for (Cookie item : cookies) {
                    cookieStore.add(url, item);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies;
        }
    }

    /**
     * 设置返回数据的  Interceptor  判断网络   没网读取缓存
     */
    private Interceptor getOffLineInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetworkUtils.hasNetwork(MyApplication.getContext())) {
                    int offlineCacheTime = 60 * 60 * 24 * 7;//离线的时候的缓存的过期时间
                    request = request.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + offlineCacheTime)
                            .build();
                }
                return chain.proceed(request);
            }
        };
    }

    /**
     * 设置连接器  设置缓存
     */
    private Interceptor getNetWorkInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                int onlineCacheTime = 0;//在线的时候的缓存过期时间，如果想要不缓存，直接时间设置为0
                return response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + onlineCacheTime)
                        .removeHeader("Pragma")
                        .build();
            }
        };
    }


    /**
     * 信任所有证书
     *
     * @param mBuilder
     */
    private void noSSLKey(OkHttpClient.Builder mBuilder) {
        try {
            TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }

            }};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new SecureRandom());
            EmptyHostNameVerifier emptyHostNameVerifier = new EmptyHostNameVerifier();

            mBuilder.sslSocketFactory(sslContext.getSocketFactory());
            mBuilder.hostnameVerifier(emptyHostNameVerifier);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class EmptyHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String s, SSLSession sslSession) {

            return true;
        }
    }
    //=================================接口实现统一方法===========================================================================

    /**
     * 默认请求配置
     *
     * @param observable           请求的接口 apiService.testObservable(request)
     * @param lifecycleTransformer 请求绑定界面的生命周期
     * @param callBack             结果监听 ApiSubscriber<TestResponse<Vo>> observer
     */
    private void baseObservableSetting(Observable observable, BaseNetView baseNetView, LifecycleTransformer lifecycleTransformer, final IApiSubscriberCallBack callBack) {
        observable.subscribeOn(Schedulers.io()) //在后台线程处理请求
                .observeOn(AndroidSchedulers.mainThread()) //请求结果到主线程
                .compose(lifecycleTransformer)
                .subscribe(new ApiSubscriber(baseNetView, callBack));
    }

    private void baseObservableSetting(Observable observable, BaseNetView baseNetView, final IApiSubscriberCallBack callBack) {
        observable.subscribeOn(Schedulers.io()) //在后台线程处理请求
                .observeOn(AndroidSchedulers.mainThread()) //请求结果到主线程
                .subscribe(new ApiSubscriber(baseNetView, callBack));
    }

    private void baseObservableSetting(Observable observable, final IApiSubscriberCallBack callBack) {
        observable.subscribeOn(Schedulers.io()) //在后台线程处理请求
                .observeOn(AndroidSchedulers.mainThread()) //请求结果到主线程
                .subscribe(new ApiSubscriber(callBack));
    }

    //====================================接口实现=====================================================================
    //登陆
    public void login(LoginRequestBean request, BaseNetView baseNetView, LifecycleTransformer lifecycleTransformer, IApiSubscriberCallBack<BaseApiResultData<String>> callBack) {
        baseObservableSetting(apiService.login(request), baseNetView, lifecycleTransformer, callBack);
    }

    //下载文件
    public void downloadFile(@NonNull final String url, IApiSubscriberCallBack<ResponseBody> callBack) {
        baseObservableSetting(apiService.downloadFile(url), callBack);
    }

    //上傳头像
    public void upLoadFile(UpLoadRequest request, IApiSubscriberCallBack<BaseApiResultData<String>> callBack) {
        baseObservableSetting(apiService.upLoadFile(APPBASEURL + "/org/qiniu/upload", request.getFileUrl()), callBack);
    }

}
