package ru.vasic2000.newweather.rest.entities;

import com.google.gson.annotations.SerializedName;

public class CoordRestModel {
    @SerializedName("lon") public float longitude;
    @SerializedName("lat") public float latitude;
}
