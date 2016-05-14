package com.example.user.eventsupbase.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.eventsupbase.HttpClient;
import com.example.user.eventsupbase.Models.Report;
import com.example.user.eventsupbase.Models.User;
import com.example.user.eventsupbase.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    LinearLayout baseLinearLayout;
    Intent intent, intent2;
    int[] colors = new int[2];
    String event_address;
    List<Report> reports;
    int description_max_length = 125;
    int id_event;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        intent2 = new Intent(this, ConcreteReportActivity.class);

        colors[0] = Color.parseColor("#c9dcff");
        colors[1] = Color.parseColor("#acc9ff");

        baseLinearLayout = (LinearLayout) findViewById(R.id.reports_linearLayout);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.report_coordLayout);

        intent = getIntent();
        event_address = intent.getStringExtra("EventAddress");
        reports = (List<Report>) intent.getSerializableExtra("Reports");

        new Thread(new Runnable() {
            @Override
            public void run() {
                ShowAllReports(reports);
            }
        }).start();
    }
    private void ShowAllReports(List<Report> reports) {
        LayoutInflater layoutInflater = getLayoutInflater();
        String address = "";

        for (int i = 0; i < reports.size(); i++) {
            String authors = "";
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.item_report, baseLinearLayout, false);
            linearLayout.setBackgroundColor(colors[1]);
            linearLayout.setId(i);
            TextView report_title = (TextView) linearLayout.findViewById(R.id.c_report_title);
            TextView report_date = (TextView) linearLayout.findViewById(R.id.report_date);
            TextView report_address = (TextView) linearLayout.findViewById(R.id.report_address);
            TextView report_authors = (TextView) linearLayout.findViewById(R.id.report_authors);
            TextView report_description = (TextView) linearLayout.findViewById(R.id.report_description);
            report_title.setText(reports.get(i).report_name);
            report_date.setText(reports.get(i).time.substring(0, reports.get(i).time.length() - 3));

            if (reports.get(i).report_address.equals("null"))
                address = event_address + ", aудитория № " + reports.get(i).lecture_hall;
            else
                address = reports.get(i).report_address + ", aудитория №" + reports.get(i).lecture_hall;
            report_address.setText(address);

            for (int j = 0; j < reports.get(i).authors.size() - 1; j++)
                authors += reports.get(i).authors.get(j) + ", ";
            authors += reports.get(i).authors.get(reports.get(i).authors.size() - 1);
            report_authors.setText(authors);

            String descrip = reports.get(i).description;
            if (descrip.length() < description_max_length)
                report_description.setText(descrip);
            else
                report_description.setText(descrip.substring(0, Math.min(descrip.length(), description_max_length)) + "...");

            //Изменение отражения события, если оно уже прошло
            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern("yyyy-MM-dd");
            try {
                Date date_finish = format.parse(reports.get(i).time);
                if (date_finish.before(Calendar.getInstance().getTime())) {
                    linearLayout.setBackgroundColor(colors[0]);
                    report_title.setTextColor(Color.parseColor("#8592a9"));
                    report_date.setTextColor(Color.parseColor("#8592a9"));
                    report_address.setTextColor(Color.parseColor("#8592a9"));
                    report_authors.setTextColor(Color.parseColor("#8592a9"));
                    report_description.setTextColor(Color.parseColor("#8592a9"));
                }
            } catch (Exception e) {
                //TODO: реализовать обработку исключения
            }
            registerForContextMenu(linearLayout);
            baseLinearLayout.addView(linearLayout);
        }
    }

    public void onGridClick(View v) {
        int id = v.getId();
        intent2.putExtra("Reports", (Serializable) reports);
        intent2.putExtra("ReportID", id);
        intent2.putExtra("EventAddress", event_address);
        startActivity(intent2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_visited:
                Intent intent = new Intent(this, VisitedReportsActivity.class);
//              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.action_exit:
                User.login = null;
                Intent intent3 = new Intent(this, StartActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        String text = String.format("Отметить %s доклад посещенным", v.getId()+1);
        menu.add(0, v.getId(), 0, text);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        String report_id = reports.get(item_id).id_report;
        String url_add_visited_report = String.format("http://diploma.welcomeru.ru/add/%s/%s", User.login, report_id);
        new AddingVisitedReport().execute(url_add_visited_report);
        return super.onContextItemSelected(item);
    }

    class AddingVisitedReport extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new HttpClient(params[0]);
            return httpClient.SendData();
        }

        protected void onPostExecute(String response) {
            switch (response) {
                case "-2":
                    Snackbar.make(coordinatorLayout, "Нет соединения с интернетом!", Snackbar.LENGTH_LONG).show();
                    break;
                case "-1":
                    Snackbar.make(coordinatorLayout, "Этот доклад уже отмечен вами как посещенный!", Snackbar.LENGTH_LONG).show();
                    break;
                case "0":
                    Snackbar.make(coordinatorLayout, "Ошибка:( Попробуйте снова!", Snackbar.LENGTH_LONG).show();
                    break;
                case "1":
                    Snackbar.make(coordinatorLayout, "Доклад добавлен в список посещенных", Snackbar.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    }
}
