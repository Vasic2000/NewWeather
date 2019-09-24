package ru.vasic2000.newweather.Network;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {
    //https://api.openweathermap.org/data/2.5/weather?q=Mytishchi&APPID=07795d846f9c55c418379de9d14962e7
    //https://api.openweathermap.org/data/2.5/forecast?q=Moscow&APPID=07795d846f9c55c418379de9d14962e7

    private static final String OPEN_WEATHER_MAP_API = "https://api.openweathermap.org/";
    private static final String OPEN_WEATHER_METHOD = "data/2.5/weather";
    private static final String OPEN_WEATHER_FORECAST = "data/2.5/forecast";
    private static final String PARAM = "q";
    private static final String KEY = "APPID";
    private static final String MY_KEY = "07795d846f9c55c418379de9d14962e7";
    private static final String NEW_LINE = "\n";

    public static String getResponseFromURL(URL generatedURL) {
        StringBuilder rawData = new StringBuilder(1024);
        try {
            HttpURLConnection connection = (HttpURLConnection) generatedURL.openConnection();
            InputStream in = connection.getInputStream();
            InputStreamReader is = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(is);
            String tempVariable;
            while ((tempVariable = br.readLine()) != null) {
                rawData.append(tempVariable + NEW_LINE);
            }
            br.close();
            is.close();
            in.close();
            connection.disconnect();
        } catch (IOException e) {
            System.out.println("ИО!!!");
        }

        return rawData.toString();
    }

    public static URL generateURL(String city) {
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

    public static URL generateURLforecast(String city) {
        URL url = null;
        Uri builtUri = Uri.parse(OPEN_WEATHER_MAP_API + OPEN_WEATHER_FORECAST)
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

    public static JSONObject getJSONData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject;
        } catch (JSONException j) {
            return null;
        }
    }

}
