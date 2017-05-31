package com.project.marcus.weatherproject.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by marcus on 29/05/17.
 */

public class Temperature {
    @SerializedName("temp")
    private float temperature;
    @SerializedName("temp_min")
    private float minTemperature;
    @SerializedName("temp_max")
    private float maxTemperature;

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(float minTemperature) {
        this.minTemperature = minTemperature;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }
}
