package com.example.user.eventsupbase.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.eventsupbase.HttpClient;
import com.example.user.eventsupbase.JsonParsing;
import com.example.user.eventsupbase.Models.Event;
import com.example.user.eventsupbase.QrReaderIntegrator.IntentIntegrator;
import com.example.user.eventsupbase.QrReaderIntegrator.IntentResult;
import com.example.user.eventsupbase.R;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity {

    List<Event> events;
    Intent intent, intent2;
    ProgressDialog pDialog;
    String url_address_one_event;
    final String url_address_all_events = "http://diploma.welcomeru.ru/events";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        intent2 = new Intent(this, EventActivity.class);

        intent = getIntent();
        TextView twStatus = (TextView)findViewById(R.id.twStatus);
        twStatus.setText(intent.getStringExtra("Status"));
    }

    public void OnClick(View v) {
        switch (v.getId()){
            case R.id.btnScanQrCode:
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.initiateScan();
                break;
            case R.id.btnAllEvents:
                new GetJsonInfo().execute(url_address_all_events);
                break;
        }
    }

    class GetJsonInfo extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Загрузка данных....");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new HttpClient(params[0]);
            return httpClient.getAllEventsData();
        }

        protected void onPostExecute(String response) {
            pDialog.dismiss();
            switch (response) {
                case "-2":
                    Toast.makeText(getApplicationContext(), "Such event was not found!", Toast.LENGTH_LONG).show();
                    break;
                case "-1":
                    Toast.makeText(getApplicationContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
                    break;
                case "0":
                    Toast.makeText(getApplicationContext(), "There was an unexpected mistake!", Toast.LENGTH_LONG).show();
                    break;
                default:
                    JsonParsing parsing = new JsonParsing();
                    events = parsing.GetEventFromJsonString(response);
                    intent2.putExtra("Events", (Serializable) events);
                    // TODO: настроить флаги intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent2);
                    break;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult.getContents() != null) {
            url_address_one_event = "http://diploma.welcomeru.ru/" + scanResult.getContents();
            new GetJsonInfo().execute(url_address_one_event);
        } else
            Toast.makeText(this, "Чтение QR кода не было произведено!", Toast.LENGTH_LONG).show();
    }
}
