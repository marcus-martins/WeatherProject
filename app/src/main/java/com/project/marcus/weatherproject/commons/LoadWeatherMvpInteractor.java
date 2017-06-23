package com.project.marcus.weatherproject.commons;

import com.project.marcus.weatherproject.model.WeatherResult;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by marcus on 31/05/17.
 */

public interface LoadWeatherMvpInteractor {

    Single<List<WeatherResult>> getWeather(double latitude, double longitude, String TEMPERATURE_TYPE);

}
