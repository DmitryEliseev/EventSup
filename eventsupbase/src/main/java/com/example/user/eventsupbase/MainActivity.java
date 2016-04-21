package com.example.user.eventsupbase;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText eventID;
    TextView responseView;
    ProgressBar progressBar;
    String eventid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseView = (TextView) findViewById(R.id.responseView);
        eventID = (EditText) findViewById(R.id.eventID);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button btnQuery = (Button) findViewById(R.id.queryButton);
        btnQuery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                eventid = eventID.getText().toString();
                new GetJsonInfo().execute();
            }
        });
    }

    class GetJsonInfo extends AsyncTask<Void, Void, String> {

        protected void OnPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("http://diploma.welcomeru.ru/" + eventid);
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
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            progressBar.setVisibility(View.GONE);

            if (response == null)
                responseView.setText("THERE WAS AN ERROR");

            if (response.length() <= 3)
                responseView.setText("THERE IS NO SUCH EVENT");
            else {
                responseView.setText(response);

                try {
                    JSONArray reader = new JSONArray(response);
                    JSONObject dataJson = reader.getJSONObject(0);
                    String id_event = dataJson.getString("id_event");
                    String event_name = dataJson.getString("event_name");
                    String data_start = dataJson.getString("date_start");
                    String data_finish = dataJson.getString("date_finish");
                    String picture = dataJson.getString("picture");
                    JSONArray report_arr = dataJson.getJSONArray("report");
                    for (int i = 0; i<report_arr.length(); i++){
                        JSONObject report_ob = report_arr.getJSONObject(i);
                        String id_report = report_ob.getString("id_report");
                        String report_name = report_ob.getString("report_name");
                        String time = report_ob.getString("time");
                        String address = report_ob.getString("address");
                        String lecture_hall = report_ob.getString("lecture_hall");
                        String description = report_ob.getString("description");
                        JSONArray authors = report_ob.getJSONArray("author");
                        for (int j = 0; j<authors.length(); j++){
                            JSONObject author_ob = authors.getJSONObject(j);
                            String author = author_ob.getString("author_name");
                        }
                    }
                } catch (JSONException e) {
                    Log.e("JSON_ERROR", "Something goes wrong here");
                }
            }

        }
    }
}
