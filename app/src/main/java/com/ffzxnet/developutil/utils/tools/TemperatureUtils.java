package com.ffzxnet.developutil.utils.tools;

/**
 * Created by Whisky on 2017/7/14.
 */

public class TemperatureUtils {

    public static String getTemp(String lowTemp, String highTemp) {
        int temp = 0;
        try{
            String lowTempString = lowTemp.split(" ")[1].substring(0, 2);
            int lowTempNum = StringUtil.stringToInt(lowTempString);

            String highTempString = lowTemp.split(" ")[1].substring(0, 2);
            int highTempNum = StringUtil.stringToInt(highTempString);

            temp = (lowTempNum + highTempNum) / 2;
        }catch(Exception e){
            temp = 25;
        }

        return String.valueOf(temp) + "°";
    }
}
