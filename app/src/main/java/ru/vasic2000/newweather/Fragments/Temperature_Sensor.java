package ru.vasic2000.newweather.Fragments;

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

import java.util.List;

import ru.vasic2000.newweather.Activities.MainActivity;
import ru.vasic2000.newweather.R;

public class Temperature_Sensor extends Fragment {
    private TextView tv_goBack1;
    private TextView tv_temperature;

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
    }
}
