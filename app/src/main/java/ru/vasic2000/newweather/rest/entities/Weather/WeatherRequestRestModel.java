package ru.vasic2000.newweather.rest.entities.Weather;

import com.google.gson.annotations.SerializedName;

public class WeatherRequestRestModel {
    @SerializedName("weather") public WeatherRestModel[] weathers;
    @SerializedName("main") public WeatherMainRestModel main;
    @SerializedName("sys") public SysRestModel sys;
    @SerializedName("name") public String cityName;
}
