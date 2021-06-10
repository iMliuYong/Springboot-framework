package com.quickshare.samples.datasource.service;


import com.quickshare.framework.datasource.DataSource;
import com.quickshare.samples.datasource.mapper.TestMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 注解在类上的方式
 * @author liu_ke
 */
@Service
@DataSource("dbsample")
public class TestOnClassService {

    private final TestMapper testMapper;

    public TestOnClassService(TestMapper testMapper){
        this.testMapper = testMapper;
    }

    public Object add(String col1){
        testMapper.add(col1);
        return "保存成功";
    }

    public Object query(){
        return testMapper.query();
    }

    //@Transactional(rollbackFor = Exception.class)
    public Object add2(String col1) throws Exception{
        testMapper.add(col1);
        throw new Exception("报错事务回滚。");
    }
}
