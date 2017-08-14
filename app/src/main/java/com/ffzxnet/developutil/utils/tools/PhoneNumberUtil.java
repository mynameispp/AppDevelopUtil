package com.ffzxnet.developutil.utils.tools;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证手机号
 */
public class PhoneNumberUtil {
    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern
                .compile("^[1]((3[0-9])|(5[^4,\\D])|(7[0-3,6-8])|(8[0-9]))\\d{8}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 座机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isTelephone(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("0\\d{2,3}\\d{7,8}"); // 验证座机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 处理电话号码   返回：136****8888
     *
     * @param phone
     * @return
     */
    public static String getCodePhone(String phone) {
        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            return phone;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(phone.substring(0, 3));
        buffer.append("****");
        buffer.append(phone.substring(7, phone.length()));
        return buffer.toString();
    }
}
