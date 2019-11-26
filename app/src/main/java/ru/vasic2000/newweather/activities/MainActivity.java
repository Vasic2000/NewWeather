package ru.vasic2000.newweather.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;

import ru.vasic2000.newweather.CityPreference;
import ru.vasic2000.newweather.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSION_REQUEST_CODE = 10;
    private LocationManager locationManager;
    private String provider;

    private NavController navController;
    private DrawerLayout drawer;
    private CityPreference cityPreference;

    private Double latitude;
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Проверим на пермиссии, и если их нет, запросим у пользователя
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // пермиссии нет, будем запрашивать у пользователя
            requestLocationPermissions();
        } else {
            requestLocation();
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);
            navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            cityPreference = new CityPreference(this);
        }
    }

    @Override
    //Чтобы по кнопке назад возвращаться в предыдущий фрагмент
    //чтобы завершить работу, если это стартовый фрагмент
    public void onBackPressed() {
        int currentFragmentID = navController.getCurrentDestination().getId();
        int startDestination = navController.getGraph().getStartDestination();
        if (currentFragmentID == startDestination) {
            cityPreference.setCity("");
            finish();
        } else {
            fragmentBack();
        }
    }

    public void changeFragment(int id) {
        navController.navigate(id);
    }

    public void fragmentBack() {
        navController.popBackStack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            navController.navigate(R.id.setting);
            return true;
        }

        if (id == R.id.action_change_city) {
            navController.navigate(R.id.changeCity);
            return true;
        }

        if (id == R.id.action_about) {
            navController.navigate(R.id.aboutDeveloper);
            return true;
        }

        if (id == R.id.action_feedback) {
            navController.navigate(R.id.feedbackForm);
            return true;
        }

        if (id == R.id.action_t_sensor) {
            navController.navigate(R.id.temperature_sensor);
            return true;
        }

        if (id == R.id.action_h_sensor) {
            navController.navigate(R.id.humidity_sensor);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void reDraw(String city) {
        cityPreference.setCity(city);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_current_weather) {
            navController.navigate(R.id.weather);
        } else if (id == R.id.nav_forecast) {
            navController.navigate(R.id.forecast);
        } else if (id == R.id.nav_setting) {
            navController.navigate(R.id.setting);
        } else if (id == R.id.nav_change_city) {
            navController.navigate(R.id.changeCity);

        } else if (id == R.id.nav_about) {
            navController.navigate(R.id.aboutDeveloper);
        } else if (id == R.id.nav_feedback) {
            navController.navigate(R.id.feedbackForm);
        } else if (id == R.id.nav_temp_sensor) {
            navController.navigate(R.id.temperature_sensor);
        } else if (id == R.id.nav_humid_sensor) {
            navController.navigate(R.id.humidity_sensor);
        }
        drawer.closeDrawers();
        return true;
    }

    // Запрос пермиссии для геолокации
    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            // Запросим эти две пермиссии у пользователя
            ActivityCompat.requestPermissions(this,
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
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                drawer = findViewById(R.id.drawer_layout);
                NavigationView navigationView = findViewById(R.id.nav_view);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.addDrawerListener(toggle);
                toggle.syncState();
                navigationView.setNavigationItemSelectedListener(this);
                navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                cityPreference = new CityPreference(this);
            }
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // запросим координаты
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            provider = locationManager.getBestProvider(criteria, true);

            Location loc = locationManager.getLastKnownLocation(provider);
            setLatitude(loc.getLatitude());
            setLongitude(loc.getLongitude());

            LocationListener ls = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        setLatitude(location.getLatitude());
                        setLongitude(location.getLongitude());
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
                locationManager.requestSingleUpdate(provider, ls, null);
            }
        } else {
            // пермиссии не появилось - выход
            return;
        }
    }
}