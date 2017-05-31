package com.project.marcus.weatherproject.map_weather;

import com.project.marcus.weatherproject.coomons.LoadWeatherMvpInteractor;
import com.project.marcus.weatherproject.model.WeatherResult;

import java.util.List;

/**
 * Created by marcus on 29/05/17.
 */

public class MapsWeatherPresenter implements MapsWeatherContract.MapsMvpPresenter,
        LoadWeatherMvpInteractor.WeatherCallback {

    private MapsWeatherContract.MapsMvpView mapsMvpView;
    private LoadWeatherMvpInteractor loadWeatherMvpInteractor;

    public MapsWeatherPresenter(MapsWeatherContract.MapsMvpView mapsMvpView,
                                LoadWeatherMvpInteractor loadWeatherMvpInteractor) {
        this.mapsMvpView = mapsMvpView;
        this.loadWeatherMvpInteractor = loadWeatherMvpInteractor;
    }

    @Override
    public void loadWeatherResults(double latitude, double longitude, String TEMPERATURE_TYPE) {
        if (getView() != null && loadWeatherMvpInteractor != null) {
            loadWeatherMvpInteractor.getWeather(latitude, longitude, TEMPERATURE_TYPE, this);
        }
    }

    @Override
    public void viewAndInteractorDestroy() {
        if (mapsMvpView != null) {
            mapsMvpView = null;
        }

        if (loadWeatherMvpInteractor != null) {
            loadWeatherMvpInteractor = null;
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