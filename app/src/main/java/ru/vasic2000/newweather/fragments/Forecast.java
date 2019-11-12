package ru.vasic2000.newweather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.vasic2000.newweather.CityPreference;
import ru.vasic2000.newweather.R;
import ru.vasic2000.newweather.activities.MainActivity;
import ru.vasic2000.newweather.rest.entities.Forecast.ForecastRequestRestModel;
import ru.vasic2000.newweather.rest.entities.OpenWeatherRepo;

public class Forecast extends Fragment {

    private TextView tvGoBack;
    private ProgressBar loadIndicator;
    private TextView tvCity;

    private TextView tvIconNight1;
    private TextView tvTemperatureDay1;
    private TextView tvIconDay1;
    private TextView tvTemperatureNight1;
    private TextView tvDate1;

    private TextView tvIconNight2;
    private TextView tvTemperatureDay2;
    private TextView tvIconDay2;
    private TextView tvTemperatureNight2;
    private TextView tvDate2;

    private TextView tvIconNight3;
    private TextView tvTemperatureDay3;
    private TextView tvIconDay3;
    private TextView tvTemperatureNight3;
    private TextView tvDate3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View forecast = getView();

        assert forecast != null;
        tvCity = forecast.findViewById(R.id.city_forecast);
        tvGoBack = forecast.findViewById(R.id.tv_return);
        loadIndicator = forecast.findViewById(R.id.pb_loading_indicator);

        tvIconNight1 = forecast.findViewById(R.id.weather_icon_1night);
        tvIconNight2 = forecast.findViewById(R.id.weather_icon_2night);
        tvIconNight3 = forecast.findViewById(R.id.weather_icon_3night);

        tvIconDay1 = forecast.findViewById(R.id.weather_icon_1day);
        tvIconDay2 = forecast.findViewById(R.id.weather_icon_2day);
        tvIconDay3 = forecast.findViewById(R.id.weather_icon_3day);

        tvTemperatureNight1 = forecast.findViewById(R.id.temperature_1night);
        tvTemperatureNight2 = forecast.findViewById(R.id.temperature_2night);
        tvTemperatureNight3 = forecast.findViewById(R.id.temperature_3night);

        tvTemperatureDay1 = forecast.findViewById(R.id.temperature_1day);
        tvTemperatureDay2 = forecast.findViewById(R.id.temperature_2day);
        tvTemperatureDay3 = forecast.findViewById(R.id.temperature_3day);

        tvDate1 = forecast.findViewById(R.id.date_1);
        tvDate2 = forecast.findViewById(R.id.date_2);
        tvDate3 = forecast.findViewById(R.id.date_3);

        final MainActivity weatherActivity = (MainActivity) getActivity();
        assert weatherActivity != null;
        CityPreference ct = new CityPreference(weatherActivity);
        updateForecastData(ct.getCity(), ct.getSecretKey());

        tvGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                assert activity != null;
                activity.fragmentBack();
            }
        });
    }

    private void renderForecast(ForecastRequestRestModel model) {
        ArrayList<Float> temperatureD = new ArrayList<>();
        ArrayList<Float> temperatureN = new ArrayList<>();
        ArrayList<Integer> iconsIdD = new ArrayList<>();
        ArrayList<Integer> iconsIdN = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();

        //Вспомогательный
        String tempTime;
        String tempDate;

        tvCity.setText(model.cityRestModel.cityName);

//      Ищу данные для 3 часов дня и 3 часов ночи
        for(int i = 0; i < model.forecasts.length; i++) {
            tempTime = model.forecasts[i].time;
            tempTime = tempTime.substring(tempTime.length() - 8, tempTime.length() - 6);

            if(tempTime.equals("15") && (iconsIdN.size() != 0)) {
                temperatureD.add(model.forecasts[i].main.temperature);
                iconsIdD.add(model.forecasts[i].weather[0].index);
            }

            if(tempTime.equals("03")) {
                temperatureN.add(model.forecasts[i].main.temperature);
                iconsIdN.add(model.forecasts[i].weather[0].index);
                tempDate = model.forecasts[i].time;
                tempDate = tempDate.substring(0, 10);
                date.add(tempDate);
            }
        }

        tvTemperatureNight1.setText(String.format("%.2f", temperatureN.get(0)) + "°C");
        tvTemperatureNight2.setText(String.format("%.2f", temperatureN.get(1)) + "°C");
        tvTemperatureNight3.setText(String.format("%.2f", temperatureN.get(2)) + "°C");

        tvTemperatureDay1.setText(String.format("%.2f", temperatureD.get(0)) + "°C");
        tvTemperatureDay2.setText(String.format("%.2f", temperatureD.get(1)) + "°C");
        tvTemperatureDay3.setText(String.format("%.2f", temperatureD.get(2)) + "°C");

        tvDate1.setText(date.get(0));
        tvDate2.setText(date.get(1));
        tvDate3.setText(date.get(2));

        setWeatherIcon(iconsIdN.get(0), tvIconNight1, false);
        setWeatherIcon(iconsIdN.get(1), tvIconNight2, false);
        setWeatherIcon(iconsIdN.get(2), tvIconNight3, false);
        setWeatherIcon(iconsIdD.get(0), tvIconDay1, true);
        setWeatherIcon(iconsIdD.get(1), tvIconDay2, true);
        setWeatherIcon(iconsIdD.get(2), tvIconDay3, true);

        forecastLoaderOff();
    }


    public void updateForecastData(String city, String secretKey) {

        forecastLoaderOn();

        OpenWeatherRepo.getSingleton().getAPI().loadForecast(city, secretKey,
                "metric").enqueue(new Callback<ForecastRequestRestModel>() {
            @Override
            public void onResponse(Call<ForecastRequestRestModel> call,
                                   Response<ForecastRequestRestModel> response) {
                if(response.body() != null && response.isSuccessful()) {
                    renderForecast(response.body());
                } else {
                    Toast.makeText(getContext(), getString(R.string.wrong_answer), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ForecastRequestRestModel> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.netork_failure), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void forecastLoaderOn() {
        loadIndicator.setVisibility(View.VISIBLE);

        tvCity.setVisibility(View.INVISIBLE);
        tvGoBack.setVisibility(View.INVISIBLE);

        tvIconNight1.setVisibility(View.INVISIBLE);
        tvIconNight2.setVisibility(View.INVISIBLE);
        tvIconNight3.setVisibility(View.INVISIBLE);

        tvIconDay1.setVisibility(View.INVISIBLE);
        tvIconDay2.setVisibility(View.INVISIBLE);
        tvIconDay3.setVisibility(View.INVISIBLE);

        tvTemperatureNight1.setVisibility(View.INVISIBLE);
        tvTemperatureNight2.setVisibility(View.INVISIBLE);
        tvTemperatureNight3.setVisibility(View.INVISIBLE);

        tvTemperatureDay1.setVisibility(View.INVISIBLE);
        tvTemperatureDay2.setVisibility(View.INVISIBLE);
        tvTemperatureDay3.setVisibility(View.INVISIBLE);

        tvDate1.setVisibility(View.INVISIBLE);
        tvDate2.setVisibility(View.INVISIBLE);
        tvDate3.setVisibility(View.INVISIBLE);
    }


    private void forecastLoaderOff() {
        tvCity.setVisibility(View.VISIBLE);
        tvGoBack.setVisibility(View.VISIBLE);

        tvIconNight1.setVisibility(View.VISIBLE);
        tvIconNight2.setVisibility(View.VISIBLE);
        tvIconNight3.setVisibility(View.VISIBLE);

        tvIconDay1.setVisibility(View.VISIBLE);
        tvIconDay2.setVisibility(View.VISIBLE);
        tvIconDay3.setVisibility(View.VISIBLE);

        tvTemperatureNight1.setVisibility(View.VISIBLE);
        tvTemperatureNight2.setVisibility(View.VISIBLE);
        tvTemperatureNight3.setVisibility(View.VISIBLE);

        tvTemperatureDay1.setVisibility(View.VISIBLE);
        tvTemperatureDay2.setVisibility(View.VISIBLE);
        tvTemperatureDay3.setVisibility(View.VISIBLE);

        tvDate1.setVisibility(View.VISIBLE);
        tvDate2.setVisibility(View.VISIBLE);
        tvDate3.setVisibility(View.VISIBLE);

        loadIndicator.setVisibility(View.INVISIBLE);
    }

    private void setWeatherIcon(int actualId, TextView weatherIcon, boolean isDayTime) {
        int id = actualId / 100;
        String icon = "";

        if (actualId == 800) {
            if(isDayTime)
                icon = getActivity().getString(R.string.weather_sunny);
            else
                icon = getActivity().getString(R.string.weather_clear_night);
        } else {
            switch (id) {
                case 2:
                    icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = getActivity().getString(R.string.weather_drizzly);
                    break;
                case 5:
                    icon = getActivity().getString(R.string.weather_rainy);
                    break;
                case 6:
                    icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 7:
                    icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                default:
                    break;
            }
        }
        weatherIcon.setText(icon);
    }
}
