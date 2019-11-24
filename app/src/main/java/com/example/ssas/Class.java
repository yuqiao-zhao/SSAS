package com.example.ssas;

import java.util.Date;
import java.util.List;

public class Class {
    private List<Record> records;
    private Date startTime;

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
