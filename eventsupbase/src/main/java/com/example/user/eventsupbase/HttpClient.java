package com.example.user.eventsupbase;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by User on 26.04.2016.
 * This class is responsible for Internet querying
 */
public class HttpClient {

    private String url_address;

    public HttpClient(String url_address){
        this.url_address = url_address;
    }

    public String getAllEventsData() {
        try {
            URL url = new URL(url_address);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();

                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR_JSON_STRING", e.getMessage(), e);
            return null;
        }
    }
}

