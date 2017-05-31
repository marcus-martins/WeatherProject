package com.project.marcus.weatherproject.coomons;

import android.location.Location;

import com.project.marcus.weatherproject.model.WeatherResult;
import com.project.marcus.weatherproject.model.WeatherResults;
import com.project.marcus.weatherproject.network.WeatherClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import static com.project.marcus.weatherproject.network.WeatherService.API_KEY;

/**
 * Created by marcus on 29/05/17.
 */

public class LoadWeatherInteractor implements LoadWeatherMvpInteractor {
    private CompositeDisposable compositeDisposable;
    private WeatherClient weatherClient;

    public LoadWeatherInteractor(CompositeDisposable compositeDisposable, WeatherClient weatherClient) {
        this.compositeDisposable = compositeDisposable;
        this.weatherClient = weatherClient;
    }

    @Override
    public void getWeather(double latitude, double longitude,
                           String TEMPERATURE_TYPE, WeatherCallback callback) {

        if (compositeDisposable != null && weatherClient != null) {

            compositeDisposable.add(

                    weatherClient.
                            searchWeatherByCoordinates(latitude, longitude, TEMPERATURE_TYPE, API_KEY)
                            .concatMapIterable(WeatherResults::getList)
                            .distinct(WeatherResult::getName)
                            .filter(weatherResult -> {
                                weatherResult.setKilometers(
                                        kilometersDistanceBetweenPoints(
                                                latitude,
                                                longitude,
                                                weatherResult.getCoordinate().getLatitude(),
                                                weatherResult.getCoordinate().getLongitude()
                                        )
                                );
                                return true;
                            })
                            .filter(weatherResult -> weatherResult.getKilometers() <= 50)
                            .toList()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    callback::returnWeather,

                                    throwable -> callback.returnError()
                            )

            );

        }

    }

    private double kilometersDistanceBetweenPoints(double latA, double lonA, double latB, double lonB) {

        Location locationA = new Location("point A");
        locationA.setLatitude(latA);
        locationA.setLongitude(lonA);

        Location locationB = new Location("point B");
        locationB.setLatitude(latB);
        locationB.setLongitude(lonB);

        double distance = locationA.distanceTo(locationB);

        if (distance > 0) {
            return distance/1000;
        } else {
            return 0;
        }

    }
}
