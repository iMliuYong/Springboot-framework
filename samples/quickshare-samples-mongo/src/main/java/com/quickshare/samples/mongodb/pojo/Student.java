package com.quickshare.samples.mongodb.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 学生
 * @author liu_ke
 */
public class Student implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private int age;
    private List<Course> courses;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
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
}
