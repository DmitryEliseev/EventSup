package com.example.user.eventsupbase.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.eventsupbase.Models.Report;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        intent2 = new Intent(this, ConcreteReportActivity.class);

        colors[0] = Color.parseColor("#c9dcff");
        colors[1] = Color.parseColor("#acc9ff");

        baseLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);

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
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.report_item, baseLinearLayout, false);
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
//                Intent intent = new Intent(this, HomeActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                finish();
                return true;
            case R.id.action_visited:
                Toast.makeText(getApplicationContext(), "Super!", Toast.LENGTH_SHORT).show();
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
