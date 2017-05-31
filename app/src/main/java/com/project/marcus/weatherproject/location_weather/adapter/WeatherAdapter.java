package com.project.marcus.weatherproject.location_weather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.marcus.weatherproject.R;
import com.project.marcus.weatherproject.model.WeatherResult;

import java.util.List;

/**
 * Created by marcus on 25/05/17.
 */

public class WeatherAdapter extends RecyclerView.Adapter<CardHolder> {
    private List<WeatherResult> weatherResultList;
    private Context context;

    public WeatherAdapter(List<WeatherResult> weatherResultList, Context context) {
        this.weatherResultList = weatherResultList;
        this.context = context;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_weather, parent, false));
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        WeatherResult item = weatherResultList.get(position);

        Glide.with(context)
                .load(item.getWeather().get(0).getIcon())
                .centerCrop()
                .into(holder.weatherImage);

        holder.cityName.setText(item.getName());
        holder.temperature.setText(String.format(context.getResources()
                .getString(R.string.temperature), item.getTemperature().getTemperature()));
        holder.minTemperature.setText(String.format(context.getResources()
                .getString(R.string.min_temperature), item.getTemperature().getMinTemperature()));
        holder.maxTemperature.setText(String.format(context.getResources()
                .getString(R.string.max_temperature), item.getTemperature().getMaxTemperature()));
        holder.description.setText(item.getWeather().get(0).getDescription());
        holder.kilometers.setText(String.format(context.getResources()
                .getString(R.string.kilometers), item.getKilometers()));
    }

    @Override
    public int getItemCount() {
        return weatherResultList != null ? weatherResultList.size() : 0;
    }
}