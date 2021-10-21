package com.ffzxnet.developutil.net;

import android.net.ParseException;
import android.text.TextUtils;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.mvp.BaseNetView;
import com.ffzxnet.developutil.base.net.BaseApiResultData;
import com.ffzxnet.developutil.net.retrofit_gson_factory.ResultErrorException;
import com.ffzxnet.developutil.utils.tools.GsonUtil;
import com.ffzxnet.developutil.utils.tools.LogUtil;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 创建者： feifan.pi 在 2017/3/14.
 */

public class ApiSubscriber<T> implements Observer<T> {
    private IApiSubscriberCallBack<T> iApiSubscriberCallBack;
    private BaseNetView baseNetView;
    //取消订阅
    private Disposable disposable;

    public ApiSubscriber(IApiSubscriberCallBack<T> iApiSubscriberCallBack) {
        this.iApiSubscriberCallBack = iApiSubscriberCallBack;
    }

    public ApiSubscriber(BaseNetView baseNetView, IApiSubscriberCallBack<T> iApiSubscriberCallBack) {
        this.baseNetView = baseNetView;
        this.iApiSubscriberCallBack = iApiSubscriberCallBack;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
    }

    @Override
    public void onComplete() {
        iApiSubscriberCallBack.onCompleted();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    @Override
    public void onError(final Throwable e) {
        LogUtil.e("Subscriber onError", e.toString());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(-3);
        errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_link_exception));
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_access_unauthorized));
                    if (null != baseNetView) {
                        //返回给界面处理
                        baseNetView.catchApiSubscriberError(errorResponse);
                        return;
                    }
                    break;
                case HttpURLConnection.HTTP_FORBIDDEN:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_access_denied));
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_not_find));
                    break;
                case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_time_out));
                    break;
                case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_time_out));
                    break;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_service_error));
                    break;
                case HttpURLConnection.HTTP_BAD_GATEWAY:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_service_error));
                    break;
                case HttpURLConnection.HTTP_UNAVAILABLE:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_service_error));
                    break;
                default:
                    errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_link_exception));
                    break;
            }
        } else if (e instanceof JsonParseException || e instanceof JSONException
                || e instanceof ParseException) {
            errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_json));
        } else if (e instanceof ConnectException) {
            errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_access_denied));
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_access_denied));
        } else if (e instanceof ResultErrorException) {
            //系统返回的异常
            String errorMes = e.getMessage();
            if (!TextUtils.isEmpty(errorMes)) {
                errorResponse = (ErrorResponse) GsonUtil.toClass(errorMes, ErrorResponse.class);
            }
        } else if (e instanceof SocketTimeoutException) {
            errorResponse.setMessage(MyApplication.getStringByResId(R.string.net_error_for_time_out));
        }
        iApiSubscriberCallBack.onError(errorResponse);
        onComplete();
    }

    @Override
    public void onNext(T t) {
        if (t instanceof BaseApiResultData) {
            BaseApiResultData baseApiResultData = (BaseApiResultData) t;
            if (baseApiResultData.getCode() == 201) {
                //成功
                iApiSubscriberCallBack.onNext(t);
            } else {
                //用户级错误
                ErrorResponse errorResponse = new ErrorResponse();
                String errorMessage = ErrorCodeMessageUtil.getMessageByErrorCode(baseApiResultData.getCode());
                errorResponse.setMessage(TextUtils.isEmpty(errorMessage) ? baseApiResultData.getMessage() : errorMessage);
                errorResponse.setCode(baseApiResultData.getCode());
//                if (baseApiResultData.getCode() == 4010) {
//                    //Token失效
//                    EventBus.getDefault().post(new EvenBusEven.RefreshToken());
//                    errorResponse.setMessage(errorResponse.getMessage() + ",请返回重新操作");
//                }
                if (baseApiResultData.getCode() == HttpURLConnection.HTTP_UNAUTHORIZED && null != baseNetView) {
                    //重新登录
                    baseNetView.catchApiSubscriberError(errorResponse);
                }
                iApiSubscriberCallBack.onError(errorResponse);
            }
        } else {
            //非统一接口格式返回的数据，自行处理
            iApiSubscriberCallBack.onNext(t);
        }

    }

}
