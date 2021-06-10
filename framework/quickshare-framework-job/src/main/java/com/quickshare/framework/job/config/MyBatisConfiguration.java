package com.quickshare.framework.job.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author liu_ke
 */
@Configuration
@MapperScan("com.quickshare.framework.job.mapper")
@ConditionalOnProperty(name = "spring.datasource.url")
public class MyBatisConfiguration {
}
