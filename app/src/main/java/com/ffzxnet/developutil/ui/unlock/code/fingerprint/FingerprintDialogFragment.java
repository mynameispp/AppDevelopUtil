package com.ffzxnet.developutil.ui.unlock.code.fingerprint;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ffzxnet.developutil.R;

import javax.crypto.Cipher;

import androidx.annotation.Nullable;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.fragment.app.DialogFragment;

public class FingerprintDialogFragment extends DialogFragment {
    //Android 29,这里29版本的代码建议直接在activity里面使用，这里只是方便测试
    private BiometricPrompt biometricPrompt;
    private CancellationSignal mCancellationSignal;
    //Android <29
    private FingerprintManagerCompat fingerprintManagerCompat;
    private androidx.core.os.CancellationSignal mCancellationSignalAndroidX;

    private Cipher mCipher;

    private Context mActivity;

    private TextView errorMsg;

    /**
     * 标识是否是用户主动取消的认证。
     */
    private boolean isSelfCancelled;

    public void setCipher(Cipher cipher) {
        mCipher = cipher;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fingerprint_dialog, container, false);
        errorMsg = v.findViewById(R.id.error_msg);
        TextView cancel = v.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                stopListening();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mCancellationSignal = new CancellationSignal();
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.height = 0;
            layoutParams.alpha=0;
            window.setAttributes(layoutParams);
        } else {
            mCancellationSignalAndroidX = new androidx.core.os.CancellationSignal();
            fingerprintManagerCompat = FingerprintManagerCompat.from(mActivity);
        }
        // 开始指纹认证监听
        startListening(mCipher);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 停止指纹认证监听
        stopListening();
    }

    private void startListening(Cipher cipher) {
        isSelfCancelled = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            KeyguardManager keyguardManager = (KeyguardManager) mActivity.getSystemService(Context.KEYGUARD_SERVICE);
            if (null != keyguardManager && keyguardManager.isKeyguardSecure()) {
                BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(mActivity)
                        .setTitle("指纹验证")
                        .setDescription("请验证指纹")
                        .setNegativeButton("取消", mActivity.getMainExecutor(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dismiss();
                            }
                        }).build();
                biometricPrompt.authenticate(mCancellationSignal, mActivity.getMainExecutor(), new QMyCallBack());
            }
        } else {
            FingerprintManagerCompat.CryptoObject cryptoObject = new FingerprintManagerCompat.CryptoObject(cipher);
            fingerprintManagerCompat.authenticate(cryptoObject, 0, mCancellationSignalAndroidX, new MyCallBack(), null);
        }
    }

    public class MyCallBack extends FingerprintManagerCompat.AuthenticationCallback {

        // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            if (!isSelfCancelled) {
                errorMsg.setText(errString);
                Log.e("TAG", "errMsgId=" + errMsgId);
                Toast.makeText(mActivity, "errMsgId=" + errMsgId, Toast.LENGTH_SHORT).show();
                if (errMsgId == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                    Log.e("TAG", "" + errString);
                    dismiss();
                }
            }
        }

        // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
        @Override
        public void onAuthenticationFailed() {
            errorMsg.setText("指纹认证失败，请再试一次");
            Log.e("TAG", "onAuthenticationFailed");
        }

        //错误时提示帮助，比如说指纹错误，我们将显示在界面上 让用户知道情况
        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            errorMsg.setText(helpString);
            Log.e("TAG", "helpString=" + helpString);
        }

        // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
//            FingerprintManagerCompat.CryptoObject cryptoObject = result.getCryptoObject();
//            try {
//                byte[] bytes = cryptoObject.getCipher().doFinal();
//            } catch (BadPaddingException e) {
//                e.printStackTrace();
//            } catch (IllegalBlockSizeException e) {
//                e.printStackTrace();
//            }
            if (onFingerprintSetting != null) {
                onFingerprintSetting.onFingerprint(true);
            }
            dismiss();
        }
    }

    //Android 29
    public class QMyCallBack extends BiometricPrompt.AuthenticationCallback {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            if (!isSelfCancelled) {
                if (errorCode == BiometricPrompt.BIOMETRIC_ERROR_LOCKOUT
                        || errorCode == BiometricPrompt.BIOMETRIC_ERROR_USER_CANCELED) {
                    dismiss();
                }
            }
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
        }

        @Override
        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            if (onFingerprintSetting != null) {
                onFingerprintSetting.onFingerprint(true);
            }
            dismiss();
        }
    }

    private void stopListening() {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
            isSelfCancelled = true;
        } else if (mCancellationSignalAndroidX != null) {
            mCancellationSignalAndroidX.cancel();
            mCancellationSignalAndroidX = null;
            isSelfCancelled = true;
        }
    }

    private OnFingerprintSetting onFingerprintSetting;

    public void setOnFingerprintSetting(
            OnFingerprintSetting onFingerprintSetting) {
        this.onFingerprintSetting = onFingerprintSetting;
    }

    public interface OnFingerprintSetting {
        void onFingerprint(boolean isSucceed);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopListening();
    }
}
