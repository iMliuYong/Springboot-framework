package com.quickshare.framework.datasource.provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.ibatis.mapping.DatabaseIdProvider;

/**
 * 自定义一个数据库标示提供类，实现DatabaseIdProvider接口
 * @author liu_ke
 */
public class MyDatabaseIdProvider implements DatabaseIdProvider {
    private static final String DATABASE_MYSQL = "MySQL";
    private static final String DATABASE_ORACLE = "Oracle";
    private static final String DATABASE_SQLSERVER = "Microsoft SQL Server";

    @Override
    public void setProperties(Properties p) {
        //System.out.println(p.getProperty("Oracle"));
    }

    @Override
    public String getDatabaseId(javax.sql.DataSource dataSource) throws SQLException {
        Connection conn = dataSource.getConnection();
        String dbName = conn.getMetaData().getDatabaseProductName();
        String dbAlias = "";
        switch (dbName) {
        case DATABASE_MYSQL:
            dbAlias = "mysql";
            break;
        case DATABASE_SQLSERVER:
            dbAlias = "sqlserver";
            break;
        case DATABASE_ORACLE:
            dbAlias = "oracle";
            break;
        default:
            break;
        }
        return dbAlias;
    }
}