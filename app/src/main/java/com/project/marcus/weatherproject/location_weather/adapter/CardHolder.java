package com.project.marcus.weatherproject.location_weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.marcus.weatherproject.R;

/**
 * Created by marcus on 29/05/17.
 */

public class CardHolder extends RecyclerView.ViewHolder {

    public TextView cityName;
    public TextView temperature;
    public TextView minTemperature;
    public TextView maxTemperature;
    public TextView description;
    public TextView kilometers;
    public ImageView weatherImage;

    public CardHolder(View itemView) {
        super(itemView);
        cityName = (TextView) itemView.findViewById(R.id.city_name);
        temperature = (TextView) itemView.findViewById(R.id.temperature);
        minTemperature = (TextView) itemView.findViewById(R.id.min_temperature);
        maxTemperature = (TextView) itemView.findViewById(R.id.max_temperature);
        description = (TextView) itemView.findViewById(R.id.description);
        kilometers = (TextView) itemView.findViewById(R.id.kilometers);
        weatherImage = (ImageView) itemView.findViewById(R.id.weather_image);
    }
}
