package com.example.ssas;

import java.util.List;

public class Teacher {
    private String id;
    private String name;
    private String password;
    private List<Course> courses;
    private List<String> colleges;
    private String email;

    public boolean addCollege(String collegeName)
    {
        if(!colleges.contains(collegeName))
        {
            colleges.add(collegeName);
            return true;
        }
        else
            return false;
    }

    public boolean deleteCollege(String collegeName)
    {
        if(colleges.contains(collegeName))
        {
            colleges.remove(collegeName);
            return true;
        }
        else
            return false;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<String> getColleges() {
        return colleges;
    }

    public void setColleges(List<String> colleges) {
        this.colleges = colleges;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
