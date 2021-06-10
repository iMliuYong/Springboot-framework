package com.quickshare.framework.datasource;

import com.quickshare.common.encrytor.DesUtils;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * @author liu_ke
 */
public class DataSourceUtils {

    private final static String key = "quckshre";

    public static DataSource createDataSource(String url, String username, String pwd, String driverclassname){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        try{
            pwd = DesUtils.decrypt(pwd,key);
        } catch (Exception e) {
            // do nothing
        }
        dataSource.setPassword(pwd);
        dataSource.setDriverClassName(driverclassname);
        return dataSource;
    }
}
