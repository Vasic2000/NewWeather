package ru.vasic2000.newweather.rest.entities;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.vasic2000.newweather.rest.entities.Forecast.ForecastRequestRestModel;
import ru.vasic2000.newweather.rest.entities.Weather.WeatherRequestRestModel;

public interface IOpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherRequestRestModel> loadWeather(@Query("q") String city,
                                              @Query("appid") String keyApi,
                                              @Query("lang") String lang,
                                              @Query("units") String units);
    @GET("data/2.5/forecast")
    Call<ForecastRequestRestModel> loadForecast(@Query("q") String city,
                                                @Query("appid") String keyApi,
                                                @Query("units") String units);

    @GET("data/2.5/weather")
    Call<WeatherRequestRestModel> loadWeatherCoord(@Query("lat") double lat,
                                                   @Query("lon") double lon,
                                                   @Query("appid") String keyApi,
                                                   @Query("lang") String lang,
                                                   @Query("units") String units);
    @GET("data/2.5/forecast")
    Call<ForecastRequestRestModel> loadForecastCoord(@Query("lat") double lat,
                                                     @Query("lon") double lon,
                                                     @Query("appid") String keyApi,
                                                     @Query("units") String units);





}
