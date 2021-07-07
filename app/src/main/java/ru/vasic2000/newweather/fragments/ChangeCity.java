package ru.vasic2000.newweather.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
        final Button changeCity = viewChangeCity.findViewById(R.id.btn_changeCity);

        changeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                String newCity = city_field.getText().toString();
                hideKeyboard(getActivity());
                activity.reDraw(newCity);
                activity.fragmentBack();
            }
        });

        final Button goBack = viewChangeCity.findViewById(R.id.btn_return);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                hideKeyboard(getActivity());
                activity.fragmentBack();
            }
        });

        return viewChangeCity;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
