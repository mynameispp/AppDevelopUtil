package com.ffzxnet.developutil.utils.ui;

import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import com.ffzxnet.countrymeet.R;
import com.ffzxnet.countrymeet.application.MyApplication;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 创建者： feifan.pi 在 2017/4/28.
 */

public class GetSMSCodeUtil {
    private static boolean going = true;
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
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                int i = 120;//倒计时的秒数
                isCountDowning = true;
                while (i >= 0 && going) {
                    try {
                        emitter.onNext(i);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i--;
                }
                if (!going) {
                    //强制停止
                    emitter.onNext(-1);
                }
                going = true;
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())// 此方法为上面发出事件设置线程为IO线程
                .observeOn(AndroidSchedulers.mainThread())// 为消耗事件设置\线程为UI线程
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        textView.setClickable(integer > 0 ? false : true);//是否可点击
                        //倒计时背景颜色
//                        textView.setBackgroundResource(integer > 0 ? R.drawable.btn_get_msm_code_down
//                                : R.drawable.btn_get_msm_code_up);
                        if (integer > 0) {
                            String content = integer + "s";
                            SpannableString span = new SpannableString(content);
//                            int index = content.indexOf("后");
                            int index = content.length();
//                            span.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MyApplication.getContext(), R.color.green_2e)),
//                                    0, index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //倒计时数字颜色
                            textView.setText(span);
                        } else if (integer == -1) {
                            //强行停止的
                            textView.setText(MyApplication.getStringByResId(R.string.sms_countdown_send));
                        } else {
                            isCountDowning = false;
                            textView.setText(MyApplication.getStringByResId(R.string.sms_countdown_again));
                        }
                    }
                });
    }
}
