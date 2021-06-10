package com.quickshare.framework.datasource.dynamic;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import javax.sql.DataSource;

/**
 * @author liu_ke
 */
public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {

    public DynamicDataSourceTransactionManager(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        //设置数据源
        boolean readOnly = definition.isReadOnly();
        /*
        if (readOnly) {
            DynamicDataSourceHolder.putDataSource(DynamicDataSourceGlobal.READ);
        } else {
            DynamicDataSourceHolder.putDataSource(DynamicDataSourceGlobal.WRITE);
        }

         */
        super.doBegin(transaction, definition);
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        DynamicDataSourceContextHolder.clearDataSourceKey();
    }
}
