package com.ffzxnet.developutil.utils.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    // 日期格式
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT2 = "yyyy-MM-d";
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM = "yyyy-MM";
    public static final String FORMAT_YYYY = "yyyy";
    public static final String FORMAT_HH_MM = "HH:mm";
    public static final String FORMAT_HHMM = "HHmm";
    public static final String FORMAT_HH_MM_SS = "HH:mm:ss";
    public static final String FORMAT_MM_SS = "mm:ss";
    public static final String FORMAT_MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String FORMAT_MM_DD_HH_MM_SS = "MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YYYY_M_D_HH_MM = "yyyy-M-d HH:mm";
    public static final String FORMAT_YYYY2MM2DD = "yyyy.MM.dd";
    public static final String FORMAT_YYYY2MM2DD2W = "yyyy.MM.dd  E";
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String FORMAT_YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String FORMAT_YYYY2MM2DD_HH_MM = "yyyy.MM.dd HH:mm";
    public static final String FORMAT_MMCDD_HH_MM = "MM月dd日 HH:mm";
    public static final String FORMAT_MMCDD = "MM月dd日";
    public static final String FORMAT_MMCDD2 = "MM.dd";
    public static final String FORMAT_YYYYCMMCDD = "yyyy年MM月dd日";

    public static final long ONE_DAY = 1000 * 60 * 60 * 24;

    public static String getThisTime(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, new Locale("CN"));
        return sdf.format(new Date());
    }

    public static String getTimeFormatText(Date date) {
        long minute = 60 * 1000;// 1分钟
        long hour = 60 * minute;// 1小时
        long day = 24 * hour;// 1天
        long month = 31 * day;// 月
        long year = 12 * month;// 年
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > day * 3) {
            return "";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    public static String getTimeFormatText2(Date date) {
        long minute = 60 * 1000;// 1分钟
        long hour = 60 * minute;// 1小时
        long day = 24 * hour;// 1天
        long month = 31 * day;// 月
        long year = 12 * month;// 年
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > day * 2) {
            return "";
        }
//        if (diff > day) {
//            r = (diff / day);
//            return "昨天";
//        }
        if (diff > hour) {
            r = (diff / hour);
            if (r < 6) {
                return r + "小时前";
            } else {
                return "";
            }
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 取得当月天数
     */
    public static int getCurrentMonthLastDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 得到指定月的天数
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    //返回当前时间
    public static String getNowDate(String format) {
        Date date = new Date();
        return new SimpleDateFormat(format, Locale.CHINA).format(date);
    }

    //返回当前年份
    public static int getYear() {
        Date date = new Date();
        String year = new SimpleDateFormat("yyyy", Locale.CHINA).format(date);
        return Integer.parseInt(year);
    }

    //返回当前月份
    public static int getMonth() {
        Date date = new Date();
        String month = new SimpleDateFormat("MM", Locale.CHINA).format(date);
        return Integer.parseInt(month);
    }

    //返回当周第几天，星期日为第一天
    public static int getDayOfWeek() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    //返回当天周几
    public static String getDayOfWeekS() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String weekOfDay;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 2:
                weekOfDay = "星期一";
                break;
            case 3:
                weekOfDay = "星期二";
                break;
            case 4:
                weekOfDay = "星期三";
                break;
            case 5:
                weekOfDay = "星期四";
                break;
            case 6:
                weekOfDay = "星期五";
                break;
            case 7:
                weekOfDay = "星期六";
                break;
            default:
                weekOfDay = "星期日";
                break;
        }
        return weekOfDay;
    }

    public static String getDayOfWeekToS(int i) {
        String weekOfDay;
        switch (i) {
            case 2:
                weekOfDay = "星期一";
                break;
            case 3:
                weekOfDay = "星期二";
                break;
            case 4:
                weekOfDay = "星期三";
                break;
            case 5:
                weekOfDay = "星期四";
                break;
            case 6:
                weekOfDay = "星期五";
                break;
            case 7:
                weekOfDay = "星期六";
                break;
            default:
                weekOfDay = "星期日";
                break;
        }
        return weekOfDay;
    }

    /**
     * yyyy-M-d
     *
     * @param dateString yyyy-M-d
     * @return
     */
    public static String getDayOfWeekS(String dateString) {
        Date date;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sf.parse(dateString);// 日期转换为时间戳
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        String weekOfDay;
        switch (i) {
            case 2:
                weekOfDay = "星期一";
                break;
            case 3:
                weekOfDay = "星期二";
                break;
            case 4:
                weekOfDay = "星期三";
                break;
            case 5:
                weekOfDay = "星期四";
                break;
            case 6:
                weekOfDay = "星期五";
                break;
            case 7:
                weekOfDay = "星期六";
                break;
            default:
                weekOfDay = "星期日";
                break;
        }
        return weekOfDay;
    }

    /**
     * 返回指定日是周几
     *
     * @param dateString
     * @param format     格式
     * @return
     */
    public static String getDayOfWeekS(String dateString, String format) {
        Date date;
        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            date = sf.parse(dateString);// 日期转换为时间戳
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        String weekOfDay;
        switch (i) {
            case 2:
                weekOfDay = "星期一";
                break;
            case 3:
                weekOfDay = "星期二";
                break;
            case 4:
                weekOfDay = "星期三";
                break;
            case 5:
                weekOfDay = "星期四";
                break;
            case 6:
                weekOfDay = "星期五";
                break;
            case 7:
                weekOfDay = "星期六";
                break;
            default:
                weekOfDay = "星期日";
                break;
        }
        return weekOfDay;
    }

    //返回指定日期在一周的第几天
    public static int getDayOfWeek(String dateTime) {
        Calendar cal = Calendar.getInstance();
        if (dateTime.equals("")) {
            cal.setTime(new Date(System.currentTimeMillis()));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
            Date date;
            try {
                date = sdf.parse(dateTime);
            } catch (ParseException e) {
                date = null;
                e.printStackTrace();
            }
            if (date != null) {
                cal.setTime(new Date(date.getTime()));
            }
        }
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String getCurrentTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * @param time   时间戳
     * @param format
     * @return
     */
    public static String getTimeFormat(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date(Long.parseLong(time)));
    }

    /**
     * @param time   时间戳
     * @param format
     * @return
     */
    public static String getTimeFormat(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date(time));
    }

    /**
     * 比较当前时间是否在指定时间范围内
     *
     * @param starDate    yyyy-M-d
     * @param endDate     yyyy-M-d
     * @param currentTime yyyy-M-d
     * @return
     */
    public static boolean isRange(String starDate, String endDate, String currentTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.M.d", Locale.getDefault());
        Date sDate, eDate, cDate;
        try {
            sDate = sdf.parse(starDate);
            eDate = sdf.parse(endDate);
            cDate = sdf.parse(currentTime);
            return cDate.getTime() >= sDate.getTime() && cDate.getTime() <= eDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 比较当前时间是否在指定时间范围内
     *
     * @return
     */
    public static long getTimeLongFromString(String dates, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(dates);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
