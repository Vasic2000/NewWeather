package ru.vasic2000.newweather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import ru.vasic2000.newweather.activities.MainActivity;
import ru.vasic2000.newweather.R;

public class ChangeCity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewChangeCity = inflater.inflate(R.layout.fragment_change_sity, container, false);
        final EditText city_field = viewChangeCity.findViewById(R.id.et_city_field);
        Button changeCity = viewChangeCity.findViewById(R.id.btn_changeCity);

        changeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                String newCity = city_field.getText().toString();
                activity.reDraw(newCity);
                activity.fragmentBack();
            }
        });

        Button goBack = viewChangeCity.findViewById(R.id.btn_return);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();

                activity.fragmentBack();
            }
        });

        return viewChangeCity;
    }
}
