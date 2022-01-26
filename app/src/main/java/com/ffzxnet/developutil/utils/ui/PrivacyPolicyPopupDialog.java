package com.ffzxnet.developutil.utils.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.ui.web.WebViewActivity;
import com.ffzxnet.developutil.utils.tools.ClickTooQucik;
import com.ffzxnet.developutil.utils.tools.TxtSpannableUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * 隐私政策弹窗
 */
public class PrivacyPolicyPopupDialog extends DialogFragment implements View.OnClickListener {
    private final String privacyContent = "请您务必审慎阅读，充分理解“服务协议”和“隐私政策”各条款，包括但不限于：" +
            "    为了向你提供分享等服务，我们需要收集你的设备信息，操作日志等个人信息。你可以在手机“设置”中查看，变更，删除" +
            "    个人信息并管理你的权限。\n你可以阅读《服务协议》和《隐私政策》了解详细信息。如你同意，点击“同意”开始接受我们的服务。";
    private Listen listen;

    public void setListen(Listen listen) {
        this.listen = listen;
    }

    public interface Listen {
        void privacyPolicyAgreeClick(boolean agree);
    }

    public PrivacyPolicyPopupDialog() {
        super();
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_privacy_policy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.dialog_privacy_policy_no).setOnClickListener(this);
        view.findViewById(R.id.dialog_privacy_policy_yes).setOnClickListener(this);

        TextView content = view.findViewById(R.id.popu_privacy_policy_content);
        int oneL = privacyContent.indexOf("《");
        int oneR = privacyContent.indexOf("》") + 1;
        int twoL = privacyContent.lastIndexOf("《");
        int twoR = privacyContent.lastIndexOf("》") + 1;
        TxtSpannableUtil spannableUtil = new TxtSpannableUtil(privacyContent);
        spannableUtil.setSpanColor(R.color.colorPrimary, oneL, oneR)
                .setSpanColor(R.color.colorPrimary, twoL, twoR)
                .setTxtClickListen(new CommonClickableSpan(), oneL, oneR)
                .setTxtClickListen(new CommonClickableSpan2(), twoL, twoR);
        content.setText(spannableUtil.build());
        content.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
        content.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
    }

    public class CommonClickableSpan extends ClickableSpan implements View.OnClickListener {
        @Override
        public void onClick(View widget) {
            //服务协议
            Intent intent = new Intent(getContext(), WebViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(MyConstans.KEY_DATA, "https://www.baidu.com");
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.clearShadowLayer();
            ds.setColor(MyApplication.getColorByResId(R.color.colorPrimaryDark));
        }
    }

    public class CommonClickableSpan2 extends ClickableSpan implements View.OnClickListener {
        @Override
        public void onClick(View widget) {
            //隐私政策
            Intent intent = new Intent(getContext(), WebViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(MyConstans.KEY_DATA, "https://www.baidu.com");
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.clearShadowLayer();
            ds.setColor(MyApplication.getColorByResId(R.color.colorPrimaryDark));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (null != getDialog()) {
            Window dialogWindow = getDialog().getWindow();
            getDialog().setCanceledOnTouchOutside(false);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.CENTER);
//            lp.width = (int) (MyConstant.Screen_Width * 0.7); // 宽度
//            lp.height = (int) (XGConstant.Screen_Height * 0.5); // 高度
            setCancelable(false);
//         lp.alpha = 0.7f; // 透明度
            dialogWindow.setAttributes(lp);
        }
    }

    @Override
    public void onClick(View view) {
        if (ClickTooQucik.isFastClick() || null == listen) {
            return;
        }
        if (view.getId() == R.id.dialog_privacy_policy_no) {
            listen.privacyPolicyAgreeClick(false);
        } else {
            listen.privacyPolicyAgreeClick(true);
        }
        dismiss();
    }
}
