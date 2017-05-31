package com.project.marcus.weatherproject.network;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by marcus on 29/05/17.
 */

public class WeatherService {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static String API_KEY = "16dbe79a20fcc291aa563b8bd54ef371";

    private static RxJava2CallAdapterFactory rxAdapter = RxJava2CallAdapterFactory
            .createWithScheduler(Schedulers.io());

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(rxAdapter);

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass, String open_weather_key) {
        API_KEY = open_weather_key;
        return retrofit.create(serviceClass);
    }

}
