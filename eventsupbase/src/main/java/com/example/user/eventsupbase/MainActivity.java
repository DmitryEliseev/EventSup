package com.example.user.eventsupbase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.user.eventsupbase.Models.Event;
import com.example.user.eventsupbase.QR.IntentIntegrator;
import com.example.user.eventsupbase.QR.IntentResult;

import java.io.Serializable;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity {

    List<Event> events;
    Intent intent;
    ProgressDialog pDialog;
    String url_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        intent = new Intent(this, EventActivity.class);
    }

    public void OnBeginButtonClick(View v) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if ((scanResult != null)&&(scanResult.getContents()!=null)){
            url_address = "http://diploma.welcomeru.ru/" + scanResult.getContents();
            new GetJsonInfo().execute(url_address);
        }
        else Toast.makeText(this, "Чтение QR кода не было произведено!", Toast.LENGTH_LONG).show();
    }
}
