package com.example.user.eventsupbase.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.eventsupbase.Models.Report;
import com.example.user.eventsupbase.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 17.05.2016.
 */
public class ReportAdapter extends BaseAdapter {

    Context context;
    LayoutInflater lInflater;
    List<Report> reports;
    int description_max_length = 125;
    String event_address;
    String TAG = "MY_TAG";

    public ReportAdapter (Context _context, List<Report> _reports, String _event_address) {
        context = _context;
        reports = _reports;
        lInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        event_address = _event_address;
    }

    @Override
    public int getCount() {
        return reports.size();
    }

    @Override
    public Object getItem(int position) {
        return reports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_full_report, parent, false);
            view.setLongClickable(true);
        }

        Report report = (Report)getItem(position);

        view.setId(position);

        TextView report_title = (TextView) view.findViewById(R.id.report_title);
        report_title.setText(report.report_name);

        TextView report_date = (TextView) view.findViewById(R.id.report_date);
        report_date.setText(report.time.substring(0, report.time.length() - 3));

        String address="";
        if (report.report_address.equals("null"))
            address = event_address + ", aудитория № " + report.lecture_hall;
        else
            address = report.report_address + ", aудитория №" + report.lecture_hall;
        TextView report_address = (TextView) view.findViewById(R.id.report_address);
        report_address.setText(address);

        String authors="";
        for (int j = 0; j < report.authors.size() - 1; j++)
            authors += report.authors.get(j) + ", ";
        authors += report.authors.get(report.authors.size() - 1);
        TextView report_authors = (TextView) view.findViewById(R.id.report_authors);
        report_authors.setText(authors);

        String descrip = report.description;
        TextView report_description = (TextView) view.findViewById(R.id.report_description);
        if (descrip.length() < description_max_length)
            report_description.setText(descrip);
        else
            report_description.setText(descrip.substring(0, Math.min(descrip.length(), description_max_length)) + "...");

        TextView status = (TextView)view.findViewById(R.id.report_status);
        status.setVisibility(View.GONE);

        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd");
        try {
            Date date_finish = format.parse(report.time);
            Date current_date = EventAdapter.trim(Calendar.getInstance().getTime());
            if (date_finish.before(current_date)) {
                report_title.setTextColor(Color.parseColor("#8592a9"));
                report_date.setTextColor(Color.parseColor("#8592a9"));
                report_address.setTextColor(Color.parseColor("#8592a9"));
                report_authors.setTextColor(Color.parseColor("#8592a9"));
                report_description.setTextColor(Color.parseColor("#8592a9"));
                status.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return view;
    }
}
