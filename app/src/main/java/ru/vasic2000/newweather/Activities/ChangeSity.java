package ru.vasic2000.newweather.Activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.vasic2000.newweather.R;

public class ChangeSity extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button changeCity = getView().findViewById(R.id.btn_changeCity);
        changeCity.setOnClickListener();

        Button goBack = getView().findViewById(R.id.btn_return);
        goBack.setOnClickListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_sity, container, false);
    }

}
