package ru.vasic2000.newweather.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.vasic2000.newweather.CityPreference;
import ru.vasic2000.newweather.R;
import ru.vasic2000.newweather.activities.MainActivity;
import ru.vasic2000.newweather.dataBase.DataBaseHelper;
import ru.vasic2000.newweather.dataBase.WeathersTable;
import ru.vasic2000.newweather.rest.entities.OpenWeatherRepo;
import ru.vasic2000.newweather.rest.entities.Weather.WeatherRequestRestModel;

public class Weather extends Fragment {

    private TextView cityTextView;
    private TextView detailsTextView;
    private TextView currentTemperatureTextView;
    private TextView weatherIcon;
    private ProgressBar loadIndicator;
    private TextView forecast;

    private SQLiteDatabase dataBase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View weather = inflater.inflate(R.layout.fragment_weather, container, false);
        return weather;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View rootView = getView();
        assert rootView != null;
        cityTextView = rootView.findViewById(R.id.city_field);
        detailsTextView = rootView.findViewById(R.id.details_field);
        currentTemperatureTextView = rootView.findViewById(R.id.temperature_field);
        weatherIcon = rootView.findViewById(R.id.weather_icon_field);
        loadIndicator = rootView.findViewById(R.id.pb_loading_indicator);
        forecast = rootView.findViewById(R.id.tv_forecast);

        initDB();

        final MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        CityPreference cityPreference = new CityPreference(activity);
        updateWeatherData(cityPreference.getCity(), cityPreference.getSecretKey());

        forecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeFragment(R.id.forecast);
            }
        });
    }

    private void initDB() {
        dataBase = new DataBaseHelper(getContext()).getWritableDatabase();
    }

    private void updateWeatherData(String city, String SecretKey) {

        if (WeathersTable.isCityInBase(city, dataBase)) {
            if (WeathersTable.actualWeatherTime(city, dataBase)) {
                weatherFromSQL(city, dataBase);
            }
        } else {
            weatherFromInternet(city, SecretKey);
        }
    }

    private void weatherFromInternet(String city, String SecretKey) {
        weatherLoaderOn();
        if (Locale.getDefault().getLanguage().equals("ru")) {
            OpenWeatherRepo.getSingleton().getAPI().loadWeather(city, SecretKey, "RU",
                    "metric").enqueue(new Callback<WeatherRequestRestModel>() {
                @Override
                public void onResponse(Call<WeatherRequestRestModel> call,
                                       Response<WeatherRequestRestModel> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        renderWeather(response.body());
                    } else {
                        Toast.makeText(getContext(), getString(R.string.wrong_answer), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<WeatherRequestRestModel> call, Throwable t) {
                    Toast.makeText(getContext(), getString(R.string.netork_failure), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            OpenWeatherRepo.getSingleton().getAPI().loadWeather(city, SecretKey, "EN",
                    "metric").enqueue(new Callback<WeatherRequestRestModel>() {
                @Override
                public void onResponse(Call<WeatherRequestRestModel> call,
                                       Response<WeatherRequestRestModel> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        renderWeather(response.body());
                    } else {
                        Toast.makeText(getContext(), getString(R.string.wrong_answer), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<WeatherRequestRestModel> call, Throwable t) {
                    Toast.makeText(getContext(), getString(R.string.netork_failure), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void weatherFromSQL(String city, SQLiteDatabase db) {
        Toast.makeText(getContext(), "Погода из SQL!!!", Toast.LENGTH_LONG).show();
        List<String> result = WeathersTable.getInfoFromSQL(city, db);

        cityTextView.setText(result.get(0).toUpperCase(Locale.US) + "," + result.get(1));
        currentTemperatureTextView.setText(result.get(2) +" °C");

        StringBuilder humidityValue = new StringBuilder();
        if(Locale.getDefault().getLanguage().equals("ru")) {
            humidityValue.append(result.get(3))
                    .append("\n")
                    .append("Влажность: ")
                    .append(result.get(4))
                    .append("%\n")
                    .append(result.get(5))
                    .append(" кПа");
        } else  {
            humidityValue.append(result.get(3))
                    .append("\n")
                    .append("Humidity: ")
                    .append(result.get(4))
                    .append("%\n")
                    .append(result.get(5))
                    .append(" hpa");
        }
        detailsTextView.setText(humidityValue);

        setWeatherIcon(Integer.valueOf(result.get(7)), Long.valueOf(result.get(8)), Long.valueOf(result.get(9)));


    }


    private void weatherLoaderOn() {
        loadIndicator.setVisibility(View.VISIBLE);

        cityTextView.setVisibility(View.INVISIBLE);
        detailsTextView.setVisibility(View.INVISIBLE);
        currentTemperatureTextView.setVisibility(View.INVISIBLE);
        weatherIcon.setVisibility(View.INVISIBLE);
        forecast.setVisibility(View.INVISIBLE);
    }

    private void weatherLoaderOff() {
        cityTextView.setVisibility(View.VISIBLE);
        detailsTextView.setVisibility(View.VISIBLE);
        currentTemperatureTextView.setVisibility(View.VISIBLE);
        weatherIcon.setVisibility(View.VISIBLE);
        forecast.setVisibility(View.VISIBLE);

        loadIndicator.setVisibility(View.INVISIBLE);
    }

    private void renderWeather(WeatherRequestRestModel model) {

        cityTextView.setText(model.cityName.toUpperCase(Locale.US) + "," + model.sys.country);

        currentTemperatureTextView.setText(String.format("%.2f", model.main.temperature) + " °C");

        StringBuilder humidityValue = new StringBuilder();
        if(Locale.getDefault().getLanguage().equals("ru")) {
            humidityValue.append(model.weathers[0].description)
                    .append("\n")
                    .append("Влажность: ")
                    .append(model.main.humidity)
                    .append("%\n")
                    .append(model.main.pressure)
                    .append(" кПа");
        } else  {
            humidityValue.append(model.weathers[0].description)
                    .append("\n")
                    .append("Humidity: ")
                    .append(model.main.humidity)
                    .append("%\n")
                    .append(model.main.pressure)
                    .append(" hpa");
        }
        detailsTextView.setText(humidityValue);

        setWeatherIcon(model.weathers[0].index, model.sys.sunrise, model.sys.sunset);

        // Дополняю город с погодой в SQL
        WeathersTable.addInfo(model.cityName,
                model.sys.country,
                model.main.temperature,
                model.main.humidity,
                model.main.pressure,
                model.weathers[0].description,
                model.weathers[0].index,
                model.sys.sunrise,
                model.sys.sunset,
                System.currentTimeMillis(),
                dataBase);

        weatherLoaderOff();
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";

        if(actualId == 800) {
            long currentTime = new Date().getTime();
            if(currentTime > sunrise && currentTime < sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
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
