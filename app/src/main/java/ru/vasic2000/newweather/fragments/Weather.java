package ru.vasic2000.newweather.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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

import static android.content.Context.LOCATION_SERVICE;

public class Weather extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 10;
    private LocationManager locationManager;
    private String provider;

    private TextView cityTextView;
    private TextView detailsTextView;
    private TextView currentTemperatureTextView;
    private TextView weatherIcon;
    private ProgressBar loadIndicator;
    private TextView forecast;

    private MainActivity activity;
    private CityPreference cityPreference;
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
        // Проверим на пермиссии, и если их нет, запросим у пользователя
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // пермиссии нет, будем запрашивать у пользователя
            requestLocationPermissions();
        } else {
            requestLocation();
            cityPreference = new CityPreference(getActivity());

            View rootView = getView();
            assert rootView != null;
            cityTextView = rootView.findViewById(R.id.city_field);
            detailsTextView = rootView.findViewById(R.id.details_field);
            currentTemperatureTextView = rootView.findViewById(R.id.temperature_field);
            weatherIcon = rootView.findViewById(R.id.weather_icon_field);
            loadIndicator = rootView.findViewById(R.id.pb_loading_indicator);
            forecast = rootView.findViewById(R.id.tv_forecast);

            initDB();

            activity = (MainActivity) getActivity();
            assert activity != null;
            updateWeatherData(cityPreference.getCity(), cityPreference.getSecretKey());

            forecast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.changeFragment(R.id.forecast);
                }
            });
        }
    }

    private void initDB() {
        dataBase = new DataBaseHelper(getContext()).getWritableDatabase();
    }

    private void updateWeatherData(String city, String SecretKey) {
        if(!city.equals("") && !city.equals(null)) {
            if (WeathersTable.isCityInBase(city, dataBase)) {
                if (WeathersTable.actualWeatherTime(city, dataBase)) {
                    weatherFromSQL(city, dataBase);
                }
            } else {
                weatherFromInternet(city, SecretKey);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // пермиссии нет, будем запрашивать у пользователя
                requestLocationPermissions();
            } else {
                Location loc = locationManager.getLastKnownLocation(provider);
                weatherFromInternetByCoord(loc.getLatitude(), loc.getLongitude());
            }
//            Double lat = activity.getLatitude();
//            Double lon = activity.getLongitude();
//            weatherFromInternetByCoord(lat, lon);
        }
    }

    private void weatherFromInternet(String city, String SecretKey) {
        weatherLoaderOn();
        if(!city.equals("") && !city.equals(null)) {
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
        } else {
            weatherFromInternetByCoord(activity.getLatitude(), activity.getLongitude());
        }
    }


    private void weatherFromInternetByCoord(double lat, double lon) {
        weatherLoaderOn();
        cityPreference = new CityPreference(getActivity());
        String SecretKey = cityPreference.getSecretKey();

        if (Locale.getDefault().getLanguage().equals("ru")) {
            OpenWeatherRepo.getSingleton().getAPI().loadWeatherCoord(lat, lon, SecretKey, "RU",
                    "metric").enqueue(new Callback<WeatherRequestRestModel>() {
                @Override
                public void onResponse(Call<WeatherRequestRestModel> call,
                                       Response<WeatherRequestRestModel> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        String cityText;
                        if (response.body().cityName != null) {
                            if(!response.body().cityName.equals("")) {
                                if (response.body().sys.country != null) {
                                    cityText = response.body().cityName.toUpperCase(Locale.US) + "," + response.body().sys.country;
                                } else {
                                    cityText = response.body().cityName.toUpperCase(Locale.US);
                                }
                            } else {
                                cityText = response.body().coords.latitude + " : " + response.body().coords.longitude; // Широта : Долгота
                            }
                        } else {
                            cityText = response.body().coords.latitude + " : " + response.body().coords.longitude; // Широта : Долгота
                        }
                        cityPreference.setCity(cityText);
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
            OpenWeatherRepo.getSingleton().getAPI().loadWeatherCoord(lat, lon, SecretKey, "EN",
                    "metric").enqueue(new Callback<WeatherRequestRestModel>() {
                @Override
                public void onResponse(Call<WeatherRequestRestModel> call,
                                       Response<WeatherRequestRestModel> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        String cityText;
                        if (response.body().cityName != null) {
                            if(!response.body().cityName.equals("")) {
                                if (response.body().sys.country != null) {
                                    cityText = response.body().cityName.toUpperCase(Locale.US) + "," + response.body().sys.country;
                                } else {
                                    cityText = response.body().cityName.toUpperCase(Locale.US);
                                }
                            } else {
                                cityText = response.body().coords.latitude + " : " + response.body().coords.longitude; // Широта : Долгота
                            }
                        } else {
                            cityText = response.body().coords.latitude + " : " + response.body().coords.longitude; // Широта : Долгота
                        }
                        cityPreference.setCity(cityText);
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

        String cityText = result.get(0).toUpperCase(Locale.US);
        cityTextView.setText(cityText);

        String tempText = result.get(2) +" °C";
        currentTemperatureTextView.setText(tempText);

        StringBuilder humidityValue = new StringBuilder();
        if(Locale.getDefault().getLanguage().equals("ru")) {
            humidityValue.append(result.get(5))
                    .append("\n")
                    .append("Влажность: ")
                    .append(result.get(4))
                    .append("%\n")
                    .append(result.get(3))
                    .append(" кПа");
        } else  {
            humidityValue.append(result.get(5))
                    .append("\n")
                    .append("Humidity: ")
                    .append(result.get(4))
                    .append("%\n")
                    .append(result.get(3))
                    .append(" hpa");
        }
        detailsTextView.setText(humidityValue);

        setWeatherIcon(Integer.valueOf(result.get(6)), Long.valueOf(result.get(7)), Long.valueOf(result.get(8)));

        weatherLoaderOff();
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
        String cityText;
        if (model.cityName != null) {
            if(!model.cityName.equals("")) {
                if (model.sys.country != null) {
                    cityText = model.cityName.toUpperCase(Locale.US) + "," + model.sys.country;
                } else {
                    cityText = model.cityName.toUpperCase(Locale.US);
                }
            } else {
                cityText = model.coords.latitude + " : " + model.coords.longitude; // Широта : Долгота
            }
        } else {
            cityText = model.coords.latitude + " : " + model.coords.longitude; // Широта : Долгота
        }

        cityTextView.setText(cityText);

        @SuppressLint("DefaultLocale") String tempText = String.format("%.2f", model.main.temperature) + " °C";
        currentTemperatureTextView.setText(tempText);

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
        WeathersTable.addInfo(cityText,
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




    // Запрос пермиссии для геолокации
    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
            // Запросим эти две пермиссии у пользователя
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }


    // Это результат запроса у пользователя пермиссии
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {   // Это та самая пермиссия, что мы запрашивали?
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                // Все препоны пройдены и пермиссия дана
                requestLocation();
                cityPreference = new CityPreference(getActivity());

                View rootView = getView();
                assert rootView != null;
                cityTextView = rootView.findViewById(R.id.city_field);
                detailsTextView = rootView.findViewById(R.id.details_field);
                currentTemperatureTextView = rootView.findViewById(R.id.temperature_field);
                weatherIcon = rootView.findViewById(R.id.weather_icon_field);
                loadIndicator = rootView.findViewById(R.id.pb_loading_indicator);
                forecast = rootView.findViewById(R.id.tv_forecast);

                initDB();

                activity = (MainActivity) getActivity();
                assert activity != null;
                updateWeatherData(cityPreference.getCity(), cityPreference.getSecretKey());

                forecast.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.changeFragment(R.id.forecast);
                    }
                });
            }
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // запросим координаты
            locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            provider = locationManager.getBestProvider(criteria, true);

                LocationListener ls = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if (location != null) {
                            activity.setLatitude(location.getLatitude());
                            activity.setLongitude(location.getLongitude());
                            updateWeatherData(cityPreference.getCity(), cityPreference.getSecretKey());
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                };

                if (provider != null) {
                    locationManager.requestLocationUpdates(provider, 10000, 10, ls);
                }
            } else {
                // пермиссии не появилось - выход
                return;
            }
        }
}
