package com.quickshare.samples.datasource.controller;

import com.quickshare.samples.datasource.service.TestMultiService;
import com.quickshare.samples.datasource.service.TestOnClassService;
import com.quickshare.samples.datasource.service.TestOnMethodService;
import org.springframework.web.bind.annotation.*;

/**
 * @author liu_ke
 */
@RestController
@RequestMapping("test")
public class TestController {

    private final TestOnMethodService testOnMethodService;
    private final TestOnClassService testOnClassService;
    private final TestMultiService testMultiService;

    public TestController(TestOnMethodService testOnMethodService,
                          TestOnClassService testOnClassService,
                          TestMultiService testMultiService){
        this.testOnMethodService = testOnMethodService;
        this.testOnClassService = testOnClassService;
        this.testMultiService = testMultiService;
    }

    @PostMapping
    public Object add(@RequestParam("col1")String col1){
        return testOnMethodService.add(col1);
    }

    @GetMapping
    public Object query(){
        return testOnMethodService.query();
    }

    @PutMapping
    public Object addTran(@RequestParam("col1")String col1) throws Exception {
        return testOnMethodService.add2(col1);
    }

    @PostMapping("class")
    public Object add2(@RequestParam("col1")String col1){
        return testOnClassService.add(col1);
    }

    @GetMapping("class")
    public Object query2(){
        return testOnClassService.query();
    }

    @PutMapping("class")
    public Object addTran2(@RequestParam("col1")String col1) throws Exception {
        return testOnClassService.add2(col1);
    }

    @GetMapping("db1")
    public Object queryMulti1(){
        return testMultiService.query1();
    }

    @PostMapping("db1")
    public Object addDb1(@RequestParam("col1")String col1){
        return testMultiService.save1(col1);
    }

    @PostMapping("db1/tran1")
    public Object addDb2(@RequestParam("col1")String col1){
        return testMultiService.save2(col1);
    }

    @PostMapping("db1/tran2")
    public Object addDb3(@RequestParam("col1")String col1){
        return testMultiService.save3(col1);
    }

    @PostMapping("db1/tran3")
    public Object addDb4(@RequestParam("col1")String col1){
        return testMultiService.save4(col1);
    }

    @GetMapping("db2")
    public Object queryMulti2(){
        return testMultiService.query2();
    }
}
