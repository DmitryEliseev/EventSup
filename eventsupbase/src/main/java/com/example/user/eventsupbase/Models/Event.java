package com.example.user.eventsupbase.Models;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

public class Event implements Serializable{
    public String id_event;
    public String event_name;
    public String date_start;
    public String date_finish;
    public byte[] picture;
    public String event_address;
    public List<Report> reports;
}
