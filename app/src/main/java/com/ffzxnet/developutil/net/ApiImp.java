package com.ffzxnet.developutil.net;


import com.ffzxnet.developutil.base.net.BaseApiResultData;
import com.ffzxnet.developutil.net.cookie.PersistentCookieStore;
import com.ffzxnet.developutil.net.retrofit_gson_factory.CustomGsonConverterFactory;
import com.ffzxnet.developutil.utils.tools.LogUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 创建者： feifan.pi 在 2017/2/20.
 */

public class ApiImp {
    //主地址  192.168.2.195:50510   kart.ffzxnet.com
        public static String APPBASEURL = "http://192.168.5.162:8080/";

//            public static String APPBASEURL = "http://192.168.11.105:50730/";
    //        http://192.168.11.105:50730/meet-villager-web 测试环境
    //请求地址
//            public static String APIURL = APPBASEURL + "meet-villager-web/";
    public static String APIURL = APPBASEURL + "upms-web/";


    //获取天气地址
    public static String WEATHERURL = "http://www.sojson.com/open/api/weather/json.shtml?city=";
    //使用协议
    public static String PROTOCOL = APIURL + "xiangyu/protocol.html";
    //商务洽谈
    public static String COOPERATION = APIURL + "xiangyu/cooperation.html";

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
        mBuilder.addInterceptor(loggingInterceptor);
        //打印日志 End

        final OkHttpClient okHttpClient = mBuilder.build();
        final Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(APIURL)
                .build();
        //服务器请求接口
        apiService = retrofit.create(ApiService.class);
    }

    /**
     * 默认请求配置
     *
     * @param observable 请求的接口 apiService.testObservable(request)
     * @param observer   结果监听 ApiSubscriber<TestResponse<TestPojo>> observer
     */
    private void baseObservableSetting(Observable observable, final IApiSubscriberCallBack observer) {
        observable.subscribeOn(Schedulers.io()) //在后台线程处理请求
                .observeOn(AndroidSchedulers.mainThread()) //请求结果到主线程
                .subscribe(new ApiSubscriber(observer));
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

    //登陆
    public void login(String request, IApiSubscriberCallBack<BaseApiResultData<String>> observer) {
        baseObservableSetting(apiService.login(request), observer);
    }


}
