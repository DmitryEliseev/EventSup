package com.example.user.eventsupbase.Activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.eventsupbase.HttpClient;
import com.example.user.eventsupbase.JsonParsing;
import com.example.user.eventsupbase.Models.Report;
import com.example.user.eventsupbase.Models.User;
import com.example.user.eventsupbase.R;

import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class VisitedReportsActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    String url_get_all_visited_reports;
    List<Report> reports;
    LinearLayout baseLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited_reports);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        baseLinearLayout = (LinearLayout) findViewById(R.id.visited_linearLayout);

        url_get_all_visited_reports = String.format("http://diploma.welcomeru.ru/visited/%s", User.login);
        new GetJsonInfo().execute(url_get_all_visited_reports);
    }

    class GetJsonInfo extends AsyncTask<String, Void, String> {
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
            return httpClient.getData();
        }

        @Override
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
                    reports = parsing.GetReportsFromJsonString(response);
                    ShowAllReports(reports);
                    break;
            }
        }
    }

    private void ShowAllReports(List<Report> reports) {
        LayoutInflater layoutInflater = getLayoutInflater();

        for (int i = 0; i < reports.size(); i++) {
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.item_report, baseLinearLayout, false);
            linearLayout.setBackgroundColor(Color.parseColor("#c9dcff"));
            linearLayout.setId(i);
            TextView report_title = (TextView) linearLayout.findViewById(R.id.c_report_title);
            TextView report_date = (TextView) linearLayout.findViewById(R.id.report_date);
            report_date.setGravity(Gravity.CENTER_HORIZONTAL);

            TextView report_address = (TextView) linearLayout.findViewById(R.id.report_address);
            report_address.setVisibility(View.GONE);
            TextView report_authors = (TextView) linearLayout.findViewById(R.id.report_authors);
            report_authors.setVisibility(View.GONE);
            TextView report_description = (TextView) linearLayout.findViewById(R.id.report_description);
            report_description.setVisibility(View.GONE);

            report_title.setText(reports.get(i).report_name);
            report_date.setText(reports.get(i).time.substring(0, reports.get(i).time.length() - 3));

            report_title.setTextColor(Color.parseColor("#8592a9"));
            report_date.setTextColor(Color.parseColor("#8592a9"));

            registerForContextMenu(linearLayout);
            baseLinearLayout.addView(linearLayout);
        }
    }
}
