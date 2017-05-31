package com.project.marcus.weatherproject;

import android.app.Application;

import com.project.marcus.weatherproject.network.WeatherClient;
import com.project.marcus.weatherproject.network.WeatherService;

/**
 * Created by marcus on 29/05/17.
 */

public class WeatherApplication extends Application {
    private static WeatherClient weatherClient;

    @Override
    public void onCreate() {
        super.onCreate();

        weatherClient = WeatherService
                .createService(WeatherClient.class, getResources().getString(R.string.open_weather_key));
    }

    public WeatherClient getWeatherClient() {
        return weatherClient;
    }
}
