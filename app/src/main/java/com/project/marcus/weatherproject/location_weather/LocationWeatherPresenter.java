package com.project.marcus.weatherproject.location_weather;

import com.project.marcus.weatherproject.coomons.LoadWeatherMvpInteractor;
import com.project.marcus.weatherproject.model.WeatherResult;

import java.util.List;

/**
 * Created by marcus on 29/05/17.
 */

public class LocationWeatherPresenter implements LocationWeatherContract.LocationMvpPresenter,
        LoadWeatherMvpInteractor.WeatherCallback {

    private LocationWeatherContract.LocationMvpView locationMvpView;
    private LoadWeatherMvpInteractor loadWeatherMvpInteractor;

    public LocationWeatherPresenter(LocationWeatherContract.LocationMvpView locationMvpView,
                                    LoadWeatherMvpInteractor loadWeatherMvpInteractor) {
        this.locationMvpView = locationMvpView;
        this.loadWeatherMvpInteractor = loadWeatherMvpInteractor;
    }

    @Override
    public void loadWeatherResults(double latitude, double longitude, String TEMPERATURE_TYPE) {
        if (getView() != null && loadWeatherMvpInteractor != null) {
            getView().showProgress();
            loadWeatherMvpInteractor.getWeather(latitude, longitude, TEMPERATURE_TYPE, this);
        }
    }

    @Override
    public void viewAndInteractorDestroy() {
        if (locationMvpView != null) {
            locationMvpView = null;
        }

        if (loadWeatherMvpInteractor != null) {
            loadWeatherMvpInteractor = null;
        }
    }

    @Override
    public void returnWeather(List<WeatherResult> weatherResults) {
        if (getView() != null) {
            getView().hideProgress();
            getView().showWeatherResults(weatherResults);
        }
    }

    @Override
    public void returnError() {
        if (getView() != null) {
            getView().hideProgress();
            getView().showLoadWeatherResultsError();
        }
    }

    public LocationWeatherContract.LocationMvpView getView() {
        return locationMvpView;
    }
}
