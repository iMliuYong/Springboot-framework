package com.quickshare.samples.json.controller;

import com.quickshare.samples.json.dto.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liu_ke
 */
@RestController
@RequestMapping("/")
public class TestController {

    @GetMapping
    public Object getStudent(){
        Student student = new Student();
        student.setId("123");
        student.setName("美女");
        student.setSex("girl");
        return student;
    }
}
