package com.example.zhou.coolweather.gson;

/**
 * Created by zhou on 2017/7/5.
 */

public class AQI {
    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
