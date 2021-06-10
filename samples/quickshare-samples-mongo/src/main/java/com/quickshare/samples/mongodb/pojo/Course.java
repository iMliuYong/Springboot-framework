package com.quickshare.samples.mongodb.pojo;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 课程
 * @author liu_ke
 */
public class Course implements Serializable {

    @Id
    private String name;
    private float score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
