package ru.vasic2000.newweather.Activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import ru.vasic2000.newweather.R;


public class Setting extends Fragment {
    private static final String BRIGHT = "Bright";
    private static final String DARK = "Dark";

    RadioButton themeDark;
    RadioButton themeBright;

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

        return settingActivity;
    }

}
