package com.project.marcus.weatherproject.map_weather;

import com.project.marcus.weatherproject.location_weather.LocationWeatherContract;
import com.project.marcus.weatherproject.model.WeatherResult;

import java.util.List;

/**
 * Created by marcus on 29/05/17.
 */

public class MapsWeatherPresenter implements MapsWeatherContract.MapsMvpPresenter,
        LocationWeatherContract.LocationMvpInteractor.WeatherCallback {

    private MapsWeatherContract.MapsMvpView mapsMvpView;
    private LocationWeatherContract.LocationMvpInteractor locationMvpInteractor;

    public MapsWeatherPresenter(MapsWeatherContract.MapsMvpView mapsMvpView,
                                LocationWeatherContract.LocationMvpInteractor locationMvpInteractor) {
        this.mapsMvpView = mapsMvpView;
        this.locationMvpInteractor = locationMvpInteractor;
    }

    @Override
    public void loadWeatherResults(double latitude, double longitude, String TEMPERATURE_TYPE) {
        if (getView() != null && locationMvpInteractor != null) {
            locationMvpInteractor.getWeather(latitude, longitude, TEMPERATURE_TYPE, this);
        }
    }

    @Override
    public void viewAndInteractorDestroy() {
        if (mapsMvpView != null) {
            mapsMvpView = null;
        }

        if (locationMvpInteractor != null) {
            locationMvpInteractor = null;
        }
    }

    @Override
    public void returnWeather(List<WeatherResult> weatherResults) {
        if (getView() != null) {
            getView().showWeatherResults(weatherResults);
        }
    }

    @Override
    public void returnError() {
        if (getView() != null) {
            getView().showLoadWeatherResultsError();
        }
    }

    public MapsWeatherContract.MapsMvpView getView() {
        return mapsMvpView;
    }
}