package ru.vasic2000.newweather.rest.entities.Weather;

import com.google.gson.annotations.SerializedName;

public class SysRestModel {
    @SerializedName("country") public String country;
    @SerializedName("sunrise") public long sunrise;
    @SerializedName("sunset") public long sunset;
}
