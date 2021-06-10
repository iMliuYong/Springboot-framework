package com.quickshare.samples.mongodb.controller;

import com.quickshare.common.bean.web.BaseResponse;
import com.quickshare.framework.mongodb.MongoDataSource;
import com.quickshare.samples.mongodb.dao.StudentDaoImpl;
import com.quickshare.samples.mongodb.pojo.Student;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liu_ke
 */
@RestController
public class TestController {

    private final StudentDaoImpl studentDao;

    public TestController(StudentDaoImpl studentDao){
        this.studentDao = studentDao;
    }

    @PostMapping(value = "/test1",produces = "application/json; charset=utf-8")
    public Object add(@RequestBody Student student){
        studentDao.insert(student,"table1");
        return new BaseResponse<>(0,"添加成功",true);
    }

    @PostMapping(value = "/test2",produces = "application/json; charset=utf-8")
    @MongoDataSource("test2")
    public Object add2(@RequestBody Student student){
        studentDao.insert(student,"table1");
        return new BaseResponse<>(0,"添加成功",true);
    }
}
