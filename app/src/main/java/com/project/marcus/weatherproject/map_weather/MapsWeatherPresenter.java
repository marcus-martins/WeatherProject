package com.project.marcus.weatherproject.map_weather;

import com.project.marcus.weatherproject.commons.LoadWeatherMvpInteractor;
import com.project.marcus.weatherproject.model.WeatherResult;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by marcus on 29/05/17.
 */

public class MapsWeatherPresenter implements MapsWeatherContract.MapsMvpPresenter {

    private MapsWeatherContract.MapsMvpView mapsMvpView;
    private LoadWeatherMvpInteractor loadWeatherMvpInteractor;
    private CompositeDisposable compositeDisposable;

    public MapsWeatherPresenter(MapsWeatherContract.MapsMvpView mapsMvpView,
                                LoadWeatherMvpInteractor loadWeatherMvpInteractor,
                                CompositeDisposable compositeDisposable) {
        this.mapsMvpView = mapsMvpView;
        this.loadWeatherMvpInteractor = loadWeatherMvpInteractor;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void loadWeatherResults(double latitude, double longitude, String TEMPERATURE_TYPE) {
        if (getView() != null && loadWeatherMvpInteractor != null) {

            compositeDisposable.add(
                    loadWeatherMvpInteractor
                            .getWeather(latitude, longitude, TEMPERATURE_TYPE)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    weatherResults -> getView().showWeatherResults(weatherResults),

                                    throwable -> getView().showLoadWeatherResultsError()
                            )
            );

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

    public MapsWeatherContract.MapsMvpView getView() {
        return mapsMvpView;
    }
}