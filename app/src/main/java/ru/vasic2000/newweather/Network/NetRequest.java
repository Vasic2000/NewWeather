package ru.vasic2000.newweather.Network;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetRequest extends AsyncTask<URL, Void, String> {
    private static final String NEW_LINE = "\n";

    @Override
    protected String doInBackground(URL... urls) {
        StringBuilder rawData = new StringBuilder(1024);
        try {
            HttpURLConnection connection = (HttpURLConnection) urls[0].openConnection();
            InputStreamReader is = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(is);
            String tempVariable;
            while ((tempVariable = br.readLine()) != null) {
                rawData.append(tempVariable + NEW_LINE);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("ИО!!!");;
        }
        return rawData.toString();
    }
}
