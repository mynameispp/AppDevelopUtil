package com.ffzxnet.developutil.net;

import android.net.ParseException;
import android.text.TextUtils;

import com.ffzxnet.countrymeet.R;
import com.ffzxnet.countrymeet.application.MyApplication;
import com.ffzxnet.countrymeet.base.net.BaseApiResultData;
import com.ffzxnet.countrymeet.net.retrofit_gson_factory.ResultErrorException;
import com.ffzxnet.countrymeet.utils.tools.GsonUtil;
import com.ffzxnet.countrymeet.utils.tools.LogUtil;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 创建者： feifan.pi 在 2017/3/14.
 */

public class ApiSubscriber<T> implements Observer<T> {
    private IApiSubscriberCallBack<T> iApiSubscriberCallBack;
    //取消订阅
    private Disposable disposable;

    public ApiSubscriber(IApiSubscriberCallBack<T> iApiSubscriberCallBack) {
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
        BaseApiResultData baseApiResultData = (BaseApiResultData) t;
        if (baseApiResultData.getCode() == 1) {
            //成功
            iApiSubscriberCallBack.onNext(t);
        } else {
            //用户级错误
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(baseApiResultData.getMessage());
            switch (baseApiResultData.getCode()) {

            }
            iApiSubscriberCallBack.onError(errorResponse);
        }

    }

}
