package com.quickshare.framework.datasource.config;

import com.github.pagehelper.PageHelper;
import com.quickshare.framework.datasource.Consts;
import com.quickshare.framework.datasource.DataSourceCondition;
import com.quickshare.framework.datasource.DataSourceUtils;
import com.quickshare.framework.datasource.dynamic.DynamicDataSource;
import com.quickshare.framework.datasource.dynamic.DynamicDataSourceContextHolder;
import com.quickshare.framework.datasource.dynamic.DynamicDataSourceTransactionManager;
import com.quickshare.framework.datasource.dynamic.DynamicSqlSessionTemplate;
import com.quickshare.framework.datasource.dynamic.dbsplit.AccordUtil;
import com.quickshare.framework.datasource.property.*;
import com.quickshare.framework.datasource.provider.MyDatabaseIdProvider;
import com.quickshare.framework.datasource.slave.ReadWritePlugin;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * MyBatis配置
 * 
 * @author liu_ke
 */
@Configuration
@EnableConfigurationProperties({AppsProperty.class, DefaultDataSource.class})
@Conditional({DataSourceCondition.class})
public class MyBatisConfig{

    private final AppsProperty apps;
    private final DefaultDataSource defaultDataSource;

    private final Map<String, SqlSessionFactory> driverFactoryMap = new HashMap<>();

    public MyBatisConfig(AppsProperty apps,DefaultDataSource defaultDataSource){
        this.apps = apps;
        this.defaultDataSource = defaultDataSource;
    }

    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() throws Exception{

        Map<Object, Object> dataSourceMap = new HashMap<>(apps.getApp().size());
        Map<String,String> dbsplitDataSourceKeys = new HashMap<>();
        List<String> slaveKeys = new ArrayList<>();
        if(defaultDataSource!=null && StringUtils.isNotEmpty(defaultDataSource.getUrl())){
            AppProperty app = new AppProperty();
            DataSourceProperty sourceProperty = new DataSourceProperty();
            sourceProperty.setUrl(defaultDataSource.getUrl());
            sourceProperty.setUsername(defaultDataSource.getUsername());
            sourceProperty.setPassword(defaultDataSource.getPassword());
            sourceProperty.setDriverclassname(defaultDataSource.getDriverclassname());
            app.setDatasource(sourceProperty);
            apps.getApp().put(Consts.DEFAULT, app);
         }
        DataSource defaultDS = null;
        for(Map.Entry<String,AppProperty> property : apps.getApp().entrySet()){
            DbSplitProperty dbsplit = property.getValue().getDbsplit();
            if(dbsplit != null && dbsplit.getDatasource()!=null){
                try{
                    AccordUtil.addField(dbsplit.getAccord().getType(),dbsplit.getAccord().getMethod());
                    DataSource split_ds = createDataSource(dbsplit.getDatasource());
                    List<DataSourceProperty> split_datasources = lstDataSourceProperty(property.getKey(),split_ds);
                    for (DataSourceProperty dsProperty:split_datasources) {
                        String datasourceKey = String.format("%s.%s",property.getKey(),dsProperty.getSplitkey());
                        DataSource ds = createDataSource(dsProperty);
                        dataSourceMap.put(datasourceKey, ds);
                        if(StringUtils.isNotEmpty(dsProperty.getSlaveUrl())){
                            ds = DataSourceUtils.createDataSource(dsProperty.getSlaveUrl(),dsProperty.getSlaveUsername(),
                                    dsProperty.getSlavePassword(),dsProperty.getDriverclassname());
                            slaveKeys.add(datasourceKey);
                            dataSourceMap.put(datasourceKey+Consts.SUFFIX_SLAVE, ds);
                        }
                    }
                    dbsplitDataSourceKeys.put(property.getKey(),AccordUtil.generateKey(dbsplit.getAccord().getType(),dbsplit.getAccord().getMethod()));
                }
                catch (Exception ex){
                    throw ex;
                }
            }

            DataSourceProperty dataSourceProperty = property.getValue().getDatasource();
            if(dataSourceProperty==null || StringUtils.isEmpty(dataSourceProperty.getUrl())){
                continue;
            }
            DataSource ds = createDataSource(dataSourceProperty);
            dataSourceMap.put(property.getKey(), ds);
            if(defaultDS == null || Consts.DEFAULT.equals(property.getKey())){
                defaultDS = ds;
            }
            // 从库处理
            dataSourceProperty = property.getValue().getDatasourceSlave();
            if(dataSourceProperty!=null && StringUtils.isNotEmpty(dataSourceProperty.getUrl())){
                ds = createDataSource(dataSourceProperty);
                dataSourceMap.put(property.getKey()+Consts.SUFFIX_SLAVE, ds);
                slaveKeys.add(property.getKey());
                dataSourceMap.put(property.getKey()+Consts.SUFFIX_SLAVE, ds);
            }
        }
        if(dataSourceMap.isEmpty()){
            return new HikariDataSource();
        }

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDataSources(dataSourceMap,dbsplitDataSourceKeys,slaveKeys);
        dynamicDataSource.setDefaultDataSource(defaultDS);
        return dynamicDataSource;
    }

    private SqlSessionFactory getDriverFactory(DataSource ds) throws Exception {
        String databaseName = ds.getConnection().getMetaData().getDatabaseProductName();
        if(driverFactoryMap.containsKey(databaseName)){
            return driverFactoryMap.get(databaseName);
        }
        else{
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean(ds);
            driverFactoryMap.put(databaseName,sqlSessionFactory);
            return sqlSessionFactory;
        }
    }

    private SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        // 配置数据源，此处配置为关键配置，如果没有将 dynamicDataSource作为数据源则不能实现切换
        sessionFactory.setDataSource(dataSource);
        // 扫描Model
        sessionFactory.setTypeAliasesPackage("com.quickshare.**.model,com.quickshare.**.domain,com.quickshare.**.model.**,com.quickshare.**.domain.**,com.quickshare.dep.common.result");
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 扫描映射文件
        sessionFactory.setMapperLocations(resolver.getResources("classpath*:mapper/**/*.xml"));
        sessionFactory.setDatabaseIdProvider(getDatabaseIdProvider());
        if(DynamicDataSourceContextHolder.existsSlave()){
            sessionFactory.setPlugins(new Interceptor[]{new ReadWritePlugin()});
        }
        return sessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        final DynamicDataSource ds = (DynamicDataSource)dynamicDataSource();
        final Map<String, SqlSessionFactory> sessionFactoryMap = new HashMap<>();
        for (Map.Entry<Object,Object> datasourceEntry: ds.getDataSources().entrySet()) {
            String key = (String) datasourceEntry.getKey();
            DynamicDataSourceContextHolder.setDataSourceKey(key);
            sessionFactoryMap.put(key,getDriverFactory(ds));
        }
        DynamicDataSourceContextHolder.clearDataSourceKey();
        SqlSessionFactory defaultSessionFactory = sqlSessionFactoryBean(ds);
        DynamicSqlSessionTemplate dynamicSqlSessionTemplate = new DynamicSqlSessionTemplate(defaultSessionFactory);
        dynamicSqlSessionTemplate.setTargetSqlSessionFactory(sessionFactoryMap);
        return dynamicSqlSessionTemplate;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception{
        // 配置事务管理, 使用事务时在方法头部添加@Transactional注解即可
        //return new DataSourceTransactionManager(dynamicDataSource());
        return new DynamicDataSourceTransactionManager(dynamicDataSource());
    }

    private DataSource createDataSource(DataSourceProperty property){
        return DataSourceUtils.createDataSource(property.getUrl(),property.getUsername(),property.getPassword(),property.getDriverclassname());
    }

    @Bean
    public DatabaseIdProvider getDatabaseIdProvider(){
        DatabaseIdProvider databaseIdProvider = new MyDatabaseIdProvider();
        return databaseIdProvider;
    }

    private List<DataSourceProperty> lstDataSourceProperty(String name, DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "select MASTER_URL as url, MASTER_USER as username, MASTER_PWD as password, " +
                "SLAVE_URL as slaveUrl, SLAVE_USER as slaveUsername, SLAVE_PWD as slavePassword, " +
                "DRIVER as driverclassname,SPLIT_KEY as splitkey " +
                "from SYS_SHARDING_DB_MAPPING where SYS_ID=? and JLZT='1'";
        return jdbcTemplate.query(sql,new Object[]{name},new BeanPropertyRowMapper<>(DataSourceProperty.class));
    }

    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}
