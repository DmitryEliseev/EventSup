package com.example.user.eventsupbase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.user.eventsupbase.Models.DataStorage;
import com.example.user.eventsupbase.Models.Event;

import java.io.Serializable;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity {

    List<Event> events;
    Intent intent;
    String TAG = "MY_LOG";
    ProgressDialog pDialog;

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

    class GetJsonInfo extends AsyncTask<Void, Void, DataStorage> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Загрузка даных....");
            pDialog.show();
        }

        @Override
        protected DataStorage doInBackground(Void... params) {
            HttpClient httpClient = new HttpClient();
            return httpClient.getAllEventsData();
        }

        protected void onPostExecute(DataStorage response) {
            if (response == null) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error or no Internet connection!", Toast.LENGTH_LONG).show();
            } else {
                pDialog.dismiss();
                JsonParsing parsing = new JsonParsing();
                events = parsing.GetEventFromJsonString(response);
                intent.putExtra("Events", (Serializable) events);
                // TODO: настроить флаги intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent);

            }
        }
    }
}
