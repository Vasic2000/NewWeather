package ru.vasic2000.newweather.rest.entities.Weather;

import com.google.gson.annotations.SerializedName;

public class WeatherRestModel {
    @SerializedName ("id") public int index;
    @SerializedName ("description") public String description;
}
