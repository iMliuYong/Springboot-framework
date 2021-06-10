package com.quickshare.framework.datasource.dynamic;

import com.quickshare.framework.datasource.Consts;
import com.quickshare.framework.datasource.OperationMode;
import com.quickshare.framework.datasource.dynamic.dbsplit.AccordUtil;

import java.util.*;

/**
 * 动态数据源上下文
 * 
 * @author liu_ke
 */
public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>() {
        /**
         * 将 master 数据源的 key作为默认数据源的 key
         */
        @Override
        protected String initialValue() {
            return Consts.DEFAULT;
        }
    };


    /**
     * 数据源的 key集合，用于切换时判断数据源是否存在
     */
    private final static List<Object> dataSourceKeys = new ArrayList<>();

    /**
     * 存在从库的数据源key
     */
    private final static List<Object> dsKeys_slave = new ArrayList<>();

    private final static Map<String,String> dbsplitDataSourceKeys = new HashMap<>();

    /**
     * 切换数据源
     * @param key
     */
    public static void setDataSourceKey(String key) {
        if(dbsplitDataSourceKeys.keySet().contains(key)){
            String dbsplitKey = AccordUtil.getValue(dbsplitDataSourceKeys.get(key));
            key = String.format("%s.%s",key,dbsplitKey);
        }
        contextHolder.set(key);
    }

    public static  boolean existsSlave(){
        return !dsKeys_slave.isEmpty();
    }

    public static boolean supportSlave(){
        String key = contextHolder.get();
        if(key.endsWith(Consts.SUFFIX_SLAVE)){
            return true;
        }
        else{
            return dsKeys_slave.contains(key);
        }
    }

    public static void setMode(OperationMode mode){
        String key = contextHolder.get();
        boolean isSlaveKey = key.endsWith(Consts.SUFFIX_SLAVE);
        if(mode == OperationMode.READ && !isSlaveKey){
            key = key + Consts.SUFFIX_SLAVE;
            contextHolder.set(key);
        }
        else if(mode == OperationMode.WRITE && isSlaveKey){
            key = key.substring(0,key.length()-Consts.SUFFIX_SLAVE.length());
            contextHolder.set(key);
        }
    }

    /**
     * 获取数据源
     * @return
     */
    public static String getDataSourceKey() {
        return contextHolder.get();
    }

    /**
     * 重置数据源
     */
    public static void clearDataSourceKey() {
        contextHolder.remove();
    }

    /**
     * 判断是否包含数据源
     * @param key 数据源key
     * @return
     */
    public static boolean containDataSourceKey(String key) {
        return dataSourceKeys.contains(key) || dbsplitDataSourceKeys.keySet().contains(key);
    }

    /**
     * 添加数据源keys
     * @param keys
     * @return
     */
    public static boolean addDataSourceKeys(Collection<? extends Object> keys) {
        return dataSourceKeys.addAll(keys);
    }

    /**
     * 添加数据源分库键
     * @param keys
     * @return
     */
    public static void setDbSplitDataSourceKeys(Map<String,String> keys){
        dbsplitDataSourceKeys.putAll(keys);
    }

    public static void setSlaveKeys(List<String> slaveKeys){
        dsKeys_slave.addAll(slaveKeys);
    }
}