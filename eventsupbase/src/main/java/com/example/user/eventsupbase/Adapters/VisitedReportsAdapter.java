package com.example.user.eventsupbase.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.eventsupbase.Models.Report;
import com.example.user.eventsupbase.R;

import java.util.List;

/**
 * Created by User on 19.05.2016.
 */
public class VisitedReportsAdapter extends BaseAdapter{

    Context context;
    LayoutInflater lInflater;
    List<Report> reports;

    public VisitedReportsAdapter (Context _context, List<Report> _reports){
        context = _context;
        reports = _reports;
        lInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            view = lInflater.inflate(R.layout.item_report, parent, false);
            view.setLongClickable(true);
        }

        Report report = (Report)getItem(position);

        view.setId(position);
        TextView report_title = (TextView) view.findViewById(R.id.visited_report_title);
        TextView report_date = (TextView) view.findViewById(R.id.visited_report_date);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)report_date.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(0, 10, 0, 20);
        report_date.setLayoutParams(params);

        report_title.setText(report.report_name);
        report_date.setText(report.time.substring(0, report.time.length() - 3));

        report_title.setTextColor(Color.parseColor("#8592a9"));
        report_date.setTextColor(Color.parseColor("#8592a9"));

        return view;
    }
}
