package com.example.user.eventsupbase.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.user.eventsupbase.HttpClient;
import com.example.user.eventsupbase.JsonParsing;
import com.example.user.eventsupbase.Models.Report;
import com.example.user.eventsupbase.R;

import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class VisitedReportsActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    String url_address;

    public VisitedReportsActivity(String _url_address)
    {
        url_address = _url_address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited_reports);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        new GetJsonInfo().execute(url_address);
    }
    class GetJsonInfo extends AsyncTask<String,Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VisitedReportsActivity.this);
            pDialog.setMessage("Загрузка данных");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new HttpClient(params[0]);
            return httpClient.getAllVisitedReports();
        }

        @Override
        protected void onPostExecute(String response) {
            pDialog.dismiss();
            switch (response) {
                case "-1":
                    Toast.makeText(getApplicationContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
                    break;
                case "0":
                    Toast.makeText(getApplicationContext(), "There was an unexpected mistake!", Toast.LENGTH_LONG).show();
                    break;
                default:
                    JsonParsing parsing = new JsonParsing();
                    List<Report> reports = parsing.GetReportsFromJsonString(response);
                    //TODO: обновление интерфейса
            }
        }
    }
}
