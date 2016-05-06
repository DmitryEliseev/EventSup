package com.example.user.eventsupbase;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.eventsupbase.Models.Report;

import java.io.Serializable;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class ReportActivity extends AppCompatActivity {

    LinearLayout baseLinearLayout;
    Intent intent, intent2;
    int[]colors = new int[2];
    String TAG = "MY_LOG";
    List<Report> reports;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        intent2 = new Intent(this, ConcreteReportActivity.class);

        colors[0] = Color.parseColor("#c9dcff");
        colors[1] = Color.parseColor("#acc9ff");

        baseLinearLayout = (LinearLayout)findViewById(R.id.linearLayout);

        intent = getIntent();
        reports = (List<Report>)intent.getSerializableExtra("Reports");

        new Thread(new Runnable() {
            @Override
            public void run() {
                ShowAllReports(reports);
            }
        }).start();
    }

    private void ShowAllReports(List<Report> reports){
        LayoutInflater layoutInflater = getLayoutInflater();
        String address="", authors="";

        for (int i = 0; i<reports.size(); i++){
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.report_item, baseLinearLayout, false);
            linearLayout.setBackgroundColor(colors[i % 2]);
            linearLayout.setId(i);
            TextView report_title = (TextView)linearLayout.findViewById(R.id.c_report_title);
            TextView report_date = (TextView)linearLayout.findViewById(R.id.report_date);
            TextView report_address = (TextView)linearLayout.findViewById(R.id.report_address);
            TextView report_authors = (TextView)linearLayout.findViewById(R.id.report_authors);
            TextView report_description = (TextView)linearLayout.findViewById(R.id.report_description);

            report_title.setText(reports.get(i).report_name);
            report_date.setText(reports.get(i).time);

            //не работает корректно отбор
            if(reports.get(i).report_address!="null")
                address = reports.get(i).report_address+". Аудитория № "+reports.get(i).lecture_hall;
            else
                address = "Аудитория №"+reports.get(i).lecture_hall;
            report_address.setText(address);

            for(int j = 0; j<reports.get(i).authors.size()-1;j++)
                authors+=reports.get(i).authors.get(j)+", ";
            authors+=reports.get(i).authors.get(reports.get(i).authors.size()-1);
            report_authors.setText(authors);

            String descrip = reports.get(i).description;
            report_description.setText(descrip.substring(0, Math.min(descrip.length(), 200))+"...");
            baseLinearLayout.addView(linearLayout);
        }
    }

    public void onGridClick (View v)
    {
        int id = v.getId();
        intent2.putExtra("Reports", (Serializable) reports);
        intent2.putExtra("ReportID", id);
        startActivity(intent2);
    }
}
