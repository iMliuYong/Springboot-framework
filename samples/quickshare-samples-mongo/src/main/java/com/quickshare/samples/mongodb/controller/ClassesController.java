package com.quickshare.samples.mongodb.controller;

import com.quickshare.common.bean.web.BaseResponse;
import com.quickshare.samples.mongodb.dao.ClassesDao;
import com.quickshare.samples.mongodb.pojo.Classes;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liu_ke
 */
@RestController
@RequestMapping("classes")
public class ClassesController {

    private final ClassesDao classesDao;

    public ClassesController(ClassesDao classesDao){
        this.classesDao = classesDao;
    }

    /**
     * 添加班级，学生，课程分数
     * @param classes
     * @return
     */
    @PostMapping
    public Object addClasses(@RequestBody Classes classes){
        classesDao.insert(classes,"classes");
        return new BaseResponse<>(0,"添加成功",true);
    }

    @GetMapping
    public Object getClasses(String id){
        Map<String,Object> param = new HashMap<>();
        param.put("id",id);
        return classesDao.findAll(param,"classes");
    }

    @PutMapping
    public Object update(@RequestBody Classes classes){
        Map<String,Object> param = new HashMap<>();
        param.put("id",classes.getId());
        param.put("grades",classes.getGrades());
        param.put("no",classes.getNo());
        classesDao.update(param,"classes");
        return new BaseResponse<>(0,"更新成功",true);
    }
}
