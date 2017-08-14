package com.ffzxnet.developutil.utils.tools;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @Package: com.feifanzhixing.o2o.utils
 * @ClassName: CalendarUtil.java
 * @Description:TODO 获取时间工具类
 * @author: pifeifan
 * @date: 2015-10-22
 * <p>
 * Copyright @ 2015 51ZhiYe
 */
public class CalendarUtil {
    /**
     * 时分格式
     */
    public final static String DATE_FORMAT_H_M = "HH:mm";
    /**
     * 默认的日期格式
     */
    public final static String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm";
    public final static String DATE_FORMAT_yyyyMMddHHmm = "yyyyMMddHHmm";
    public final static String DATE_FORMAT_YMD = "yyyy-MM-dd";
    public final static String DATE_FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";


    private static CalendarUtil calendarUtil = new CalendarUtil();

    public static CalendarUtil getInit() {
        return calendarUtil;
    }

    private static int weeks = 0;// 用来全局控制 上一周，本周，下一周的周数变化
    private int MaxDate; // 一月最大天数
    private int MaxYear; // 一年最大天数


    /**
     * 获取当前年份
     */
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     */
    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 获取今天在本年的第几天
     */
    public static int getDayOfYear() {
        return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获得今天在本月的第几天(获得当前日)
     */
    public static int getDayOfMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得今天在本周的第几天
     */
    public static int getDayOfWeek() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取当前月的第几周
     */
    public static int getWeekOfMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    /**
     * 获得半年后的日期
     */
    public static Date getTimeYearNext() {
        Calendar.getInstance().add(Calendar.DAY_OF_YEAR, 183);
        return Calendar.getInstance().getTime();
    }

    /**
     * 格式化日期 yyyy-MM-dd
     */
    public static String convertDateToString(Date dateTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return df.format(dateTime);
    }

    /**
     * 格式化日期 yyyy-MM-dd
     */
    public static String convertDateToString(Date dateTime, String dataFormat) {
        SimpleDateFormat df = new SimpleDateFormat(dataFormat, Locale.getDefault());
        return df.format(dateTime);
    }

    /**
     * 获取两个日期之间间隔天数
     */
    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        long day = 0;
        try {
            Date date = myFormatter.parse(sj1);
            Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    /**
     * 获取当前星期几
     */
    public static String getWeek(String sdate) {
        // 再转换为时间
        Date date = CalendarUtil.strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE", Locale.getDefault()).format(c.getTime());
    }

    /**
     * 将字符时间转换成Date类型
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 获取两时间的差
     */
    public static long getDays(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }

    /**
     * 获取本月最后一天日期
     */
    public String getDefaultDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取上月第一天日期
     */
    public String getPreviousMonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
        // lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天

        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取本月第一天日期
     */
    public static String getFirstDayOfMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取本周日的日期
     */
    public String getCurrentWeekday() {
        weeks = 0;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获取当天日期  dateformat=格式
     */
    public static String getNowTime(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat, Locale.getDefault());// 可以方便地修改日期格式
        String hehe = dateFormat.format(now);
        return hehe;
    }

    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    /**
     * 获取本周一日期
     */
    public static String getMondayOFWeek() {
        weeks = 0;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    public String getSaturday() {
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获取上周日日期
     */
    public String getPreviousWeekSunday() {
        weeks = 0;
        weeks--;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获取上周一日期
     */
    public String getPreviousWeekday() {
        weeks--;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获取下周一日期
     */
    public String getNextMonday() {
        weeks++;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获取下周日日期
     */
    public String getNextSunday() {

        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    private int getMonthPlus() {
        Calendar cd = Calendar.getInstance();
        int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
        cd.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        cd.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        MaxDate = cd.get(Calendar.DATE);
        if (monthOfNumber == 1) {
            return -MaxDate;
        } else {
            return 1 - monthOfNumber;
        }
    }

    /**
     * 获取上月最后一天的日期
     */
    public String getPreviousMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取下月第一天日期
     */
    public String getNextMonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取下月最后一天日期
     */
    public String getNextMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 加一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取明年最后一天日期
     */
    public String getNextYearEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        lastDate.roll(Calendar.DAY_OF_YEAR, -1);
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取明年第一天日期
     */
    public String getNextYearFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        str = sdf.format(lastDate.getTime());
        return str;

    }

    /**
     * 获取当前年份
     */
    private int getMaxYear() {
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        return MaxYear;
    }

    private int getYearPlus() {
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1) {
            return -MaxYear;
        } else {
            return 1 - yearOfNumber;
        }
    }

    /**
     * 获取本年的第一天日期
     */
    public String getCurrentYearFirst() {
        int yearPlus = this.getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus);
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        return preYearDay;
    }

    /**
     * 获得本年最后一天的日期
     */
    public String getCurrentYearEnd() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        return years + "-12-31";
    }

    /**
     * 获得上年第一天的日期
     */
    public String getPreviousYearFirst() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        years_value--;
        return years_value + "-1-1";
    }

    /**
     * 获得上年最后一天的日期
     */
    public String getPreviousYearEnd() {
        weeks--;
        int yearPlus = this.getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * weeks
                + (MaxYear - 1));
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        return preYearDay;
    }

    /**
     * 获取本季度第一天
     */
    public String getThisSeasonFirstTime(int month) {
        int array[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
//		int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
//		int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days;
        return seasonDate;

    }

    /**
     * 获取本季度最后一天
     */
    public String getThisSeasonFinallyTime(int month) {
        int array[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
//		int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

//		int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + end_month + "-" + end_days;
        return seasonDate;

    }

    private int getLastDayOfMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 0;
    }

    public boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public boolean isLeapYear2(int year) {
        return new GregorianCalendar().isLeapYear(year);
    }

    /**
     * 比较时间大小
     *
     * @param DATE1
     * @param DATE2
     * @return DATE1 > DATE2
     */
    public boolean compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return true;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return false;
            } else {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }


    /**
     * 比较时间大小
     *
     * @param DATE1
     * @param DATE2
     * @return DATE1 > DATE2
     */
    public static boolean compareDate(String DATE1, String DATE2, String format) {
        DateFormat df = new SimpleDateFormat(format);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return true;
            } else if (dt1.getTime() <= dt2.getTime()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }


    /**
     * 获取日期的中文名(今天、明天、后天、昨天、前天)
     *
     * @param textDate
     * @return
     */

    public static String getDateChineseName(String textDate, String format) {
        Date date = textToDate(textDate, format);
        if (date == null) {
            return null;
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);

        int diff = day1 - day2;
        if (diff == 0) {
            return getTextTime(date, DATE_FORMAT_H_M);
        } else if (diff == 1) {
            //return "明天" + getTextTime(date, DATE_FORMAT_H_M);
        } else if (diff == 2) {
            //return "后天" + getTextTime(date, DATE_FORMAT_H_M);
        } else if (diff == -1) {
            return "昨天" + getTextTime(date, DATE_FORMAT_H_M);
        } else if (diff == -2) {
            //return "前天" + getTextTime(date, DATE_FORMAT_H_M);
        }
        return textDate;
    }

    /**
     * 将字符串日期转换为Date类型
     *
     * @param textDate 字符串
     * @param format   格式
     * @return
     */
    public static Date textToDate(String textDate, String format) {
        if (TextUtils.isEmpty(textDate)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date;
        try {
            date = simpleDateFormat.parse(textDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    /**
     * 获取时间(时分)
     *
     * @param date   日期对象
     * @param format 格式
     * @return
     */
    public static String getTextTime(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 获取今天像个x天的日期
     *
     * @param format   格式
     * @param deltaDay 相隔天数(可为负数)
     * @return
     */
    public static String getDateByToday(String format, int deltaDay) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, deltaDay);
        String yesterday = new SimpleDateFormat(format).format(cal.getTime());
        return yesterday;
    }

    /**
     * 获取本周一日期
     *
     * @return
     */
    public static String getMondayOfThisWeek() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_YMD, Locale.CHINA);
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return format.format(c.getTime());
    }

    /**
     * 获取时分秒格式的时间
     *
     * @param time 秒
     * @return
     */
    public static String getChineseHMS(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "0秒";
        } else {
            if (time < 60) {
                return time + "秒";
            } else {
                minute = time / 60;
                if (minute < 60) {
                    second = time % 60;
                    timeStr = unitFormat(minute) + "分" + unitFormat(second) + "秒";
                } else {
                    hour = minute / 60;
                    if (hour > 99)
                        return "99:59:59";
                    minute = minute % 60;
                    second = time - hour * 3600 - minute * 60;
                    timeStr = unitFormat(hour) + "时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
                }
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */

    public static Date parse(String strDate, String pattern) {

        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        if (date == null) {
            return null;
        }
        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }
}
