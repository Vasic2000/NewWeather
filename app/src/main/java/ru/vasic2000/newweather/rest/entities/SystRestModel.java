package ru.vasic2000.newweather.rest.entities;

import com.google.gson.annotations.SerializedName;

public class SystRestModel {
    @SerializedName("type") public int type;
    @SerializedName("id") public int index;
    @SerializedName("country") public String country;
    @SerializedName("sunrise") public long sunrise;
    @SerializedName("sunset") public long sunset;
}
