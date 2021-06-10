package com.quickshare.samples.mongodb.controller;

import com.quickshare.common.bean.web.BaseResponse;
import com.quickshare.samples.mongodb.dao.ClassesDao;
import com.quickshare.samples.mongodb.dao.StudentDao;
import com.quickshare.samples.mongodb.pojo.Classes;
import com.quickshare.samples.mongodb.pojo.Student;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liu_ke
 */
@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentDao studentDao;

    public StudentController(StudentDao studentDao){
        this.studentDao = studentDao;
    }

    /**
     * 添加班级，学生，课程分数
     * @param student
     * @return
     */
    @PostMapping
    public Object addStudent(@RequestParam("classesId")String classesId,@RequestBody Student student){
        studentDao.add(classesId,student);
        return new BaseResponse<>(0,"添加成功",true);
    }

    @GetMapping
    public Object query(String id){
        Map<String,Object> param = new HashMap<>();
        param.put("id",id);
        return studentDao.findAll(param,"classes");
    }

    @GetMapping("no")
    public Object query2(String id){
        Map<String,Object> param = new HashMap<>();
        param.put("students.id",id);
        return studentDao.findOne(param,"classes");
    }
}
