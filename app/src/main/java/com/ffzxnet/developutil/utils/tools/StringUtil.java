package com.ffzxnet.developutil.utils.tools;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 创建者： feifan.pi 在 2017/4/17.
 */

public class StringUtil {

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 判断是否是数字字符串
     *
     * @param num
     * @return
     */
    public static boolean isNumber(String num) {
        if (TextUtils.isEmpty(num)) {
            return false;
        }
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数字字符串类型转换成数字类型
     *
     * @param num
     * @return
     */
    public static int stringToInt(String num) {
        if (isNumber(num)) {
            return Integer.parseInt(num);
        } else {
            return -1;
        }
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = null;
        time = toDate(sdate);

        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)

                if (Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) == 1) {
                    ftime = "刚刚";
                } else {
                    ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                            + "分钟前";
                }
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        return toDate(sdate, dateFormater.get());
    }

    public static Date toDate(String sdate, SimpleDateFormat dateFormater) {
        try {
            return dateFormater.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 处理图片组切割的方法
     */
    public static ArrayList<String> handleImg(String image) {
        String[] split = image.split(",");
        ArrayList<String> imageUrls = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            imageUrls.add(split[i]);
        }
        return imageUrls;
    }

    /**
     * 获取当前时间的String类型
     */

    public static String getCurrentTime() {
        return dateFormater.get().format(Calendar.getInstance().getTime());
    }
}
