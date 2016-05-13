package com.example.user.eventsupbase.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.eventsupbase.Models.Report;
import com.example.user.eventsupbase.R;

import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class ConcreteReportActivity extends AppCompatActivity {

    Intent intent;
    Report report;
    TextView c_report_title, c_report_date, c_report_address, c_report_description, c_report_pdfdoc;
    String event_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concrete_report);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        c_report_title = (TextView) findViewById(R.id.c_report_title);
        c_report_date = (TextView) findViewById(R.id.c_report_date);
        c_report_address = (TextView) findViewById(R.id.c_report_address);
        c_report_description = (TextView) findViewById(R.id.c_report_description);
        c_report_pdfdoc = (TextView) findViewById(R.id.c_report_pdfdoc);

        intent = getIntent();
        List<Report> reports = (List<Report>) intent.getSerializableExtra("Reports");
        int id = intent.getIntExtra("ReportID", -1);

        report = reports.get(id);
        event_address = intent.getStringExtra("EventAddress");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ShowConcreteReport(report);
            }
        }).start();
    }

    private void ShowConcreteReport(Report report) {

        c_report_title.setText(report.report_name);
        c_report_date.setText(report.time.substring(0, report.time.length() - 3));

        String address = "";
        if (report.report_address.equals("null"))
            address = event_address + ", аудитория № " + report.lecture_hall;
        else address = report.report_address + ", aудитория №" + report.lecture_hall;
        c_report_address.setText(address);

        c_report_description.setText(report.description);

        //Присваивание TextView ссылки
        String url_address = "<a href = http://diploma.welcomeru.ru/" + report.document + "> Обзор статьи";
        c_report_pdfdoc.setText(Html.fromHtml(url_address));
        c_report_pdfdoc.setMovementMethod(LinkMovementMethod.getInstance());
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
