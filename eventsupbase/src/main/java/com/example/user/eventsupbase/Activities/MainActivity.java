package com.example.user.eventsupbase.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.eventsupbase.HttpClient;
import com.example.user.eventsupbase.JsonParsing;
import com.example.user.eventsupbase.Models.Event;
import com.example.user.eventsupbase.QrReaderIntegrator.IntentIntegrator;
import com.example.user.eventsupbase.QrReaderIntegrator.IntentResult;
import com.example.user.eventsupbase.R;

import java.io.Serializable;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity {

    List<Event> events;
    Intent intent, intent2;
    ProgressDialog pDialog;
    String url_address_one_event;
    final String url_address_all_events = "http://diploma.welcomeru.ru/events";
    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        intent2 = new Intent(this, EventActivity.class);

        intent = getIntent();
        String notion = intent.getStringExtra("Status");

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_coordLayout);

        snackbar = Snackbar.make(coordinatorLayout, notion, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        snackbar.show();
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
            return httpClient.getData();
        }

        protected void onPostExecute(String response) {
            pDialog.dismiss();
            switch (response) {
                case "-3":
                    Toast.makeText(getApplicationContext(), "Такого события нет", Toast.LENGTH_LONG).show();
                    break;
                case "-2":
                    Toast.makeText(getApplicationContext(), "Нет соединения с интернетом", Toast.LENGTH_LONG).show();
                    break;
                case "0":
                    Toast.makeText(getApplicationContext(), "Ошибка:( Попробуйте снова!", Toast.LENGTH_LONG).show();
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
