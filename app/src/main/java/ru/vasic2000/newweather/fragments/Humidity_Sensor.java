package ru.vasic2000.newweather.fragments;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ru.vasic2000.newweather.activities.MainActivity;
import ru.vasic2000.newweather.R;

public class Humidity_Sensor extends Fragment implements SensorEventListener {
    private TextView tvGoBack3;
    private TextView tvHumidity;

    private SensorManager mSensorManager;
    private Sensor sensorHumidity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_humid_sensor, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View tHumid = getView();

        tvGoBack3 = tHumid.findViewById(R.id.tv_return3);
        tvHumidity = tHumid.findViewById(R.id.tv_h_value);

        tvGoBack3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity ma = (MainActivity) getActivity();
                ma.fragmentBack();
            }
        });


        // Менеджер датчиков
        mSensorManager = (SensorManager) Objects.requireNonNull(this.getActivity()).getSystemService(Activity.SENSOR_SERVICE);
        // Датчик влажности (у меня нет)
        sensorHumidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);


        // Слушатель датчика температуры
        SensorEventListener listenerTemperature = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            @Override
            public void onSensorChanged(SensorEvent event) {
                showTemperatureSensors(event);
            }
        };

        // Регистрируем слушатель датчика температуры
        mSensorManager.registerListener(listenerTemperature, sensorHumidity,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        showTemperatureSensors(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // Вывод датчика влажности
    private void showTemperatureSensors(SensorEvent event){
        StringBuilder strB = new StringBuilder();
        strB.append(event.values[0]);
        strB.append("%");
        tvHumidity.setText(strB);
    }
}
