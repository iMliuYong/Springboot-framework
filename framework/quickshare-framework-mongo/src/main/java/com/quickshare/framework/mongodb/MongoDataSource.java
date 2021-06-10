package com.quickshare.framework.mongodb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态数据源注解
 * @author liu_ke
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoDataSource {

    /**
     * 数据源key值
     * @return
     */
    String value();
}
