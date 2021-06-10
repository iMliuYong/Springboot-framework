package com.quickshare.samples.restapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liu_ke
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping
    public Object test(){
        return "测试一下";
    }
}
