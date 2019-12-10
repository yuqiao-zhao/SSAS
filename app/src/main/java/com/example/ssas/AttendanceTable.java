package com.example.ssas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AttendanceTable {
    private Date classTime;
    private String status;
    private static SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm");

    public Date getClassTime() {
        return classTime;
    }

    public void setClassTime(Date classTime) {
        this.classTime = classTime;
    }

    public String getStartTimeInString()
    {
        return ft.format(getClassTime());
    }

    public static Date StrToDate(String str)
    {
        Date date = null;
        try {
            date = ft.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
