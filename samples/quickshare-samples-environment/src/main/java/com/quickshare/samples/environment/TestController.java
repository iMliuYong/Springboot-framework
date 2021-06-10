package com.quickshare.samples.environment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liu_ke
 */
@RestController
@RequestMapping("test")
public class TestController {

    private final TestMapper testMapper;

    public TestController(TestMapper testMapper){
        this.testMapper = testMapper;
    }

    @Value("${key1:}")
    private String key1;

    @GetMapping
    public Object get(){
        return testMapper.lstHospitals();
    }
}
