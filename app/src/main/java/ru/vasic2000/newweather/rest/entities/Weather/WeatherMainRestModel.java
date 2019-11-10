package ru.vasic2000.newweather.rest.entities.Weather;

import com.google.gson.annotations.SerializedName;

public class WeatherMainRestModel {
    @SerializedName("temp") public float temperature;
    @SerializedName("pressure") public float pressure;
    @SerializedName("humidity") public float humidity;
}
