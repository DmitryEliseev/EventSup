package com.example.user.eventsupbase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.eventsupbase.Models.Report;

import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class ConcreteReportActivity extends AppCompatActivity {

    Intent intent;
    Report report;
    TextView c_report_title, c_report_date, c_report_address,c_report_description,c_report_pdfdoc;
    ImageView c_report_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concrete_report);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        c_report_picture = (ImageView)findViewById(R.id.c_report_picture);
        c_report_title = (TextView)findViewById(R.id.c_report_title);
        c_report_date = (TextView)findViewById(R.id.c_report_date);
        c_report_address = (TextView)findViewById(R.id.c_report_address);
        c_report_description = (TextView)findViewById(R.id.c_report_description);
        c_report_pdfdoc = (TextView)findViewById(R.id.c_report_pdfdoc);

        intent = getIntent();
        List<Report> reports = (List<Report>) intent.getSerializableExtra("Reports");
        report = reports.get(Integer.parseInt(intent.getStringExtra("ReportID")));

        new Thread(new Runnable() {
            @Override
            public void run() {
                ShowConcreteReport(report);
            }
        }).start();
    }

    private void ShowConcreteReport(Report report){

        c_report_title.setText(report.report_name);
        c_report_date.setText(report.time);

        //не работает корректно отбор
        String address = "";
        if(report.report_address!="null")
            address = report.report_address+". Аудитория № "+report.lecture_hall;
        else
            address = "Аудитория №"+report.lecture_hall;
        c_report_address.setText(address);
        c_report_description.setText(report.description);
        c_report_pdfdoc.setText("diploma.welcomeru.ru"+report.document);
    }
}
