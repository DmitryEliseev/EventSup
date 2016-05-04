package com.example.user.eventsupbase;

import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by User on 26.04.2016.
 * This class is responsible for Internet querying
 */
public class HttpClient {

    public String getAllEventsData() {
        try {
            URL url = new URL("http://diploma.welcomeru.ru/events");
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

    // This doesn't work properly
    public byte[] getEventPicture(String picture_url) {
        try {
            URL url = new URL("http://diploma.welcomeru.ru/" + picture_url);
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream is = urlConnection.getInputStream();

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while ( is.read(buffer) != -1)
                    baos.write(buffer);

                return baos.toByteArray();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR_JSON_PICTURE", e.getMessage(), e);
            return null;
        }
    }

}

