package com.example.user.eventsupbase;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.eventsupbase.Models.Event;

import java.io.Serializable;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity {

    List<Event> events;
    Intent intent;
    String TAG = "MY_LOG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        intent = new Intent(this, EventActivity.class);
    }

    public void OnBeginButtonClick(View v) {
        new GetJsonInfo().execute();

    }

    class GetJsonInfo extends AsyncTask<Void, Void, String> {

        protected void OnPreExecute() {

        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new HttpClient();
            return httpClient.getAllEventsData();
        }

        protected void onPostExecute(String response) {
            if (response == null)
                Log.e(TAG, "THERE WAS AN ERROR");

            else if (response.length() <= 3)
                Log.e(TAG, "THERE IS NO SUCH EVENT");
            else {
                JsonParsing parsing = new JsonParsing();
                events = parsing.GetEventFromJsonString(response);
                intent.putExtra("Events", (Serializable) events);
                // TODO: настроить флаги intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent);
            }
        }
    }
}
