package ru.vasic2000.newweather;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import ru.vasic2000.newweather.Activities.ChangeCity;
import ru.vasic2000.newweather.Activities.Forecast;
import ru.vasic2000.newweather.Activities.Setting;
import ru.vasic2000.newweather.Activities.Weather;

public class MainActivity extends AppCompatActivity {
    private static final String WEATHER_FRAGMENT_TAG = "077TAG";
    private static final String WEATHER_CHANGE_TAG = "077TAH";

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
        fragment_changeCity = new ChangeCity();
        fragment_setting = new Setting();
    }

    private void addFragment(Fragment fragment){
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
        fragmentTransaction.commit();
        fragmentManager.popBackStack();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Setting???", Toast.LENGTH_LONG).show();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_city) {
            addFragment(fragment_changeCity);
            Toast.makeText(this, "I can't!!!", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeCity(String city) {
        fragment_weather.changeCity(city);
        cityPreference.setCity(city);
    }
}
