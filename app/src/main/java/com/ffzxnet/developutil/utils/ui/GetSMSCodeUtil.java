package com.ffzxnet.developutil.utils.ui;

import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


/**
 * 创建者： feifan.pi
 */

public class GetSMSCodeUtil {
    private static boolean going = true;
    private static Disposable disposable;
    //是否在倒计时
    private static boolean isCountDowning = false;

    /**
     * 停止倒计时
     */
    public static void stopcountDown() {
        if (isCountDowning) {
            going = false;
            isCountDowning = false;
        }
    }

    public static void countDownTimer(View view) {
        if (view instanceof TextView) {
            countdownForTextView((TextView) view);
        }
    }

    /**
     * 倒计时
     */
    private static void countdownForTextView(final TextView textView) {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
        going = true;
        Observable.timer(120, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        textView.setClickable(aLong <= 0);//是否可点击
                        if (aLong > 0) {
                            String content = aLong + "s";
                            SpannableString span = new SpannableString(content);
//                            int index = content.indexOf("后");
//                            int index = content.length();
//                            span.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MyApplication.getContext(), R.color.green_2e)),
//                                    0, index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //倒计时数字颜色
                            textView.setText(span);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (!going) {
                            //强行停止的
                            textView.setText("发送验证码");
                        } else {
                            isCountDowning = false;
                            textView.setText("重新发送");
                        }
                    }
                });
    }
}
