package com.quickshare.samples.datasource.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author liu_ke
 */
@Configuration
@MapperScan("com.quickshare.samples.datasource.mapper.**")
public class AppConfig {
}
