package com.quickshare.framework.environment.service;

import com.quickshare.framework.datasource.DataSourceUtils;
import com.quickshare.framework.environment.dto.EnvProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.*;

/**
 * @author liu_ke
 */
public class MyEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String applicationName = environment.getProperty("spring.application.name");
        String url = environment.getProperty("spring.profiles.datasource.url");
        String username = environment.getProperty("spring.profiles.datasource.username");
        String password = environment.getProperty("spring.profiles.datasource.password");
        String driverClassName = environment.getProperty("spring.profiles.datasource.driver-class-name");

        if(StringUtils.isEmpty(url)){
            return;
        }
        try {
            List<String> profiles = new ArrayList<>();
            String[] defaultProfiles = environment.getDefaultProfiles();
            if(defaultProfiles.length>0){
                profiles.addAll(Arrays.asList(defaultProfiles));
            }
            String[] activeProfiles = environment.getActiveProfiles();
            if(activeProfiles.length>0){
                profiles.addAll(Arrays.asList(activeProfiles));
            }
            if(profiles.isEmpty()){
                return;
            }

            DataSource ds = DataSourceUtils.createDataSource(url, username, password, driverClassName);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

            for (String profile:profiles) {
                List<EnvProperty> envProperties = lstProperties(jdbcTemplate,applicationName,profile);
                if(!envProperties.isEmpty()){
                    Properties properties = new Properties();
                    for (EnvProperty envProperty:envProperties) {
                        properties.put(envProperty.getKey1(),envProperty.getValue1());
                    }
                    PropertySource ps = new PropertiesPropertySource(profile+"-jdbc",properties);
                    environment.getPropertySources().addLast(ps);
                }
            }
        }
        catch (Exception ex){
            // do nothing
        }
    }

    private List<EnvProperty> lstProperties(JdbcTemplate jdbcTemplate,String application,String profile){
        String sql = "select KEY1 as key1, VALUE1 as value1 from SYS_CONFIG_PROPERTIES " +
                "where APPLICATION=? and PROFILE=?";
        return jdbcTemplate.query(sql,new Object[]{application,profile},new BeanPropertyRowMapper<>(EnvProperty.class));
    }
}
