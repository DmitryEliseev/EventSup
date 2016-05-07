package com.example.user.eventsupbase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.user.eventsupbase.Models.DataStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 26.04.2016.
 * This class is responsible for Internet querying
 */
public class HttpClient {

    public DataStorage getAllEventsData() {
        DataStorage ds = new DataStorage();
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

                String response = stringBuilder.toString();
                ds.JsonResponse = response;

                List<byte[]> pictures = new ArrayList<>();

                JSONArray reader = new JSONArray(response);
                for (int k = 0; k < reader.length(); k++) {
                    JSONObject dataJson = reader.getJSONObject(k);
                    //TODO: доработать подгрузку картинки или удалить ее
//                    pictures.add(getEventPicture(dataJson.getString("picture")));
                }

                ds.pictures = pictures;
                return ds;
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR_JSON_STRING", e.getMessage(), e);
            return null;
        }
    }

    public byte[] getEventPicture(String picture_url) {
        try {
            URL url = new URL("http://diploma.welcomeru.ru/" + picture_url);
            try {
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                return stream.toByteArray();
            }
            catch (Exception e){
                Log.e("ERROR_JSON_PICTURE", e.getMessage(), e);
                return null;
            }
        } catch (Exception e) {
            Log.e("ERROR_JSON_PICTURE", e.getMessage(), e);
            return null;
        }
    }


}

