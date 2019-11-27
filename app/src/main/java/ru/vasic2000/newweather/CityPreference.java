package ru.vasic2000.newweather;

import android.app.Activity;
import android.content.SharedPreferences;

public class CityPreference {
    private static final String KEY = "city";
    private static final String MOSCOW = "";
    private static final String OW_KEY = "APPID";
    private static final String OW_MY_KEY = "07795d846f9c55c418379de9d14962e7";

    private SharedPreferences sharedPreferences; //Спец класс для длительного хранения

    public CityPreference(Activity activity) {
        sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity() {
        return sharedPreferences.getString(KEY, MOSCOW);
    }

    public String getSecretKey() {
        return sharedPreferences.getString(OW_KEY, OW_MY_KEY);
    }

    public void setCity(String city) {
        sharedPreferences.edit().putString(KEY, city).apply();
    }

}
