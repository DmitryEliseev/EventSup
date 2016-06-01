package com.example.user.eventsupbase.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.eventsupbase.DB.DbToken;
import com.example.user.eventsupbase.HttpClient;
import com.example.user.eventsupbase.JsonParser;
import com.example.user.eventsupbase.Models.Event;
import com.example.user.eventsupbase.Models.Token;
import com.example.user.eventsupbase.QrReaderIntegrator.IntentIntegrator;
import com.example.user.eventsupbase.QrReaderIntegrator.IntentResult;
import com.example.user.eventsupbase.R;

import java.io.Serializable;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity {

    List<Event> events;
    Intent intent1, intent2;
    ProgressDialog pDialog;
    String url_address_one_event = null;
    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar = null;
    JsonParser parser;

    final String url_address_all_events = "http://diploma.welcomeru.ru/events";
    final String TAG = "MY_LOG";
    public static final String EVENTS = "Events";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        intent1 = getIntent();
        String status = intent1.getStringExtra(SplashActivity.AUTH_STATUS);

        intent2 = new Intent(this, EventActivity.class);
        parser = new JsonParser();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordLayout);
        snackbar = Snackbar.make(coordinatorLayout, status, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        snackbar.show();
    }

    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btnScanQrCode:
                if (HttpClient.hasConnection(this)) {
                    IntentIntegrator integrator = new IntentIntegrator(this);
                    integrator.initiateScan();
                } else {
                    Toast.makeText(this, "Для выполнения этого действия необходимо соединение с интернетом!", Toast.LENGTH_SHORT).show();
                }
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
            return httpClient.getOrSendData();
        }

        protected void onPostExecute(String response) {
            pDialog.dismiss();
            switch (response) {
                case "[]":
                case "\t":
                case "-3":
                    Toast.makeText(getApplicationContext(), "Такого события нет", Toast.LENGTH_SHORT).show();
                    break;
                case "0":
                    response = parser.ReadDataFromFile(getApplicationContext());
                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "Сохраненных данных пока нет, подключитесь к интернету", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    events = parser.GetEventFromJsonString(response);
                    intent2.putExtra(EVENTS, (Serializable) events);
                    startActivity(intent2);
                    break;
                default:
                    if (url_address_one_event == null)
                        parser.WriteDataToFile(response, getApplicationContext());
                    events = parser.GetEventFromJsonString(response);
                    intent2.putExtra(EVENTS, (Serializable) events);
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
            Toast.makeText(this, "Чтение QR кода не было произведено!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_visited:
                if (HttpClient.hasConnection(this)) {
                    Intent intent = new Intent(this, ReportVisitedActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Для выполнения этого действия необходимо соединение с интернетом!", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_user:
                String message = String.format("Username: %s", Token.login);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_exit:
                DbToken dbToken = null;
                try {
                    dbToken = new DbToken(this);
                    SQLiteDatabase db = dbToken.getWritableDatabase();
                    db.execSQL("DELETE FROM " + DbToken.TABLE_NAME);
                } finally {
                    dbToken.close();
                }

                Intent intent3 = new Intent(this, AuthActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
