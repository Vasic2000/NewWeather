package ru.vasic2000.newweather.rest.entities;

import com.google.gson.annotations.SerializedName;

public class WeatherRequestRestModel {
    @SerializedName("coord") public CoordRestModel coordinates;
    @SerializedName("weather") public WeatherRestModel[] weathers;
    @SerializedName("base") public String stations;
    @SerializedName("main") public MainRestModel main;
    @SerializedName("visibility") public int visibility;
    @SerializedName("wind") public WindRestModel wind;
    @SerializedName("clouds") public CloudsRestModel clouds;
    @SerializedName("dt") public long dt;
    @SerializedName("sys") public SystRestModel sys;
    @SerializedName("timezone") public int timezone;
    @SerializedName("id") public int index;
    @SerializedName("name") public String cityName;
    @SerializedName("cod") public int cod;
}
