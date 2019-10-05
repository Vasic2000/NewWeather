package ru.vasic2000.newweather.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import ru.vasic2000.newweather.CityPreference;
import ru.vasic2000.newweather.Fragments.ChangeCity;
import ru.vasic2000.newweather.Fragments.Forecast;
import ru.vasic2000.newweather.Fragments.Setting;
import ru.vasic2000.newweather.Fragments.Weather;
import ru.vasic2000.newweather.R;

public class MainActivity extends AppCompatActivity {

    public Weather fragment_weather;
    public Forecast fragment_forecast;
    public ChangeCity fragment_changeCity;
    public Setting fragment_setting;
    private CityPreference cityPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cityPreference = new CityPreference(this);

        if(savedInstanceState == null) {
            addFragment(fragment_weather);
        }
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
        fragmentTransaction.add(R.id.container_for_fragment, fragment);
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

    public void reDraw(String city) {
        cityPreference.setCity(city);

//        if(fragment_weather.getActivity() != null)
//            fragment_weather.changeCity(city);
//
//        if(fragment_forecast.getActivity() != null)
//            fragment_forecast.changeCity(city);
    }
}
