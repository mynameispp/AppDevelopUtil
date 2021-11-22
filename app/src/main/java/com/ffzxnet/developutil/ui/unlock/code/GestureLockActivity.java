package com.ffzxnet.developutil.ui.unlock.code;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.ui.unlock.code.fingerprint.FingerprintDialogFragment;
import com.ffzxnet.developutil.ui.unlock.code.gesture.GestureLockLayout;
import com.ffzxnet.developutil.ui.unlock.code.gesture.SetGestureLockActivity;
import com.ffzxnet.developutil.utils.tools.DeviceUtil;
import com.ffzxnet.developutil.utils.tools.MMKVUtil;
import com.ffzxnet.developutil.utils.ui.ToastUtil;

import javax.crypto.Cipher;

import androidx.constraintlayout.widget.Group;
import butterknife.BindView;

/**
 * 手势解锁
 */
public class GestureLockActivity extends BaseActivity {
    @BindView(R.id.gestureLock)
    GestureLockLayout mGestureLockLayout;
    @BindView(R.id.hintTV)
    TextView hintTV;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.group)
    Group group;
    private Context mContext;
    private Animation animation;

    /**
     * 最大解锁次数
     */
    private int mNumber = 5;
    /**
     * change:修改手势  login:登录
     */
    private String type;

    /**
     * true:设置   false:未设置
     */
    private Boolean isFingerprint, isGesture;

    private FingerprintDialogFragment dialogFragment;
    private Cipher cipher;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_gesture_lock;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "手势或指纹验证");
        initView();
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    protected void initView() {
        mContext = this;
        type = getIntent().getStringExtra("type");
        isGesture = MMKVUtil.getInstance().getBoolean(MMKVUtil.ISGESTURELOCK_KEY, false);
        isFingerprint = MMKVUtil.getInstance().getBoolean(MMKVUtil.ISFINGERPRINT_KEY, false);
        if (isGesture) {
            //手势解密
            group.setVisibility(View.VISIBLE);
            //线的颜色
            //普通状态的颜色
            mGestureLockLayout.setMatchedPathColor(MyApplication.getColorByResId(R.color.colorPrimary));
            //匹配成功时的颜色
            mGestureLockLayout.setTouchedPathColor(MyApplication.getColorByResId(R.color.colorPrimary));
            //匹配失败时的颜色
//            mGestureLockLayout.setUnmatchedPathColor(MyApplication.getColorByResId(R.color.colorPrimary));
            //圆点颜色
            //未连接时颜色
            mGestureLockLayout.setNoFingerColor(MyApplication.getColorByResId(R.color.colorAccent));
            //连接时颜色
            mGestureLockLayout.setFingerTouchColor(MyApplication.getColorByResId(R.color.colorPrimary));
            //匹配失败时的颜色
//            mGestureLockLayout.setUnmatchedColor(MyApplication.getColorByResId(R.color.colorPrimary));
            setGestureListener();
        }

        if ("login".equals(type) && isFingerprint) {
            setFingerprint();
        }

    }

    //设置指纹验证
    private void setFingerprint() {
        if (DeviceUtil.supportFingerprint(this)) {
            DeviceUtil.initKey(); //生成一个对称加密的key
            //生成一个Cipher对象
            cipher = DeviceUtil.initCipher();
        }
        if (cipher != null) {
            showFingerPrintDialog(cipher);
        }
    }

    private void showFingerPrintDialog(Cipher cipher) {
        dialogFragment = new FingerprintDialogFragment();
        dialogFragment.setCipher(cipher);
        dialogFragment.show(getSupportFragmentManager(), "fingerprint");

        dialogFragment.setOnFingerprintSetting(new FingerprintDialogFragment.OnFingerprintSetting() {
            @Override
            public void onFingerprint(boolean isSucceed) {
                if (isSucceed) {
                    ToastUtil.showToastShort("指纹解锁成功！");
//                    redirectActivity(UnlockActivity.class);
                    finish();
                } else {
                    ToastUtil.showToastShort("指纹解锁失败！");
                }
            }
        });
    }

    //设置手势验证
    private void setGestureListener() {
        String gestureLockPwd = MMKVUtil.getInstance().getString(MMKVUtil.GESTURELOCK_KEY, "");
        if (!TextUtils.isEmpty(gestureLockPwd)) {
            mGestureLockLayout.setAnswer(gestureLockPwd);
        } else {
            ToastUtil.showToastShort("没有设置过手势密码");
        }
        mGestureLockLayout.setDotCount(3);
        mGestureLockLayout.setMode(GestureLockLayout.VERIFY_MODE);
        //设置手势解锁最大尝试次数 默认 5
        mGestureLockLayout.setTryTimes(5);
        animation = AnimationUtils.loadAnimation(this, R.anim.shake);
        mGestureLockLayout.setOnLockVerifyListener(new GestureLockLayout.OnLockVerifyListener() {
            @Override
            public void onGestureSelected(int id) {
                //每选中一个点时调用
            }

            @Override
            public void onGestureFinished(boolean isMatched) {
                //绘制手势解锁完成时调用
                if (isMatched) {
                    //验证成功
                    if ("change".equals(type)) {
                        redirectActivity(SetGestureLockActivity.class);
                    } else if ("login".equals(type)) {
                        ToastUtil.showToastShort("手势解锁成功！");
//                        redirectActivity(UnlockActivity.class);
                    }
                    finish();
                } else {
                    hintTV.setVisibility(View.VISIBLE);
                    mNumber = --mNumber;
                    hintTV.setText("你还有" + mNumber + "次机会");
                    hintTV.startAnimation(animation);
                    mGestureLockLayout.startAnimation(animation);
                    DeviceUtil.setVibrate(mContext);
                }
                resetGesture();
            }

            @Override
            public void onGestureTryTimesBoundary() {
                //超出最大尝试次数时调用
                mGestureLockLayout.setTouchable(false);
            }
        });
    }

    /**
     * 重置手势布局（只是布局）
     */
    private void resetGesture() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mGestureLockLayout.resetGesture();
            }
        }, 300);
    }

}
