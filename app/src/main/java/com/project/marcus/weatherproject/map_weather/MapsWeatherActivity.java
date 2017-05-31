package com.project.marcus.weatherproject.map_weather;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.marcus.weatherproject.R;
import com.project.marcus.weatherproject.WeatherApplication;
import com.project.marcus.weatherproject.coomons.LoadWeatherInteractor;
import com.project.marcus.weatherproject.coomons.LoadWeatherMvpInteractor;
import com.project.marcus.weatherproject.model.WeatherResult;
import com.project.marcus.weatherproject.network.WeatherClient;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class MapsWeatherActivity extends AppCompatActivity implements MapsWeatherContract.MapsMvpView,
        OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener {

    private static String TEMPERATURE_TYPE = "metric";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MapsWeatherContract.MapsMvpPresenter mapsMvpPresenter;

    private GoogleMap mMap;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        latitude = getIntent().getExtras().getDouble("latitude");
        longitude = getIntent().getExtras().getDouble("longitude");

        WeatherClient weatherClient =
                ((WeatherApplication) getApplication()).getWeatherClient();

        LoadWeatherMvpInteractor
                loadWeatherMvpInteractor = new LoadWeatherInteractor(compositeDisposable, weatherClient);

        mapsMvpPresenter = new MapsWeatherPresenter(this, loadWeatherMvpInteractor);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        if (TEMPERATURE_TYPE.equals("metric")) {
            menu.findItem(R.id.celsius).setVisible(false);
        } else {
            menu.findItem(R.id.fahrenheit).setVisible(false);
        }

        menu.findItem(R.id.map).setVisible(false);
        menu.findItem(R.id.list).setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.celsius:
                mMap.clear();
                TEMPERATURE_TYPE = "metric";
                mapsMvpPresenter.loadWeatherResults(latitude, longitude, TEMPERATURE_TYPE);
                invalidateOptionsMenu();
                return true;
            case R.id.fahrenheit:
                mMap.clear();
                TEMPERATURE_TYPE = "imperial";
                mapsMvpPresenter.loadWeatherResults(latitude, longitude, TEMPERATURE_TYPE);
                invalidateOptionsMenu();
                return true;
            case R.id.map:
                Intent intent = new Intent(this, MapsWeatherActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("latitude", latitude);
                bundle.putDouble("longitude", longitude);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.list:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(9);

        LatLng coord = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coord));

        mapsMvpPresenter.loadWeatherResults(latitude, longitude, TEMPERATURE_TYPE);

        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraIdleListener(this);
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            mMap.clear();
        }
    }

    @Override
    public void onCameraIdle() {
        LatLng coord = mMap.getCameraPosition().target;
        mapsMvpPresenter.loadWeatherResults(coord.latitude, coord.longitude, TEMPERATURE_TYPE);
    }

    @Override
    public void showWeatherResults(List<WeatherResult> weatherResults) {

        if (weatherResults != null && weatherResults.size() > 0) {

            for (WeatherResult item : weatherResults) {

                LatLng coord = new LatLng(item.getCoordinate().getLatitude(),
                        item.getCoordinate().getLongitude());

                mMap.addMarker(new MarkerOptions().position(coord)
                        .title(String.format(getResources().getString(R.string.map_temperature),
                                item.getName(),
                                item.getTemperature().getTemperature())));
            }

        } else {

            showLoadWeatherResultsError();

        }
    }

    @Override
    public void showLoadWeatherResultsError() {
        Snackbar
                .make(findViewById(R.id.map),
                        getString(R.string.weather_results_error), Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(compositeDisposable != null) {
            compositeDisposable.clear();
        }

        if (mapsMvpPresenter != null) {
            mapsMvpPresenter.viewAndInteractorDestroy();
        }
    }
}
