package com.project.marcus.weatherproject.location_weather;

import com.project.marcus.weatherproject.coomons.LoadWeatherMvpInteractor;
import com.project.marcus.weatherproject.model.WeatherResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by marcus on 30/05/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class LocationWeatherPresenterTest {
    @Mock
    LocationWeatherContract.LocationMvpView locationMvpView;
    @Mock
    LoadWeatherMvpInteractor loadWeatherMvpInteractor;

    private LocationWeatherPresenter presenter;
    private double latitude = -133283.0;
    private double longitude = -143283.0;
    private String type = "metric";

    private WeatherResult weather = new WeatherResult();
    private List<WeatherResult> weatherResults = Arrays.asList(weather, weather, weather);

    @Before
    public void setUp() throws Exception {
        presenter = new LocationWeatherPresenter(locationMvpView, loadWeatherMvpInteractor);
    }

    @Test
    public void checkIfShowProgressOnLoadWeatherResults() {
        presenter.loadWeatherResults(latitude, longitude, type);
        verify(locationMvpView, times(1)).showProgress();
    }

    @Test
    public void checkIfGetWeatherOnLoadWeatherResults() {
        presenter.loadWeatherResults(latitude, longitude, type);
        verify(loadWeatherMvpInteractor, times(1)).getWeather(latitude, longitude, type, presenter);
    }

    @Test
    public void checkIfHideProgressOnReturnWeather() {
        presenter.returnWeather(weatherResults);
        verify(locationMvpView, times(1)).hideProgress();
    }

    @Test
    public void checkIfHideProgressOnReturnError() {
        presenter.returnError();
        verify(locationMvpView, times(1)).hideProgress();
    }

    @Test
    public void checkIfShowLoadWeatherResultsErrorOnReturnError() {
        presenter.returnError();
        verify(locationMvpView, times(1)).showLoadWeatherResultsError();
    }

    @Test
    public void checkIfShowWeatherResults() {
        presenter.returnWeather(weatherResults);
        verify(locationMvpView, times(1)).showWeatherResults(weatherResults);
    }

    @Test
    public void checkIfViewAndInteractorIsReleasedOnDestroy() {
        presenter.viewAndInteractorDestroy();
        assertNull(presenter.getView());
    }
}
