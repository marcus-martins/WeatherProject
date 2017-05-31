package com.project.marcus.weatherproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by marcus on 29/05/17.
 */

public class WeatherResult {
    private String name;
    private List<Weather> weather;
    @SerializedName("main")
    private Temperature temperature;
    @SerializedName("coord")
    private Coordinate coordinate;
    private double kilometers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public double getKilometers() {
        return kilometers;
    }

    public void setKilometers(double kilometers) {
        this.kilometers = kilometers;
    }
}
