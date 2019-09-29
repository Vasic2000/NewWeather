package ru.vasic2000.newweather;

import android.app.Activity;
import android.content.SharedPreferences;

public class CityPreference {
    private static final String KEY = "city";
    private static final String MOSCOW = "Moscow";

    private SharedPreferences userPrefereces; //Спец класс для длительного хранения

    public CityPreference(Activity activity) {
        userPrefereces = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity() {
        return userPrefereces.getString(KEY, MOSCOW);
    }

    public void setCity(String city) {
        userPrefereces.edit().putString(KEY, city).apply();
    }

}
