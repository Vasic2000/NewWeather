package ru.vasic2000.newweather.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.net.URL;
import java.util.Date;
import java.util.Locale;

import ru.vasic2000.newweather.Activities.MainActivity;
import ru.vasic2000.newweather.CityPreference;
import ru.vasic2000.newweather.R;

import static ru.vasic2000.newweather.Network.NetworkUtils.generateURL;
import static ru.vasic2000.newweather.Network.NetworkUtils.getJSONData;
import static ru.vasic2000.newweather.Network.NetworkUtils.getResponseFromURL;

public class Weather extends Fragment {
    private static final String LOG_TAG = "WeatherFragment";

    private TextView cityTextView;
    private TextView detailsTextView;
    private TextView currentTemperatureTextView;
    private TextView weatherIcon;
    private ProgressBar loadIndicator;
    private TextView forecast;


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
        cityTextView = rootView.findViewById(R.id.city_field);
        detailsTextView = rootView.findViewById(R.id.details_field);
        currentTemperatureTextView = rootView.findViewById(R.id.temperature_field);
        weatherIcon = rootView.findViewById(R.id.weather_icon_field);
        loadIndicator = rootView.findViewById(R.id.pb_loading_indicator);
        forecast = rootView.findViewById(R.id.tv_forecast);

        final MainActivity ma = (MainActivity) getActivity();
        CityPreference ct = new CityPreference(ma);
        updateWeatherData(ct.getCity(), Locale.getDefault().getLanguage(), ct.getSecretKey());

        forecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ma.changeFragment(R.id.forecast);
            }
        });
    }

    public void updateWeatherData(String city, String language, String SecretKey) {
        URL generatedURL = generateURL(city, language, SecretKey);
        new actualWeather().execute(generatedURL);
    }

    private void renderWeather(JSONObject json) {
        try {
            cityTextView.setText(json.getString("name").toUpperCase(Locale.US) + "," +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            String st1 = details.getString("description").toUpperCase(Locale.US);
            String st2 = main.getString("humidity");
            String st3 = main.getString("pressure");

            if(Locale.getDefault().getLanguage().equals("ru")) {
                detailsTextView.setText(st1 + "\n" +
                        "Влажность: " + st2 + "%\n" + st3 + "кПа");
            } else  {
                detailsTextView.setText(st1 + "\n" +
                        "Humidity: " + st2 + "%\n" + st3 + "hpa");
            }

            Double temp = main.getDouble("temp") - 273.15;

            currentTemperatureTextView.setText(String.format("%.2f", temp) + " °C");

            int actualId = details.getInt("id");
            long sunrise = json.getJSONObject("sys").getLong("sunrise") * 1000;
            long sunset = json.getJSONObject("sys").getLong("sunset") * 1000;

            setWeatherIcon(actualId, sunrise, sunset);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Нет такого города!", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "One or several data missing");
        }
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

    public void changeCity(String city, String key) {
        updateWeatherData(city, Locale.getDefault().getLanguage(), key);
    }

    class actualWeather extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            loadIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            return getResponseFromURL(urls[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            JSONObject answer = getJSONData(response);
            renderWeather(answer);
            loadIndicator.setVisibility(View.INVISIBLE);
        }
    }
}
