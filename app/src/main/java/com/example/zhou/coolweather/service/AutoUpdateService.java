package com.example.zhou.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;

import com.example.zhou.coolweather.gson.Weather;
import com.example.zhou.coolweather.util.HttpUtil;
import com.example.zhou.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {

    public AutoUpdateService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        updateWeather();
        updateBing();
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int hour = 4 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + hour;
        Intent intent1 = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0 , intent1, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather(){
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherData = pre.getString("weather", null);
        if (weatherData != null){
           Weather weather = Utility.handleWeatherResponse(weatherData);
            String weatherId = weather.basic.weatherId;
            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId +
                    "&key=04719199c005415bbc02fbb4c872d533";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String data = response.body().string();
                    Weather weather1 = Utility.handleWeatherResponse(data);
                    if (weather1 != null &  "ok".equals(weather1.status)){
                        SharedPreferences.Editor editor = PreferenceManager.
                                getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather", data);
                        editor.apply();
                    }
                }
            });
        }
    }

    private void updateBing(){
        String requestBing = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBing, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String stringpic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("picture", stringpic);
                editor.apply();
            }
        });
    }
}
