package com.example.user.eventsupbase;

import android.content.Context;
import android.util.Log;

import com.example.user.eventsupbase.Models.Event;
import com.example.user.eventsupbase.Models.Report;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 26.04.2016.
 * This class is responsible for parsing input json string
 */
public class JsonParsing {

    final String TAG = "MY_LOG";
    final String FILE_NAME = "data.json";


    public List<Event> GetEventFromJsonString(String response) {
        try {
            JSONArray reader = new JSONArray(response);
            List<Event> events = new ArrayList<>();

            for (int k = 0; k < reader.length(); k++) {
                JSONObject dataJson = reader.getJSONObject(k);
                Event event = new Event();
                event.id_event = dataJson.getString("id_event");
                event.event_name = dataJson.getString("event_name");
                event.date_start = dataJson.getString("date_start");
                event.date_finish = dataJson.getString("date_finish");
                event.event_address = dataJson.getString("event_address");

                List<Report> reports = new ArrayList<>();

                JSONArray report_arr = dataJson.getJSONArray("report");
                for (int i = 0; i < report_arr.length(); i++) {
                    Report report = new Report();

                    JSONObject report_ob = report_arr.getJSONObject(i);
                    report.id_report = report_ob.getString("id_report");
                    report.report_name = report_ob.getString("report_name");
                    report.time = report_ob.getString("time");
                    report.report_address = report_ob.getString("report_address");
                    report.lecture_hall = report_ob.getString("lecture_hall");
                    report.description = report_ob.getString("description");
                    report.document = report_ob.getString("doc");
                    report.authors = new ArrayList<>();

                    JSONArray author = report_ob.getJSONArray("author");
                    for (int j = 0; j < author.length(); j++) {
                        JSONObject author_ob = author.getJSONObject(j);
                        report.authors.add(author_ob.getString("author_name"));
                    }
                    reports.add(report);
                }
                event.reports = reports;
                events.add(event);
            }
            return events;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public List<Report> GetReportsFromJsonString(String response) {
        try {
            JSONArray reader = new JSONArray(response);
            List<Report> reports = new ArrayList<>();

            for (int i = 0; i < reader.length(); i++) {
                Report report = new Report();
                JSONObject report_ob = reader.getJSONObject(i);
                report.id_report = report_ob.getString("id_report");
                report.report_name = report_ob.getString("report_name");
                report.time = report_ob.getString("time");
                report.report_address = report_ob.getString("report_address");
                report.lecture_hall = report_ob.getString("lecture_hall");
                report.description = report_ob.getString("description");
                report.document = report_ob.getString("doc");
                report.authors = new ArrayList<>();
                reports.add(report);
            }
            return reports;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public void WriteDataToFile(String response, Context c) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(c.openFileOutput(FILE_NAME, c.MODE_PRIVATE)));
            bw.write(response);
            bw.close();
            Log.d(TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public String ReadDataFromFile(Context c) {
        String response = "";
        String reader = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(c.openFileInput(FILE_NAME)));
            while ((reader = br.readLine()) != null) {
                response += reader;
            }
            br.close();
            return response;
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }
}
