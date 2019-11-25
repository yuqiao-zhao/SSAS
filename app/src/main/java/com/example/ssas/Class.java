package com.example.ssas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Class {
    private List<Record> records = new ArrayList<>();
    private Date startTime;
    private SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm");

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public String getStartTimeInString()
    {
        return ft.format(getStartTime());
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
