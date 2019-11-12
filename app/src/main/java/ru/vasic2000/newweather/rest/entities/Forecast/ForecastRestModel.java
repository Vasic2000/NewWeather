package ru.vasic2000.newweather.rest.entities.Forecast;

import com.google.gson.annotations.SerializedName;

public class ForecastRestModel {
    @SerializedName("main") public ForecastMainRestModel main;
    @SerializedName("weather") public ForecastWeather[] weather;
    @SerializedName("dt_txt") public String time;
}
