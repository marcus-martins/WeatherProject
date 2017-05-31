package com.project.marcus.weatherproject.network;

import com.project.marcus.weatherproject.model.WeatherResults;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by marcus on 29/05/17.
 */

public interface WeatherClient {

    @GET("find?&cnt=50&lang=pt")
    Observable<WeatherResults> searchWeatherByCoordinates(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("units") String units,
            @Query("appid") String key
    );
}
