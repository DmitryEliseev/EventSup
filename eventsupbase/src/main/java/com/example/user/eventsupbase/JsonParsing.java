package com.example.user.eventsupbase;

import android.util.Log;

import com.example.user.eventsupbase.Models.DataStorage;
import com.example.user.eventsupbase.Models.Event;
import com.example.user.eventsupbase.Models.Report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 26.04.2016.
 * This class is responsible for parsing input json string
 */
public class JsonParsing {

    public List<Event> GetEventFromJsonString(DataStorage response)
    {
        try {
            JSONArray reader = new JSONArray(response.JsonResponse);
            List<Event> events = new ArrayList<>();

            for(int k = 0; k<reader.length(); k++) {
                JSONObject dataJson = reader.getJSONObject(k);
                Event event = new Event();
                event.id_event = dataJson.getString("id_event");
                event.event_name = dataJson.getString("event_name");
                event.date_start = dataJson.getString("date_start");
                event.date_finish = dataJson.getString("date_finish");
                event.picture = response.pictures.get(k);
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

                    List<String> authors = new ArrayList<>();
                    JSONArray author = report_ob.getJSONArray("author");
                    for (int j = 0; j < author.length(); j++) {
                        JSONObject author_ob = author.getJSONObject(j);
                        authors.add(author_ob.getString("author_name"));
                    }
                    report.authors = authors;
                    authors.clear();
                    reports.add(report);
                }
                event.reports = reports;
                events.add(event);
            }
            return events;
        } catch (JSONException e) {
            Log.e("MY_LOG", "JSON PARSING ERROR");
            return null;
        }
    }
}
