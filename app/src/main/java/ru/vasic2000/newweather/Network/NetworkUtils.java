package ru.vasic2000.newweather.Network;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {

    private static final String OPEN_WEATHER_MAP_API = "https://api.openweathermap.org/";
    private static final String OPEN_WEATHER_METHOD = "data/2.5/weather";
    private static final String OPEN_WEATHER_FORECAST = "data/2.5/forecast";
    private static final String PARAM = "q";
    private static final String LANG = "lang";
    private static final String KEY = "APPID";
    private static final String NEW_LINE = "\n";

    public static String getResponseFromURL(URL generatedURL) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(generatedURL).build();
        String rawData = "";
        try {
            Response response = client.newCall(request).execute();
            rawData = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rawData;
    }

    public static URL generateURL(String city, String language, String Key) {
        URL url = null;
        Uri builtUri = Uri.parse(OPEN_WEATHER_MAP_API + OPEN_WEATHER_METHOD)
                .buildUpon()
                .appendQueryParameter(PARAM, city)
                .appendQueryParameter(LANG, language.toUpperCase())
                .appendQueryParameter(KEY, Key)
                .build();
        try {
            String appi = builtUri.toString();
            url = new URL(appi);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL generateURLforecast(String city, String language, String Key) {
        URL url = null;
        Uri builtUri = Uri.parse(OPEN_WEATHER_MAP_API + OPEN_WEATHER_FORECAST)
                .buildUpon()
                .appendQueryParameter(PARAM, city)
                .appendQueryParameter(LANG, language.toUpperCase())
                .appendQueryParameter(KEY, Key)
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
