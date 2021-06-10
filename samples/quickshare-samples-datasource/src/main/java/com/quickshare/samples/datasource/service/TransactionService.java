package com.quickshare.samples.datasource.service;

import com.quickshare.framework.datasource.DataSource;
import com.quickshare.samples.datasource.mapper.TestMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liu_ke
 */
@Service
public class TransactionService {

    private final TestMapper testMapper;

    public TransactionService(TestMapper testMapper){
        this.testMapper = testMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @DataSource("db1")
    public void save1(String col1) throws Exception {
        testMapper.add(col1);
        throw new Exception("抛错");
    }
}
