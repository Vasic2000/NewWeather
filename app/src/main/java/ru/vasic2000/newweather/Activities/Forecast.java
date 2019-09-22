package ru.vasic2000.newweather.Activities;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URL;

import ru.vasic2000.newweather.MainActivity;
import ru.vasic2000.newweather.R;

import static ru.vasic2000.newweather.Network.NetworkUtils.generateURL;
import static ru.vasic2000.newweather.Network.NetworkUtils.getJSONData;
import static ru.vasic2000.newweather.Network.NetworkUtils.getResponseFromURL;

public class Forecast extends Fragment {
    private TextView tv_goBack;
    private ProgressBar loadIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View forecast = inflater.inflate(R.layout.fragment_forecast, container, false);
        tv_goBack = forecast.findViewById(R.id.tv_return);
        loadIndicator = forecast.findViewById(R.id.pb_loading_indicator);


        tv_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity ma = (MainActivity) getActivity();
                ma.removeFragment(ma.fragment_forecast);
            }
        });

        updateForecastData(city);

        return forecast;
    }

    private void renderWeather(JSONObject answer) {
    }

    private void updateForecastData(String city) {
        URL generatedURL = generateURL(city);
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
            renderWeather(answer);
            loadIndicator.setVisibility(View.INVISIBLE);
        }
    }
}
