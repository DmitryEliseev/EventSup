package com.example.user.eventsupbase.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.eventsupbase.Models.Event;
import com.example.user.eventsupbase.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 19.05.2016.
 */
public class EventAdapter extends BaseAdapter {

    Context context;
    LayoutInflater lInflater;
    List<Event> events;
    String TAG = "MY_LOG";

    public EventAdapter(Context _context, List<Event> _events){
        context = _context;
        events = _events;
        lInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = lInflater.inflate(R.layout.item_event, parent, false);
        }

        Event event = (Event)getItem(position);
        view.setId(position);

        TextView event_name = (TextView)view.findViewById(R.id.event_name);
        TextView event_date = (TextView) view.findViewById(R.id.event_date);
        TextView event_address = (TextView)view.findViewById(R.id.event_address);
        TextView status = (TextView)view.findViewById(R.id.event_status);
        status.setVisibility(View.GONE);

        event_name.setText(event.event_name);
        event_date.setText(event.date_start + " — " + event.date_finish);
        event_address.setText(event.event_address);

        //Изменение отражения события, если оно уже прошло
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd");
        try {
            Date date_finish = format.parse(event.date_finish);
            Date current_date = trim(Calendar.getInstance().getTime());
            if (date_finish.before(current_date)) {
                event_name.setTextColor(Color.parseColor("#a9a9a9"));
                event_date.setTextColor(Color.parseColor("#a9a9a9"));
                event_address.setTextColor(Color.parseColor("#a9a9a9"));
                status.setVisibility(View.VISIBLE);
            }
        }
        catch(Exception e){
            Log.e(TAG, e.getMessage());
        }

        return view;
    }

    static public Date trim(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        return calendar.getTime();
    }

}
