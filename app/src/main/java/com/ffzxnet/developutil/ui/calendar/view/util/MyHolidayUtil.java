package com.ffzxnet.developutil.ui.calendar.view.util;

import com.necer.entity.Lunar;

import java.util.Arrays;
import java.util.List;

/**
 * 节假日和补班日
 */
public class MyHolidayUtil {


    /**
     * 根据公历获取假期
     *
     * @return
     */
    public static String getSolarHoliday(int solarYear, int solarMonth, int solarDay) {
        String message = "";
        if (solarMonth == 1 && solarDay == 1) {
            message = "元旦";
        } else if (solarMonth == 2 && solarDay == 14) {
            message = "情人节";
        } else if (solarMonth == 3 && solarDay == 8) {
            message = "妇女节";
        } else if (solarMonth == 3 && solarDay == 12) {
            message = "植树节";
        } else if (solarMonth == 3 && solarDay == 15) {
            message = "消费者";
        } else if (solarMonth == 4) {
            if (solarDay == 1) {
                message = "愚人节";
            } else if (solarDay >= 4 && solarDay <= 6) {
                if (solarYear <= 1999) {
                    int compare = (int) (((solarYear - 1900) * 0.2422 + 5.59) - ((solarYear - 1900) / 4));
                    if (compare == solarDay) {
                        message = "清明节";
                    }
                } else {
                    int compare = (int) (((solarYear - 2000) * 0.2422 + 4.81) - ((solarYear - 2000) / 4));
                    if (compare == solarDay) {
                        message = "清明节";
                    }
                }
            }
        } else if (solarMonth == 5 && solarDay == 1) {
            message = "劳动节";
        } else if (solarMonth == 5 && solarDay == 4) {
            message = "青年节";
        } else if (solarMonth == 6 && solarDay == 1) {
            message = "儿童节";
        } else if (solarMonth == 7 && solarDay == 1) {
            message = "建党节";
        } else if (solarMonth == 8 && solarDay == 1) {
            message = "建军节";
        } else if (solarMonth == 9 && solarDay == 10) {
            message = "教师节";
        } else if (solarMonth == 10 && solarDay == 1) {
            message = "国庆节";
        } else if (solarMonth == 10 && solarDay == 31) {
            message = "万圣节";
        } else if (solarMonth == 11 && solarDay == 25) {
            message = "感恩节";
        } else if (solarMonth == 12 && solarDay == 24) {
            message = "平安夜";
        } else if (solarMonth == 12 && solarDay == 25) {
            message = "圣诞节";
        }
        return message;
    }


    /**
     * 用于获取中国的传统节日
     *
     * @return 中国传统节日
     */
    public static String getLunarHoliday(Lunar lunar, Lunar nextLunar) {
        int lunarMonth = lunar.lunarMonth;
        int lunarDay = lunar.lunarDay;
        String message = "";
        if (lunarMonth == 1 && lunarDay == 1) {
            message = "春节";
        } else if (lunarMonth == 1 && lunarDay == 15) {
            message = "元宵节";
        } else if (lunarMonth == 2 && lunarDay == 2) {
            message = "龙抬头";
        } else if (lunarMonth == 5 && lunarDay == 5) {
            message = "端午节";
        } else if (lunarMonth == 7 && lunarDay == 7) {
            message = "七夕";
        } else if (lunarMonth == 7 && lunarDay == 15) {
            message = "中元节";
        } else if (lunarMonth == 8 && lunarDay == 15) {
            message = "中秋节";
        } else if (lunarMonth == 9 && lunarDay == 9) {
            message = "重阳节";
        } else if (lunarMonth == 12 && lunarDay == 8) {
            message = "腊八节";
        } else if (lunarMonth == 12 && lunarDay == 23) {
            message = "小年";
        } else if (lunarMonth == 12 && lunar.lunarMonth != nextLunar.lunarMonth) {
            message = "除夕";
        }
        return message;
    }

    //法定节假日 休息的日期
    public static List<String> holidayList = Arrays.asList(
            "2017-12-30", "2017-12-31", "2018-01-01", "2018-02-15", "2018-02-16", "2018-02-17", "2018-02-18", "2018-02-19", "2018-02-20", "2018-02-21", "2018-04-05",
            "2018-04-06", "2018-04-07", "2018-04-29", "2018-04-30", "2018-05-01", "2018-06-16", "2018-06-17", "2018-06-18", "2018-09-22", "2018-09-23", "2018-09-24",
            "2018-10-01", "2018-10-02", "2018-10-03", "2018-10-04", "2018-10-05", "2018-10-06", "2018-10-07", "2018-12-30", "2018-12-31", "2019-01-01", "2019-02-04",
            "2019-02-05", "2019-02-06", "2019-02-07", "2019-02-08", "2019-02-09", "2019-02-10", "2019-04-05", "2019-04-06", "2019-04-07", "2019-05-01", "2019-05-02",
            "2019-05-03", "2019-05-04", "2019-06-07", "2019-06-08", "2019-06-09", "2019-09-13", "2019-09-14", "2019-09-15", "2019-10-01", "2019-10-02", "2019-10-03",
            "2019-10-04", "2019-10-05", "2019-10-06", "2019-10-07", "2020-01-01", "2020-01-24", "2020-01-25", "2020-01-26", "2020-01-27", "2020-01-28", "2020-01-29",
            "2020-01-30", "2020-01-31", "2020-02-01", "2020-02-02", "2020-04-04", "2020-04-05", "2020-04-06", "2020-05-01", "2020-05-02", "2020-05-03", "2020-05-04",
            "2020-05-05", "2020-06-25", "2020-06-26", "2020-06-27", "2020-10-01", "2020-10-02", "2020-10-03", "2020-10-04", "2020-10-05", "2020-10-06", "2020-10-07",
            "2020-10-08", "2021-01-01", "2021-01-02", "2021-01-03", "2021-02-11", "2021-02-12", "2021-02-13", "2021-02-14", "2021-02-15", "2021-02-16", "2021-02-17",
            "2021-04-03", "2021-04-04", "2021-04-05", "2021-05-01", "2021-05-02", "2021-05-03", "2021-05-04", "2021-05-05", "2021-06-12", "2021-06-13", "2021-06-14",
            "2021-09-19", "2021-09-20", "2021-09-21", "2021-10-01", "2021-10-02", "2021-10-03", "2021-10-04", "2021-10-05", "2021-10-06", "2021-10-07", "2022-01-01",
            "2022-01-02", "2022-01-03", "2022-01-31", "2022-02-01", "2022-02-02", "2022-02-03", "2022-02-04", "2022-02-05", "2022-02-06", "2022-04-03", "2022-04-04",
            "2022-04-05", "2022-04-30", "2022-05-01", "2022-05-02", "2022-05-03", "2022-05-04", "2022-06-03", "2022-06-04", "2022-06-05", "2022-09-10", "2022-09-11",
            "2022-09-12", "2022-10-01", "2022-10-02", "2022-10-03", "2022-10-04", "2022-10-05", "2022-10-06", "2022-10-07");

    //补班的日期
    public static List<String> workdayList = Arrays.asList(
            "2018-02-11", "2018-02-24", "2018-04-08", "2018-04-28", "2018-09-29", "2018-04-30", "2018-12-29", "2019-02-02", "2019-02-03", "2019-04-28", "2019-05-05",
            "2019-09-29", "2019-10-12", "2020-01-19", "2020-04-26", "2020-05-09", "2020-06-28", "2020-09-27", "2020-10-10", "2021-02-07", "2021-02-20", "2021-04-25",
            "2021-05-08", "2021-09-18", "2021-09-26", "2021-10-09", "2022-01-29", "2022-01-30", "2022-04-02", "2022-04-24", "2022-05-07", "2022-10-08", "2022-10-09");


}
