package com.project.marcus.weatherproject.map_weather;

import com.project.marcus.weatherproject.model.WeatherResult;

import java.util.List;

/**
 * Created by marcus on 29/05/17.
 */

public interface MapsWeatherContract {
    interface MapsMvpView {

        void showWeatherResults(List<WeatherResult> weatherResults);

        void showLoadWeatherResultsError();

    }

    interface MapsMvpPresenter {

        void loadWeatherResults(double latitude, double longitude, String TEMPERATURE_TYPE);

        void viewAndInteractorDestroy();

    }
}
