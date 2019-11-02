package ru.vasic2000.newweather.Fragments;

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

import ru.vasic2000.newweather.Activities.MainActivity;
import ru.vasic2000.newweather.R;


public class Temperature_Sensor extends Fragment implements SensorEventListener {
    private TextView tv_goBack1;
    private TextView tv_temperature;

    private SensorManager mSensorManager;
    private Sensor sensorTemperature;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_temp_sensor, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View tSensor = getView();

        tv_goBack1 = tSensor.findViewById(R.id.tv_return1);
        tv_temperature = tSensor.findViewById(R.id.tv_t_value);

        tv_goBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity ma = (MainActivity) getActivity();
                ma.fragmentBack();
            }
        });


        // Менеджер датчиков
        mSensorManager = (SensorManager) Objects.requireNonNull(this.getActivity()).getSystemService(Activity.SENSOR_SERVICE);
        // Датчик температыры (у меня нет)
        sensorTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);


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
        mSensorManager.registerListener(listenerTemperature, sensorTemperature,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        showTemperatureSensors(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // Вывод датчика температуры
    private void showTemperatureSensors(SensorEvent event){
        StringBuilder strB = new StringBuilder();
        strB.append(event.values[0]);
        strB.append("°С");
        tv_temperature.setText(strB);
    }
}
