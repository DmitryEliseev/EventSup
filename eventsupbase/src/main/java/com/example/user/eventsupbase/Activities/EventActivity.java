package com.example.user.eventsupbase.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.eventsupbase.Adapters.EventAdapter;
import com.example.user.eventsupbase.DB.DbToken;
import com.example.user.eventsupbase.HttpClient;
import com.example.user.eventsupbase.Models.Event;
import com.example.user.eventsupbase.Models.Token;
import com.example.user.eventsupbase.R;

import java.io.Serializable;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class EventActivity extends AppCompatActivity {

    Intent intent1, intent2;
    List<Event> events;

    public static final String REPORTS = "Reports";
    public static final String EVENT_ADDRESS = "EventAddress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        intent1 = getIntent();
        events = (List<Event>) intent1.getSerializableExtra(MainActivity.EVENTS);
        EventAdapter adapter = new EventAdapter(this, events);
        ListView lvEvent = (ListView) findViewById(R.id.lvEvent);
        lvEvent.setAdapter(adapter);
        registerForContextMenu(lvEvent);

        intent2 = new Intent(this, ReportActivity.class);
    }

    public void onGridClick(View v) {
        int id = v.getId();
        //Проверка на полноту полученных данных
        int numberOfRep = events.get(id).reports.size();
        if(numberOfRep!=0) {
            int count = 0;
            for (int i = 0; i<numberOfRep; i++ )
                if(events.get(id).reports.get(i).authors.size()!=0)
                    count++;
            if(count==numberOfRep) {
                intent2.putExtra(REPORTS, (Serializable) events.get(id).reports);
                intent2.putExtra(EVENT_ADDRESS, events.get(id).event_address);
                startActivity(intent2);
            }
            else Toast.makeText(this, "Информация об этом событии обновляется, зайдите чуть-чуть попозже", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(this, "Информация об этом событии обновляется, зайдите чуть-чуть попозже", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_visited:
                if (HttpClient.hasConnection(this)) {
                    Intent intent = new Intent(this, ReportVisitedActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Для выполнения этого действия необходимо соединение с интернетом!", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_user:
                String message = String.format("Username: %s", Token.login);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_exit:
                DbToken dbToken = null;
                try {
                    dbToken = new DbToken(this);
                    SQLiteDatabase db = dbToken.getWritableDatabase();
                    db.execSQL("DELETE FROM " + DbToken.TABLE_NAME);
                } finally {
                    dbToken.close();
                }

                Intent intent3 = new Intent(this, AuthActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

