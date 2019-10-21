package ru.vasic2000.newweather.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import ru.vasic2000.newweather.CityPreference;
import ru.vasic2000.newweather.Fragments.ChangeCity;
import ru.vasic2000.newweather.Fragments.Forecast;
import ru.vasic2000.newweather.Fragments.Setting;
import ru.vasic2000.newweather.Fragments.Weather;
import ru.vasic2000.newweather.R;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

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

        initSideMenu();

        initFragments();

        cityPreference = new CityPreference(this);

        if(savedInstanceState == null) {
            addFragment(fragment_weather);
        }
    }

    private void initSideMenu() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.action_settings, R.id.action_change_city,
                R.id.action_game1, R.id.action_game2)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void initFragments() {
        fragment_weather = new Weather();
        fragment_forecast = new Forecast();
        fragment_setting = new Setting();
        fragment_changeCity = new ChangeCity();
    }

    @Override
    //Чтобы по кнопке назад не оставалось пустого экрана без фрагмента
    //Чтобы по кнопке назад возвращался в предыдущий фрагмент
    public void onBackPressed() {
        List<Fragment> fragmentsList = getSupportFragmentManager().getFragments();
        if(fragmentsList.size() <= 1)
            finish();
        else {
            removeFragment(fragmentsList.get(fragmentsList.size()-1));
        }
    }

    public void addFragment(Fragment fragment){
        // Открыть транзакцию
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        List<Fragment> fragmentsList = getSupportFragmentManager().getFragments();
        if (!fragmentsList.contains(fragment))
        // Добавить фрагмент
        fragmentTransaction.add(R.id.nav_host_fragment, fragment);
        fragmentTransaction.addToBackStack("");
        // Закрыть транзакцию
        fragmentTransaction.commit();
    }

    public void removeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentManager.popBackStack();
        fragmentTransaction.commit();
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
            addFragment(fragment_setting);
            return true;
        }

        if (id == R.id.action_change_city) {
            addFragment(fragment_changeCity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getSecretKey() {
        return cityPreference.getSecretKey();
    }

    public void reDraw(String city) {
        cityPreference.setCity(city);

//        if(fragment_weather.getActivity() != null)
//            fragment_weather.changeCity(city);
//
//        if(fragment_forecast.getActivity() != null)
//            fragment_forecast.changeCity(city);
    }
}
