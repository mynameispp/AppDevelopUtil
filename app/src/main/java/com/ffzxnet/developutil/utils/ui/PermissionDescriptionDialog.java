package com.ffzxnet.developutil.utils.ui;

import android.Manifest;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ffzxnet.developutil.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;

/**
 * 申请权限说明弹窗
 */
public class PermissionDescriptionDialog extends DialogFragment {
    Map<String, String> permissionDescriptionInfo;
    public static final String PermissionData = "PermissionData";
    @BindView(R.id.permission_description_txt)
    TextView permissionDescriptionTxt;

    {
        permissionDescriptionInfo = new HashMap<>();
        permissionDescriptionInfo.put(Manifest.permission.INTERNET
                , "<b><tt>网络权限<tt></b> <br>校里加载数据页面，请求数据需要网络权限");
        permissionDescriptionInfo.put(Manifest.permission.WRITE_EXTERNAL_STORAGE
                , "<b><tt>访问设备<tt></b> <br>保存登录信息下次免登陆，保存拍照，保存设置信息操作，需要访问设备存储权限");
        permissionDescriptionInfo.put(Manifest.permission.READ_EXTERNAL_STORAGE
                , "<b><tt>访问设备<tt></b> <br>读取登录信息免登陆，访问本地相册，需要访问设备存储权限");
        permissionDescriptionInfo.put(Manifest.permission.CAMERA
                , "<b><tt>获取摄像头<tt></b> <br>拍照，扫码操作，需要获取摄像头权限");
        permissionDescriptionInfo.put(Manifest.permission.ACCESS_FINE_LOCATION
                , "<b><tt>获取设备位置<tt></b> <br>wifi打卡，需要获取位置权限");
        permissionDescriptionInfo.put(Manifest.permission.ACCESS_WIFI_STATE
                , "<b><tt>获取Wifi信息<tt></b> <br>wifi打卡，需要获取Wifi信息权限");
        permissionDescriptionInfo.put(Manifest.permission.ACCESS_NETWORK_STATE
                , "<b><tt>获取网络信息<tt></b> <br>监察网络信息，提示用户当前网络信息，需要获取网络信息权限");
        permissionDescriptionInfo.put(Manifest.permission.READ_PHONE_STATE
                , "<b><tt>获取设备信息<tt></b> <br>校里单点登录，Wifi打卡，消息推送，需要获取设备信息权限");
        permissionDescriptionInfo.put(Manifest.permission.CALL_PHONE
                , "<b><tt>拨打电话<tt></b> <br>点击拨打电话，需要拨打电话权限");
        permissionDescriptionInfo.put(Manifest.permission.USE_BIOMETRIC
                , "<b><tt>指纹解密<tt></b> <br>判断手机是否支持指纹，需要拨打电话权限");
        permissionDescriptionInfo.put(Manifest.permission.RECORD_AUDIO
                , "<b><tt>录音<tt></b> <br>录音需要录音权限");
    }

    private View.OnClickListener clickListener;

    public void setOnclickListen(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public PermissionDescriptionDialog() {
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
        return inflater.inflate(R.layout.dialog_permission_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        permissionDescriptionTxt = view.findViewById(R.id.permission_description_txt);
        view.findViewById(R.id.permission_description_sure).setOnClickListener(clickListener);

        if (getArguments() != null) {
            String[] permissionList = getArguments().getStringArray(PermissionData);
            if (null != permissionList) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : permissionList) {
                    if (permissionDescriptionInfo.get(s) != null) {
                        stringBuilder.append(permissionDescriptionInfo.get(s)).append("<br><br>");
                    }
                }
                permissionDescriptionTxt.setText(Html.fromHtml(stringBuilder.toString()));
            }
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
//            lp.height = (int) (MyConstant.Screen_Height * 0.8); // 高度
//            setCancelable(false);
//         lp.alpha = 0.7f; // 透明度
            dialogWindow.setAttributes(lp);
        }
    }
}
