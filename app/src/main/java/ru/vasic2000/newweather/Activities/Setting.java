package ru.vasic2000.newweather.Activities;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import ru.vasic2000.newweather.MainActivity;
import ru.vasic2000.newweather.R;


public class Setting extends Fragment {
    RadioButton themeDark;
    RadioButton themeBright;
    TextView goBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View settingActivity = inflater.inflate(R.layout.fragment_setting, container, false);
        themeDark = settingActivity.findViewById(R.id.rb_dark);
        themeBright = settingActivity.findViewById(R.id.rb_standard);
        goBack = settingActivity.findViewById(R.id.tv_back);

        themeBright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                if(Locale.getDefault().getLanguage().equals("ru")) {
                    Toast.makeText(context, "Всегда зелёная тема (((", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Always green (((", Toast.LENGTH_LONG).show();
                }
            }
        });

        themeDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                if(Locale.getDefault().getLanguage().equals("ru")) {
                    Toast.makeText(context, "Всегда зелёная тема (((", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Always green (((", Toast.LENGTH_LONG).show();
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity ma = (MainActivity) getActivity();
                ma.removeFragment(ma.fragment_setting);
            }
        });

        return settingActivity;
    }

}
