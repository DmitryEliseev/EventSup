package com.example.user.eventsupbase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.eventsupbase.Models.Event;

import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class EventActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    Intent intent;
    int[] colors = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        colors[0] = Color.parseColor("#559966CC");
        colors[1] = Color.parseColor("#55336699");

        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

        intent = getIntent();
        ShowAllEvents((List<Event>)intent.getSerializableExtra("Events"));
    }

    public void ShowAllEvents (List<Event> events){
        LayoutInflater layoutInflater = getLayoutInflater();

        for (int i = 0; i<events.size(); i++){
            GridLayout gridLayout = (GridLayout)layoutInflater.inflate(R.layout.event_item, linearLayout, false);
            TextView event_name = (TextView)gridLayout.findViewById(R.id.event_name);
            TextView event_date = (TextView) gridLayout.findViewById(R.id.event_date);
            TextView event_address = (TextView)gridLayout.findViewById(R.id.event_address);
            ImageView event_picture = (ImageView)gridLayout.findViewById(R.id.event_picture);
            event_name.setText((events.get(i).event_name));
            event_date.setText((events.get(i).date_start + " — " + events.get(i).date_finish));
            event_address.setText((events.get(i).event_address));

//            if(events.get(i).picture!=null && events.get(i).picture.length>0){
//                Bitmap bmp = BitmapFactory.decodeByteArray(events.get(i).picture, 0, events.get(i).picture.length);
//                event_picture.setImageBitmap(bmp);
//            }

            gridLayout.setBackgroundColor(colors[i%2]);
            linearLayout.addView(gridLayout);
        }
    }
}
