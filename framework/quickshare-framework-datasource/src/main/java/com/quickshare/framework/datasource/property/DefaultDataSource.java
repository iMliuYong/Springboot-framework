package com.quickshare.framework.datasource.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * DefaultDataSource
 * @author liu_ke
 */
@ConfigurationProperties(prefix = "spring.datasource")
public class DefaultDataSource extends DataSourceProperty {

    
}