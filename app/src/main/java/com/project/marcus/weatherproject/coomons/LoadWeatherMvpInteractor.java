package com.project.marcus.weatherproject.coomons;

import com.project.marcus.weatherproject.model.WeatherResult;

import java.util.List;

/**
 * Created by marcus on 31/05/17.
 */

public interface LoadWeatherMvpInteractor {

    void getWeather(double latitude, double longitude, String TEMPERATURE_TYPE, WeatherCallback callback);

    interface WeatherCallback {

        void returnWeather(List<WeatherResult> weatherResults);

        void returnError();

    }
}
