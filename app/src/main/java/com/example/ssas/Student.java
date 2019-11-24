package com.example.ssas;

public class Student {
    private Record attendanceRecord;
    private String studentID;
    private String studentName;

    public Record getAttendanceRecord() {
        return attendanceRecord;
    }

    public void setAttendanceRecord(Record attendanceRecord) {
        this.attendanceRecord = attendanceRecord;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
