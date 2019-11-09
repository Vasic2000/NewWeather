package ru.vasic2000.newweather.rest.entities;

import com.google.gson.annotations.SerializedName;

public class WeatherRestModel {
    @SerializedName ("id") public int index;
    @SerializedName ("main") public String main;
    @SerializedName ("description") public String description;
    @SerializedName ("icon") public String icon;
}
