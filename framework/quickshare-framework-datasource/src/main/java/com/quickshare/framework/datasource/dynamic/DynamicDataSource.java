package com.quickshare.framework.datasource.dynamic;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 * 
 * @author liu_ke
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    public Map<Object, Object> getDataSources() {
        return targetDataSources;
    }

    public Object getDefaultDataSource() {
        return defaultDataSource;
    }

    private Map<Object,Object> targetDataSources;
    private Object defaultDataSource;

    /**
     * 如果不希望数据源在启动配置时就加载好，可以定制这个方法，从任何你希望的地方读取并返回数据源
     * 比如从数据库、文件、外部接口等读取数据源信息，并最终返回一个DataSource实现类对象即可
     */
    @Override
    protected DataSource determineTargetDataSource() {
        return super.determineTargetDataSource();
    }

    /**
     * 如果希望所有数据源在启动配置时就加载好，这里通过设置数据源Key值来切换数据，定制这个方法
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }

    /**
     * 设置默认数据源
     * @param defaultDataSource
     */
    public void setDefaultDataSource(Object defaultDataSource) {
        super.setDefaultTargetDataSource(defaultDataSource);
        this.defaultDataSource = defaultDataSource;
    }

    /**
     * 设置数据源
     * @param dataSources
     * @param dbsplitDataSourceKeys
     * @param slaveKeys
     */
    public void setDataSources(Map<Object, Object> dataSources,
                               Map<String,String> dbsplitDataSourceKeys,
                               List<String> slaveKeys) {
        this.targetDataSources = dataSources;
        super.setTargetDataSources(dataSources);
        // 将数据源的 key 放到数据源上下文的 key 集合中，用于切换时判断数据源是否有效
        DynamicDataSourceContextHolder.addDataSourceKeys(dataSources.keySet());
        DynamicDataSourceContextHolder.setDbSplitDataSourceKeys(dbsplitDataSourceKeys);
        DynamicDataSourceContextHolder.setSlaveKeys(slaveKeys);
    }

    public DataSource getDataSources(String key){
        if(targetDataSources.keySet().contains(key)){
            return (DataSource)targetDataSources.get(key);
        }
        else{
            return null;
        }
    }
}
