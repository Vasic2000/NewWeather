package ru.vasic2000.newweather.Activities;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import ru.vasic2000.newweather.CityPreference;
import ru.vasic2000.newweather.MainActivity;
import ru.vasic2000.newweather.R;

import static ru.vasic2000.newweather.Network.NetworkUtils.generateURLforecast;
import static ru.vasic2000.newweather.Network.NetworkUtils.getJSONData;
import static ru.vasic2000.newweather.Network.NetworkUtils.getResponseFromURL;

public class Forecast extends Fragment {
    private static final String LOG_TAG = "ForecastFragment";

    private TextView tv_goBack;
    private ProgressBar loadIndicator;
    private TextView tv_city;
    private TextView tv_Icon1;
    private TextView tv_temperatureDay1;
    private TextView tv_temperatureNight1;
    private TextView tv_Icon2;
    private TextView tv_temperatureDay2;
    private TextView tv_temperatureNight2;
    private TextView tv_Icon3;
    private TextView tv_temperatureDay3;
    private TextView tv_temperatureNight3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View forecast = inflater.inflate(R.layout.fragment_forecast, container, false);
        tv_city = forecast.findViewById(R.id.city_forecast);
        tv_goBack = forecast.findViewById(R.id.tv_return);
        loadIndicator = forecast.findViewById(R.id.pb_loading_indicator);

        tv_Icon1 = forecast.findViewById(R.id.weather_icon_1day);
        tv_Icon2 = forecast.findViewById(R.id.weather_icon_2day);
        tv_Icon3 = forecast.findViewById(R.id.weather_icon_3day);

        tv_temperatureDay1 = forecast.findViewById(R.id.temperature_1day);
        tv_temperatureDay2 = forecast.findViewById(R.id.temperature_2day);
        tv_temperatureDay3 = forecast.findViewById(R.id.temperature_3day);

        tv_temperatureNight1 = forecast.findViewById(R.id.temperature_1night);
        tv_temperatureNight2 = forecast.findViewById(R.id.temperature_2night);
        tv_temperatureNight3 = forecast.findViewById(R.id.temperature_3night);

        tv_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity ma = (MainActivity) getActivity();
                ma.removeFragment(ma.fragment_forecast);
            }
        });

        final MainActivity weatherActivity = (MainActivity) getActivity();
        updateForecastData(new CityPreference(weatherActivity).getCity());

        return forecast;
    }

    private void renderForecast(JSONObject json) {
        ArrayList<JSONObject> lists = new ArrayList<>();
        ArrayList<Integer> iconsId = new ArrayList<>();
        ArrayList<Double> temperature = new ArrayList<>();

        try {
            tv_city.setText(json.getJSONObject("city").getString("name").toUpperCase(Locale.US) + ","
                    + json.getJSONObject("city").getString("country"));

            JSONArray list = json.getJSONArray("list");

            for(int i = 0; i < list.length(); i++)
                lists.add(list.getJSONObject(i));

//            Для отладки, а так можно в 6 строк

            JSONObject day1 = glist.getJSONObject(1);
            JSONObject night1 = glist.getJSONObject(5);
            JSONObject day2 = glist.getJSONObject(9);
            JSONObject night2 = glist.getJSONObject(13);
            JSONObject day3 = glist.getJSONObject(17);
            JSONObject night3 = glist.getJSONObject(21);

            Double da1 = day1.getJSONObject("main").getDouble("temp_max") - 273.15;
            Double ni1 = night1.getJSONObject("main").getDouble("temp_min") - 273.15;
            Double da2 = day2.getJSONObject("main").getDouble("temp_max") - 273.15;
            Double ni2 = night2.getJSONObject("main").getDouble("temp_min") - 273.15;
            Double da3 = day3.getJSONObject("main").getDouble("temp_max") - 273.15;
            Double ni3 = night3.getJSONObject("main").getDouble("temp_min") - 273.15;

//            Для отладки, а так можно в 6 строк

            tv_temperatureDay1.setText(String.format("%.2f", da1) + " °C");
            tv_temperatureDay2.setText(String.format("%.2f", da2) + " °C");
            tv_temperatureDay3.setText(String.format("%.2f", da3) + " °C");

            tv_temperatureNight1.setText(String.format("%.2f", ni1) + " °C");
            tv_temperatureNight2.setText(String.format("%.2f", ni2) + " °C");
            tv_temperatureNight3.setText(String.format("%.2f", ni3) + " °C");




//            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
//            JSONObject main = json.getJSONObject("main");
//            String st1 = details.getString("description").toUpperCase(Locale.US);
//            String st2 = main.getString("humidity");
//            String st3 = main.getString("pressure");
//
//            detailsTextView.setText(st1 + "\n" +
//                    "Humidity: " + st2 + "%\n" + st3 + "hpa");
//
//            int actualId = details.getInt("id");
//            long sunrise = json.getJSONObject("sys").getLong("sunrise") * 1000;
//            long sunset = json.getJSONObject("sys").getLong("sunset") * 1000;
//            setWeatherIcon(actualId, sunrise, sunset);

        } catch (Exception e) {
            Log.d(LOG_TAG, "One or severa data missing");
        }
    }

    private void updateForecastData(String city) {
        URL generatedURL = generateURLforecast(city);
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
}
