package com.example.user.eventsupbase;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

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

                String response = stringBuilder.toString();
                if(response.length()<5)
                    return "-2";
                else
                    return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        }
        catch (FileNotFoundException e) {
            return "-2";
        } catch (UnknownHostException e){
            return  "-1";
        } catch (Exception e){
            return "0";
        }
    }
}

