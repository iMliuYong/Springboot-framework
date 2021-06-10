package com.quickshare.framework.json.serializer;

import com.quickshare.framework.core.utils.SpringBeanUtil;
import com.quickshare.framework.datasource.DataSourceUtils;
import com.quickshare.framework.json.dto.JsonField;
import com.quickshare.framework.json.filter.DynamicJsonFieldFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liu_ke
 */
public class SerializerFactory {

    @Value("${quickshare.json.serialize.dynamicfield.accord.type:}")
    private String accord_type;

    @Value("${quickshare.json.serialize.dynamicfield.accord.method:}")
    private String accord_method;

    @Autowired
    private Environment env;

    private boolean existDynamicJsonFieldConfig = false;
    private Method accordMethod = null;
    private List<JsonField> jsonFields = null;
    private Map<Object,DynamicJsonFieldSerializer> serializerMap = new HashMap<>();
    private List<String> clients = new ArrayList<>();

    public void init(){
        DataSource ds= getDataSource();
        String appName = env.getProperty("spring.application.name");
        jsonFields = lstJsonField(ds,appName);
        existDynamicJsonFieldConfig = !jsonFields.isEmpty() && checkAccord();
        clients.addAll(jsonFields.stream().map(p->p.getClient()).distinct().collect(Collectors.toList()));
    }

    public String getClient(){
        String client = null;
        Object bean = SpringBeanUtil.getApplicationContext().getBean(accord_type);
        try {
            Object result = accordMethod.invoke(bean);
            if(result == null){
                return null;
            }
            client = result.toString();
            if(clients.contains(client)){
                return client;
            }
            else{
                return null;
            }
        } catch (IllegalAccessException|InvocationTargetException e) {
            return null;
        }
    }

    public DynamicJsonFieldSerializer getFiler(String client){
        if(serializerMap.containsKey(client)){
            return serializerMap.get(client);
        }
        List<JsonField> jsonFieldList = jsonFields.stream().filter(p->client.equals(p.getClient())).collect(Collectors.toList());

        DynamicJsonFieldSerializer serializer = new DynamicJsonFieldSerializer();
        for (JsonField jsonField : jsonFieldList){
            serializer.filter(jsonField.getClassName(),jsonField.getInclude(),jsonField.getExclude());
        }
        serializerMap.put(client,serializer);
        return serializer;
    }

    public boolean existDynamicJsonFieldConfig(){
        return existDynamicJsonFieldConfig;
    }

    private boolean checkAccord(){
        if(StringUtils.isEmpty(accord_type) || StringUtils.isEmpty(accord_method)){
            return false;
        }
        try{
            Class c = Class.forName(accord_type);
            accordMethod = c.getMethod(accord_method);
            return true;
        }
        catch (ClassNotFoundException|NoSuchMethodException e){
            return false;
        }
    }

    private DataSource getDataSource(){
        // 如果启用配置方式，那就使用配置数据源读取相关设置信息，否则使用默认数据源
        String url = env.getProperty("spring.profiles.datasource.url");
        String username;
        String password;
        String driverClassName;
        if(StringUtils.isEmpty(url)){
            url = env.getProperty("spring.datasource.url");
            username = env.getProperty("spring.datasource.username");
            password = env.getProperty("spring.datasource.password");
            driverClassName = env.getProperty("spring.datasource.driver-class-name");
        }
        else{
            username = env.getProperty("spring.profiles.datasource.username");
            password = env.getProperty("spring.profiles.datasource.password");
            driverClassName = env.getProperty("spring.profiles.datasource.driver-class-name");
        }
        return DataSourceUtils.createDataSource(url,username,password,driverClassName);
    }

    private List<JsonField> lstJsonField(DataSource ds, String app){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        String strsql = "select CLASSNAME,CLIENT,INCLUDE,EXCLUDE from SYS_JSON_FIELD where APP=? and JLZT='1'";
        return jdbcTemplate.query(strsql,new Object[]{app},new BeanPropertyRowMapper<>(JsonField.class));
    }
}
