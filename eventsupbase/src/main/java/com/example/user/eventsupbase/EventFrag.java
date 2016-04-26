package com.example.user.eventsupbase;

import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 26.04.2016.
 */
public class EventFrag extends Fragment {
    LinearLayout event_layout;
    TextView event_title;
    TextView event_date;
    TextView event_address;
    ImageView event_picture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_frag, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        event_layout = (LinearLayout)view.findViewById(R.id.event_layout);
        event_title = (TextView)view.findViewById(R.id.event_title);
        event_date = (TextView)view.findViewById(R.id.event_date);
        event_address = (TextView)view.findViewById(R.id.event_address);
        event_picture = (ImageView)view.findViewById(R.id.event_picture);
    }

//    public static EventFrag NewInstance (List<Event> events){
//        EventFrag fragment = new EventFrag();
//        for (int i = 0; i<events.size(); i++){
//
//        }
//    }
}
