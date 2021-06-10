package com.quickshare.samples.mongodb.dao;

import com.quickshare.samples.mongodb.mongo.MongoBase;
import com.quickshare.samples.mongodb.pojo.Student;

import java.util.List;

/**
 * @author liu_ke
 */
public interface StudentDao extends MongoBase<Student> {

    void add(String classesId,Object student);
}