package com.quickshare.samples.datasource.service;

import com.quickshare.framework.datasource.DataSource;
import com.quickshare.samples.datasource.bean.TestTable;
import com.quickshare.samples.datasource.mapper.TestMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 多数据源连接
 * @author liu_ke
 */
@Service
public class TestMultiService {

    private final TestMapper testMapper;
    private final TransactionService transactionService;
    final private PlatformTransactionManager txManager;

    public TestMultiService(TestMapper testMapper,
                            TransactionService transactionService,
                            PlatformTransactionManager txManager){
        this.testMapper = testMapper;
        this.transactionService = transactionService;
        this.txManager = txManager;
    }

    @DataSource("db1")
    public Object query1(){
        return testMapper.query();
    }

    @Transactional(rollbackFor = Exception.class)
    @DataSource("db1")
    public Object save1(String col1){
        Object result = testMapper.query();
        testMapper.add(col1);
        return result;
    }

    @DataSource("db1")
    public Object save2(String col1){
        Object result = testMapper.query();
        try {
            saveData(col1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @DataSource("db1")
    public void saveData(String col1) throws Exception {
        testMapper.add(col1);
        throw new Exception("报错");
    }

    @DataSource("db1")
    public Object save3(String col1){
        Object result = testMapper.query();
        try {
            transactionService.save1(col1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @DataSource("db1")
    public Object save4(String col1){
        Object result = testMapper.query();

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = txManager.getTransaction(def);
        testMapper.add(col1);
        txManager.rollback(status);
        //txManager.commit(status);
        return result;
    }

    @DataSource("db2")
    public Object query2(){
        return testMapper.query();
    }
}
