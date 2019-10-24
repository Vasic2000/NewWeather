package ru.vasic2000.newweather.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import ru.vasic2000.newweather.CityPreference;
import ru.vasic2000.newweather.Fragments.ChangeCity;
import ru.vasic2000.newweather.Fragments.Forecast;
import ru.vasic2000.newweather.Fragments.Setting;
import ru.vasic2000.newweather.Fragments.Weather;
import ru.vasic2000.newweather.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private NavController navController;
    private DrawerLayout drawer;

    public Weather fragment_weather;
    public Forecast fragment_forecast;
    public ChangeCity fragment_changeCity;
    public Setting fragment_setting;
    private CityPreference cityPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        initFragments();

        cityPreference = new CityPreference(this);
    }

    private void initFragments() {
        fragment_weather = new Weather();
        fragment_forecast = new Forecast();
        fragment_setting = new Setting();
        fragment_changeCity = new ChangeCity();
    }

    @Override
    //Чтобы по кнопке назад возвращаться в предыдущий фрагмент
    //чтобы завершить работу, если это стартовый фрагмент
    public void onBackPressed() {

        int bbb = navController.getCurrentDestination().getId();
        int aaa = navController.getGraph().getStartDestination();

        if(bbb == aaa)
            finish();
        else {
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

        if (id == R.id.action_game1) {
            navController.navigate(R.id.game_1);
            return true;
        }

        if (id == R.id.action_game3) {
            navController.navigate(R.id.game_3);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getSecretKey() {
        return cityPreference.getSecretKey();
    }

    public void reDraw(String city) {
        cityPreference.setCity(city);

        if(fragment_weather.getActivity() != null)
            fragment_weather.changeCity(city, getSecretKey());

        if(fragment_forecast.getActivity() != null)
            fragment_forecast.changeCity(city, getSecretKey());
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
        } else if (id == R.id.nav_game1) {
            navController.navigate(R.id.game_1);
        } else if (id == R.id.nav_game3) {
            navController.navigate(R.id.game_3);
        }
        return true;
    }
}
