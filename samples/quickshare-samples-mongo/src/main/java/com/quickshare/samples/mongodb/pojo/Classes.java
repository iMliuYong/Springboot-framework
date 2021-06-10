package com.quickshare.samples.mongodb.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 班级
 * @author liu_ke
 */
public class Classes implements Serializable {

    private String id;

    private String grades;

    private String no;

    private List<Student> students;

    public String getGrades() {
        return grades;
    }

    public void setGrades(String grades) {
        this.grades = grades;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
