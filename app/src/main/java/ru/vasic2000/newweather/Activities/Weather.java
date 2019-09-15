package ru.vasic2000.newweather.Activities;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Locale;

import ru.vasic2000.newweather.CityPreference;
import ru.vasic2000.newweather.MainActivity;
import ru.vasic2000.newweather.Network.NetRequest;
import ru.vasic2000.newweather.R;


public class Weather extends Fragment {
    private static final String LOG_TAG = "WeatherFragment";

    private TextView cityTextView;
    private TextView detailsTextView;
    private TextView currentTemperatureTextView;
    private TextView weatherIcon;


    private static final String OPEN_WEATHER_MAP_API = "https://api.openweathermap.org/";
    private static final String OPEN_WEATHER_METHOD = "data/2.5/weather";
    private static final String PARAM = "q";
    private static final String KEY = "APPID";
    private static final String MY_KEY = "07795d846f9c55c418379de9d14962e7";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View rootView = getView();
        cityTextView = rootView.findViewById(R.id.city_field);
        detailsTextView = rootView.findViewById(R.id.details_field);
        currentTemperatureTextView = rootView.findViewById(R.id.temperature_field);
        weatherIcon = rootView.findViewById(R.id.weather_icon_field);

        MainActivity weatherActivity = (MainActivity) getActivity();
        updateWeatherData(new CityPreference(weatherActivity).getCity());
    }

    private void updateWeatherData(String city) {
        URL generatedURL = generateURL(city);
        String responseFromURL = (new NetRequest().execute(generatedURL)).toString();
        JSONObject response = getJSONData(responseFromURL);
        renderWeather(response);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }


    static URL generateURL(String city) {
        URL url = null;
        Uri builtUri = Uri.parse(OPEN_WEATHER_MAP_API + OPEN_WEATHER_METHOD)
                .buildUpon()
                .appendQueryParameter(PARAM, city)
                .appendQueryParameter(KEY, MY_KEY)
                .build();
        try {
            String appi = builtUri.toString();
            url = new URL(appi);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    static JSONObject getJSONData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject;
        } catch (JSONException j) {
            return null;
        }
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

            detailsTextView.setText(st1 + "\n" +
                    "Humidity: " + st2 + "%\n" + st3 + "hpa");

            Double temp = main.getDouble("temp") - 273.15;

            currentTemperatureTextView.setText(String.format("%.2f", temp) + " °C");

            int actualId = details.getInt("id");
            long sunrise = json.getJSONObject("sys").getLong("sunrise") * 1000;
            long sunset = json.getJSONObject("sys").getLong("sunset") * 1000;

            setWeatherIcon(actualId, sunrise, sunset);

        } catch (Exception e) {
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
}
