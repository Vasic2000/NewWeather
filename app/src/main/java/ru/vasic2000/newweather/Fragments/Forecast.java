package ru.vasic2000.newweather.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import ru.vasic2000.newweather.Activities.MainActivity;
import ru.vasic2000.newweather.CityPreference;
import ru.vasic2000.newweather.R;

import static ru.vasic2000.newweather.Network.NetworkUtils.generateURLforecast;
import static ru.vasic2000.newweather.Network.NetworkUtils.getJSONData;
import static ru.vasic2000.newweather.Network.NetworkUtils.getResponseFromURL;

public class Forecast extends Fragment {
    private static final String LOG_TAG = "ForecastFragment";

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
        updateForecastData(ct.getCity(), Locale.getDefault().getLanguage(), ct.getSecretKey());

        tv_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                assert activity != null;
                activity.fragmentBack();
            }
        });
    }

    private void renderForecast(JSONObject json) {
        ArrayList<JSONObject> lists = new ArrayList<>();
        ArrayList<Integer> iconsIdD = new ArrayList<>();
        ArrayList<Integer> iconsIdN = new ArrayList<>();
        ArrayList<Double> temperatureD = new ArrayList<>();
        ArrayList<Double> temperatureN = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();

        try {
            tv_city.setText(json.getJSONObject("city").getString("name").toUpperCase(Locale.US) + ","
                    + json.getJSONObject("city").getString("country"));

            JSONArray list = json.getJSONArray("list");
            String ssstr;

//            Ищу данные для 3 часов дня и 3 часов ночи
            for(int i = 0; i < list.length(); i++) {
                lists.add(list.getJSONObject(i));
                ssstr = list.getJSONObject(i).getString("dt_txt");
                ssstr = ssstr.substring(ssstr.length() - 8, ssstr.length() - 6);
                if(ssstr.equals("15") && (iconsIdN.size() != 0)) {
                    temperatureD.add(list.getJSONObject(i).getJSONObject("main").getDouble("temp_max") - 273.15);
                    iconsIdD.add(list.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getInt("id"));
                }
                if(ssstr.equals("03")) {
                    temperatureN.add(list.getJSONObject(i).getJSONObject("main").getDouble("temp_min") - 273.15);
                    iconsIdN.add(list.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getInt("id"));
                    date.add(list.getJSONObject(i).getString("dt_txt").substring(0, 10));
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

        } catch (Exception e) {
            Log.d(LOG_TAG, "One or several data missing");
        }
    }

    public void updateForecastData(String city, String language, String secretKey) {
        URL generatedURL = generateURLforecast(city, language, secretKey);
        new MakeForecast().execute(generatedURL);
    }

    class MakeForecast extends AsyncTask<URL, Void, String> {
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
            renderForecast(answer);
            loadIndicator.setVisibility(View.INVISIBLE);
        }
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
