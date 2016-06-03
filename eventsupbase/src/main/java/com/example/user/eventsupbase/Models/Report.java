package com.example.user.eventsupbase.Models;

import java.io.Serializable;
import java.util.List;

public class Report implements Serializable {
    public String id_report;
    public String report_name;
    public String time;
    public String report_address;
    public String lecture_hall;
    public String description;
    public String document;
    public List<String> authors;
}

