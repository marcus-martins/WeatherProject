package com.project.marcus.weatherproject.location_weather;

import com.project.marcus.weatherproject.model.WeatherResult;

import java.util.List;

/**
 * Created by marcus on 29/05/17.
 */

public interface LocationWeatherContract {

    interface LocationMvpView {

        void showWeatherResults(List<WeatherResult> weatherResults);

        void showProgress();

        void hideProgress();

        void showLoadWeatherResultsError();

    }

    interface LocationMvpPresenter {

        void loadWeatherResults(double latitude, double longitude, String TEMPERATURE_TYPE);

        void viewAndInteractorDestroy();

    }

    interface LocationMvpInteractor {

        void getWeather(double latitude, double longitude, String TEMPERATURE_TYPE, WeatherCallback callback);

        interface WeatherCallback {

            void returnWeather(List<WeatherResult> weatherResults);

            void returnError();

        }
    }
}
