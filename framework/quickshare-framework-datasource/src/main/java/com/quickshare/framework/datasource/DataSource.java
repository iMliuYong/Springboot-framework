package com.quickshare.framework.datasource;

import java.lang.annotation.*;

/**
 * 动态数据源注解
 * @author liu_ke
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {

    /**
     * 数据源key值
     * @return
     */
    String value();
}