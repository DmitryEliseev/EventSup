package com.example.user.eventsupbase;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.List;

import static android.content.pm.ActivityInfo.*;

public class MainActivity extends AppCompatActivity {

    List<Event> events;
    TextView responseView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);


        responseView = (TextView) findViewById(R.id.responseView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button btnQuery = (Button) findViewById(R.id.queryButton);
        btnQuery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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
            HttpClient httpClient = new HttpClient();
            return httpClient.getAllEventsData();
        }

        protected void onPostExecute(String response) {
            progressBar.setVisibility(View.GONE);

            if (response == null)
                responseView.setText("THERE WAS AN ERROR");

            else if (response.length() <= 3)
                responseView.setText("THERE IS NO SUCH EVENT");
            else {
                responseView.setText(response);
                JsonParsing parsing = new JsonParsing();
                events = parsing.GetEventFromJsonString(response);
            }

        }
    }
}
