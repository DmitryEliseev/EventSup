package com.example.user.eventsupbase.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.eventsupbase.Adapters.EventAdapter;
import com.example.user.eventsupbase.Models.Event;
import com.example.user.eventsupbase.Models.User;
import com.example.user.eventsupbase.R;

import java.io.Serializable;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class EventActivity extends AppCompatActivity {

    Intent intent1, intent2;
    List<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        intent2 = new Intent(this, ReportActivity.class);

        intent1 = getIntent();
        events = (List<Event>)intent1.getSerializableExtra("Events");

        EventAdapter adapter = new EventAdapter(this, events);
        ListView lvEvent = (ListView)findViewById(R.id.lvEvent);
        lvEvent.setAdapter(adapter);
        registerForContextMenu(lvEvent);
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
            case R.id.action_user:
                String message = String.format("Username: %s", User.login);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_visited:
                if(User.login == null){
                    Toast.makeText(getApplicationContext(), "Выйдите и водите в систему снова!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(this, VisitedReportsActivity.class);
                    startActivity(intent);
                }
                return true;
            case R.id.action_exit:
                User.login = null;
                Intent intent3 = new Intent(this, StartActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

