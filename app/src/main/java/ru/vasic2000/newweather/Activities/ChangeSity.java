package ru.vasic2000.newweather.Activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.vasic2000.newweather.MainActivity;
import ru.vasic2000.newweather.R;

public class ChangeSity extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewChangeCity = inflater.inflate(R.layout.fragment_change_sity, container, false);
        Button changeCity = viewChangeCity.findViewById(R.id.btn_changeCity);
        changeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity ma = (MainActivity) getActivity();
                ma.removeFragment(ma.fragment_changeSity);

            }
        });

        Button goBack = viewChangeCity.findViewById(R.id.btn_return);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mai = (MainActivity) getActivity();
                mai.removeFragment(getParentFragment());
            }
        });

        return viewChangeCity;
    }
}
