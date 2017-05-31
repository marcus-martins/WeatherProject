package com.project.marcus.weatherproject.location_weather;

import com.project.marcus.weatherproject.model.WeatherResult;

import java.util.List;

/**
 * Created by marcus on 29/05/17.
 */

public class LocationWeatherPresenter implements LocationWeatherContract.LocationMvpPresenter,
        LocationWeatherContract.LocationMvpInteractor.WeatherCallback {

    private LocationWeatherContract.LocationMvpView locationMvpView;
    private LocationWeatherContract.LocationMvpInteractor locationMvpInteractor;

    public LocationWeatherPresenter(LocationWeatherContract.LocationMvpView locationMvpView,
                                    LocationWeatherContract.LocationMvpInteractor locationMvpInteractor) {
        this.locationMvpView = locationMvpView;
        this.locationMvpInteractor = locationMvpInteractor;
    }

    @Override
    public void loadWeatherResults(double latitude, double longitude, String TEMPERATURE_TYPE) {
        if (getView() != null && locationMvpInteractor != null) {
            getView().showProgress();
            locationMvpInteractor.getWeather(latitude, longitude, TEMPERATURE_TYPE, this);
        }
    }

    @Override
    public void viewAndInteractorDestroy() {
        if (locationMvpView != null) {
            locationMvpView = null;
        }

        if (locationMvpInteractor != null) {
            locationMvpInteractor = null;
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
