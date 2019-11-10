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

    private TextView tv_goBack;
    private ProgressBar loadIndicator;
    private TextView tv_city;

    private TextView tv_IconNight1;
    private TextView tv_temperatureDay1;
    private TextView tv_IconDay1;
    private TextView tv_temperatureNight1;
    private TextView tv_date1;

    private TextView tv_IconNight2;
    private TextView tv_temperatureDay2;
    private TextView tv_IconDay2;
    private TextView tv_temperatureNight2;
    private TextView tv_date2;

    private TextView tv_IconNight3;
    private TextView tv_temperatureDay3;
    private TextView tv_IconDay3;
    private TextView tv_temperatureNight3;
    private TextView tv_date3;

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
        tv_city = forecast.findViewById(R.id.city_forecast);
        tv_goBack = forecast.findViewById(R.id.tv_return);
        loadIndicator = forecast.findViewById(R.id.pb_loading_indicator);

        tv_IconNight1 = forecast.findViewById(R.id.weather_icon_1night);
        tv_IconNight2 = forecast.findViewById(R.id.weather_icon_2night);
        tv_IconNight3 = forecast.findViewById(R.id.weather_icon_3night);

        tv_IconDay1 = forecast.findViewById(R.id.weather_icon_1day);
        tv_IconDay2 = forecast.findViewById(R.id.weather_icon_2day);
        tv_IconDay3 = forecast.findViewById(R.id.weather_icon_3day);

        tv_temperatureNight1 = forecast.findViewById(R.id.temperature_1night);
        tv_temperatureNight2 = forecast.findViewById(R.id.temperature_2night);
        tv_temperatureNight3 = forecast.findViewById(R.id.temperature_3night);

        tv_temperatureDay1 = forecast.findViewById(R.id.temperature_1day);
        tv_temperatureDay2 = forecast.findViewById(R.id.temperature_2day);
        tv_temperatureDay3 = forecast.findViewById(R.id.temperature_3day);

        tv_date1 = forecast.findViewById(R.id.date_1);
        tv_date2 = forecast.findViewById(R.id.date_2);
        tv_date3 = forecast.findViewById(R.id.date_3);

        final MainActivity weatherActivity = (MainActivity) getActivity();
        assert weatherActivity != null;
        CityPreference ct = new CityPreference(weatherActivity);
        updateForecastData(ct.getCity(), ct.getSecretKey());

        tv_goBack.setOnClickListener(new View.OnClickListener() {
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

        loadIndicator.setVisibility(View.INVISIBLE);

        tv_city.setText(model.cityRestModel.cityName);

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
                date.add(model.forecasts[i].time);
            }
        }

        tv_temperatureNight1.setText(String.format("%.2f", temperatureN.get(0)) + "°C");
        tv_temperatureNight2.setText(String.format("%.2f", temperatureN.get(1)) + "°C");
        tv_temperatureNight3.setText(String.format("%.2f", temperatureN.get(2)) + "°C");

        tv_temperatureDay1.setText(String.format("%.2f", temperatureD.get(0)) + "°C");
        tv_temperatureDay2.setText(String.format("%.2f", temperatureD.get(1)) + "°C");
        tv_temperatureDay3.setText(String.format("%.2f", temperatureD.get(2)) + "°C");

        tv_date1.setText(date.get(0));
        tv_date2.setText(date.get(1));
        tv_date3.setText(date.get(2));

        setWeatherIcon(iconsIdN.get(0), tv_IconNight1, false);
        setWeatherIcon(iconsIdN.get(1), tv_IconNight2, false);
        setWeatherIcon(iconsIdN.get(2), tv_IconNight3, false);
        setWeatherIcon(iconsIdD.get(0), tv_IconDay1, true);
        setWeatherIcon(iconsIdD.get(1), tv_IconDay2, true);
        setWeatherIcon(iconsIdD.get(2), tv_IconDay3, true);
    }


    public void updateForecastData(String city, String secretKey) {
        loadIndicator.setVisibility(View.VISIBLE);
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
