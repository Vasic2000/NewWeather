package ru.vasic2000.newweather;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ru.vasic2000.newweather.Activities.ChangeSity;
import ru.vasic2000.newweather.Activities.Weather;

public class MainActivity extends AppCompatActivity {
    private static final String WEATHER_FRAGMENT_TAG = "077TAG";
    private static final String WEATHER_CHANGE_TAG = "077TAH";

    private CityPreference cityPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cityPreference = new CityPreference(this);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container_for_fragment,
                    new Weather(), WEATHER_FRAGMENT_TAG).commit();
        }
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
            getSupportFragmentManager().beginTransaction().add(R.id.container_for_fragment,
                    new ChangeSity(), WEATHER_CHANGE_TAG).commit();
            Toast.makeText(this, "I çan't!!!", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
