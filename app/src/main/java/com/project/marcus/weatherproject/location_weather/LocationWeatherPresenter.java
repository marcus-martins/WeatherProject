package com.project.marcus.weatherproject.location_weather;

import com.project.marcus.weatherproject.commons.LoadWeatherMvpInteractor;
import com.project.marcus.weatherproject.model.WeatherResult;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by marcus on 29/05/17.
 */

public class LocationWeatherPresenter implements LocationWeatherContract.LocationMvpPresenter {

    private LocationWeatherContract.LocationMvpView locationMvpView;
    private LoadWeatherMvpInteractor loadWeatherMvpInteractor;
    private CompositeDisposable compositeDisposable;

    public LocationWeatherPresenter(LocationWeatherContract.LocationMvpView locationMvpView,
                                    LoadWeatherMvpInteractor loadWeatherMvpInteractor,
                                    CompositeDisposable compositeDisposable) {
        this.locationMvpView = locationMvpView;
        this.loadWeatherMvpInteractor = loadWeatherMvpInteractor;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void loadWeatherResults(double latitude, double longitude, String TEMPERATURE_TYPE) {
        if (getView() != null && loadWeatherMvpInteractor != null) {
            getView().showProgress();

            compositeDisposable.add(
                    loadWeatherMvpInteractor
                            .getWeather(latitude, longitude, TEMPERATURE_TYPE)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    weatherResults -> {
                                        getView().hideProgress();
                                        getView().showWeatherResults(weatherResults);
                                    },

                                    throwable -> {
                                        getView().hideProgress();
                                        getView().showLoadWeatherResultsError();
                                    }
                            )
            );

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

    public LocationWeatherContract.LocationMvpView getView() {
        return locationMvpView;
    }
}
