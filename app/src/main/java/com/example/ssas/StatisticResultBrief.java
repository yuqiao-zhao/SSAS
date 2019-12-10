package com.example.ssas;

import java.util.Date;
import java.util.List;

public class StatisticResultBrief {
    private String courseID;
    private String courseName;
    private String semester;
    private String studentName;
    private String studentId;
    private List<AttendanceTable> attendanceTables;

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }


    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<AttendanceTable> getAttendanceTables() {
        return attendanceTables;
    }

    public void setAttendanceTables(List<AttendanceTable> attendanceTables) {
        this.attendanceTables = attendanceTables;
    }
}
