package com.example.ssas;

import java.util.List;

public class Course {
    private String courseID;
    private String courseName;
    private Teacher tutor;
    private String semester;
    private List<Class> classes;

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

    public Teacher getTutor() {
        return tutor;
    }

    public void setTutor(Teacher tutor) {
        this.tutor = tutor;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }
}
