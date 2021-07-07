package ru.vasic2000.newweather.rest.entities.Forecast;

import com.google.gson.annotations.SerializedName;

public class ForecastRequestRestModel {
    @SerializedName("list") public ForecastRestModel[] forecasts;
    @SerializedName("city") public CityRestModel cityRestModel;
}
