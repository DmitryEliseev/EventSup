package com.example.user.eventsupbase.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.eventsupbase.Models.Event;
import com.example.user.eventsupbase.Models.User;
import com.example.user.eventsupbase.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class EventActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    Intent intent1, intent2;
    int[] colors = new int[2];
    String TAG = "MY_LOG";
    List<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        intent2 = new Intent(this, ReportActivity.class);

        colors[0] = Color.parseColor("#eeeeee");
        colors[1] = Color.parseColor("#dbdbdb");

        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

        intent1 = getIntent();
        events = (List<Event>)intent1.getSerializableExtra("Events");
        ShowAllEvents(events);
    }

    public void ShowAllEvents (List<Event> events){
        LayoutInflater layoutInflater = getLayoutInflater();

        for (int i = 0; i<events.size(); i++){
            GridLayout gridLayout = (GridLayout)layoutInflater.inflate(R.layout.item_event, linearLayout, false);
            gridLayout.setId(i);
            gridLayout.setBackgroundColor(colors[1]);
            TextView event_name = (TextView)gridLayout.findViewById(R.id.event_name);
            TextView event_date = (TextView) gridLayout.findViewById(R.id.event_date);
            TextView event_address = (TextView)gridLayout.findViewById(R.id.event_address);
            TextView status = (TextView)gridLayout.findViewById(R.id.status);
            status.setVisibility(View.GONE);

            event_name.setText((events.get(i).event_name));
            event_date.setText((events.get(i).date_start + " — " + events.get(i).date_finish));
            event_address.setText((events.get(i).event_address));

            //Изменение отражения события, если оно уже прошло
            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern("yyyy-MM-dd");
            try {
                Date date_finish = format.parse(events.get(i).date_finish);
                if (date_finish.before(Calendar.getInstance().getTime())) {
                    gridLayout.setBackgroundColor(colors[0]);
                    event_name.setTextColor(Color.parseColor("#a9a9a9"));
                    event_date.setTextColor(Color.parseColor("#a9a9a9"));
                    event_address.setTextColor(Color.parseColor("#a9a9a9"));
                    status.setVisibility(View.VISIBLE);
                }
            }
            catch(Exception e)
            {
                //TODO: реализовать обработку исключения
            }
            linearLayout.addView(gridLayout);
        }
    }

    public void onGridClick (View v)
    {
        int id = v.getId();
        intent2.putExtra("Reports", (Serializable) events.get(id).reports);
        intent2.putExtra("EventAddress", events.get(id).event_address);
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

}

