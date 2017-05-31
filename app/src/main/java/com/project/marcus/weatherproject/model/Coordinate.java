package com.project.marcus.weatherproject.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by marcus on 29/05/17.
 */

public class Coordinate {

    @SerializedName("lat")
    private double latitude;
    @SerializedName("lon")
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
