package ru.vasic2000.newweather.rest.entities.Weather;

import com.google.gson.annotations.SerializedName;

public class Coord {
    @SerializedName("lat") public Double latitude;
    @SerializedName ("lon") public Double longitude;
}
