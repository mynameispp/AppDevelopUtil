package com.ffzxnet.developutil.utils.tools;

import com.ffzxnet.countrymeet.R;

/**
 * Created by Whisky on 2017/7/14.
 * 根据返回的天气字符匹配对应的图片资源
 */

public class WeatherUtils {

    public static int getResIdToStr(String weather) {
        //雪
        if (weather.contains("雪")) {

            if (weather.contains("中")) {
                return R.mipmap.icon_weather_snow;
            } else if (weather.contains("大")) {
                return R.mipmap.icon_weather_snowstorm;
            } else if (weather.contains("小")) {
                return R.mipmap.icon_weather_light_snow;
            }
            return R.mipmap.icon_weather_snow;
        }
        //雨
        if (weather.contains("雨")) {
            if (weather.contains("中")) {
                return R.mipmap.icon_weather_moderate_rain;
            } else if (weather.contains("大")) {
                return R.mipmap.icon_weather_heavy_rain;
            } else if (weather.contains("小")) {
                return R.mipmap.icon_weather_light_rain;
            }
            return R.mipmap.icon_weather_thunder_rain;
        }
        if (weather.contains("云")) {
            return R.mipmap.icon_weather_cloudy;
        }
        if (weather.contains("阴")) {
            return R.mipmap.icon_weather_overcast;
        }
        if (weather.contains("沙")) {
            return R.mipmap.icon_weather_sand;
        }
        if (weather.contains("风")) {
            return R.mipmap.icon_weather_wind;
        }
        if (weather.contains("雾")) {
            return R.mipmap.icon_weather_foggy;
        }
        return R.mipmap.icon_weather_sunny;
    }
}
