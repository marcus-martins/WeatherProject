package com.project.marcus.weatherproject.location_weather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.project.marcus.weatherproject.R;
import com.project.marcus.weatherproject.WeatherApplication;
import com.project.marcus.weatherproject.commons.LoadWeatherInteractor;
import com.project.marcus.weatherproject.commons.LoadWeatherMvpInteractor;
import com.project.marcus.weatherproject.location_weather.adapter.WeatherAdapter;
import com.project.marcus.weatherproject.map_weather.MapsWeatherActivity;
import com.project.marcus.weatherproject.model.WeatherResult;
import com.project.marcus.weatherproject.network.WeatherClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by marcus on 29/05/17.
 */

public class LocationWeatherActivity extends AppCompatActivity
        implements LocationWeatherContract.LocationMvpView, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private String TEMPERATURE_TYPE = "metric";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private LocationWeatherContract.LocationMvpPresenter locationMvpPresenter;
    private WeatherAdapter weatherAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<WeatherResult> weatherResults = new ArrayList<>();

    private GoogleApiClient mGoogleApiClient;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_weather);

        WeatherClient weatherClient =
                ((WeatherApplication) getApplication()).getWeatherClient();

        LoadWeatherMvpInteractor
                loadWeatherMvpInteractor = new LoadWeatherInteractor(weatherClient);

        locationMvpPresenter = new LocationWeatherPresenter(this, loadWeatherMvpInteractor, compositeDisposable);

        setupViews();

        instantiateLocation();

    }

    private void setupViews() {
        progressBar = (ProgressBar) findViewById(R.id.location_progress);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.location_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        weatherAdapter = new WeatherAdapter(weatherResults, this);
        recyclerView.setAdapter(weatherAdapter);

        swipeRefreshLayout =
                (SwipeRefreshLayout) findViewById(R.id.location_swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(() ->
                locationMvpPresenter.loadWeatherResults(latitude, longitude, TEMPERATURE_TYPE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        if (TEMPERATURE_TYPE.equals("metric")) {
            menu.findItem(R.id.celsius).setVisible(false);
        } else {
            menu.findItem(R.id.fahrenheit).setVisible(false);
        }

        menu.findItem(R.id.list).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.celsius:
                TEMPERATURE_TYPE = "metric";
                locationMvpPresenter.loadWeatherResults(latitude, longitude, TEMPERATURE_TYPE);
                invalidateOptionsMenu();
                return true;
            case R.id.fahrenheit:
                TEMPERATURE_TYPE = "imperial";
                locationMvpPresenter.loadWeatherResults(latitude, longitude, TEMPERATURE_TYPE);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void instantiateLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            return;
        }

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (lastLocation != null) {
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();

            locationMvpPresenter.loadWeatherResults(latitude, longitude, TEMPERATURE_TYPE);
        } else {
            showLocationError(getString(R.string.gps_problem_message));
        }
    }

    private void showLocationError(String message) {
        Snackbar
                .make(findViewById(R.id.location_swipe_refresh), message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry_text, v -> onStart()).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                onStart();
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        showLocationError(getString(R.string.gps_problem_message));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showLocationError(getString(R.string.gps_problem_message));
    }


    @Override
    public void showWeatherResults(List<WeatherResult> weatherResults) {
        swipeRefreshLayout.setRefreshing(false);

        if (weatherResults != null && weatherResults.size() > 0) {
            hideProgress();
            this.weatherResults.clear();
            this.weatherResults.addAll(weatherResults);
            weatherAdapter.notifyDataSetChanged();
        } else {
            showLoadWeatherResultsError();
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoadWeatherResultsError() {
        Snackbar
                .make(findViewById(R.id.location_swipe_refresh),
                        getString(R.string.weather_results_error), Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(compositeDisposable != null) {
            compositeDisposable.clear();
        }

        if (locationMvpPresenter != null) {
            locationMvpPresenter.viewAndInteractorDestroy();
        }
    }
}
