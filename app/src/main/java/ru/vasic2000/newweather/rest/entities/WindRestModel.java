package ru.vasic2000.newweather.rest.entities;

import com.google.gson.annotations.SerializedName;

public class WindRestModel {
    @SerializedName ("speed") public float speed;
    @SerializedName ("deg") public float direction;
}
