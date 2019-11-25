package com.example.ssas;

import java.util.Date;
import java.util.List;

public class Record {
    private long recordID;
    private String teacherID;
    private Date signInTime;
    private Student registeredStudent;
    private String status = "Present";

    public long getRecordID() {
        return recordID;
    }

    public void setRecordID(long recordID) {
        this.recordID = recordID;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public Date getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(Date signInTime) {
        this.signInTime = signInTime;
    }

    public Student getRegisteredStudent() {
        return registeredStudent;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRegisteredStudent(Student registeredStudent) {
        this.registeredStudent = registeredStudent;
    }
}
