package ru.vasic2000.newweather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import ru.vasic2000.newweather.R;
import ru.vasic2000.newweather.activities.MainActivity;

public class AboutDeveloper extends Fragment {
    private TextView tvGoBack;
    private ImageView devPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_developer, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View about = getView();
        tvGoBack = about.findViewById(R.id.tv_return4);


        tvGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity ma = (MainActivity) getActivity();
                ma.fragmentBack();
            }
        });

        devPhoto = about.findViewById(R.id.iv_dev_photo);
        Picasso.with(getContext())
                .load("https://avatars1.githubusercontent.com/u/39723055?s=400&u=22e7c65b0cf73e9ce5b3ef84b770ff1439e20afb&v=4")
                .fit()
                .centerCrop()
                .into(devPhoto);

    }

}
