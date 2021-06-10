package com.quickshare.framework.mongodb.dynamic;

import com.quickshare.framework.log.QLoggerFactory;
import com.quickshare.framework.mongodb.MongoDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 动态数据源切换处理器
 *
 * @author liu_ke
 */
@Aspect
@Component
@Order(-1)
public class DynamicDataSourceAspect {

    private static Logger log = QLoggerFactory.getLogger("default");

    @Pointcut("@within(com.quickshare.framework.mongodb.MongoDataSource)||@annotation(com.quickshare.framework.mongodb.MongoDataSource)")
    public void pointcut() {}

    /**
     * 切换数据源
     * @param joinPoint
     */
    @Before("pointcut()")
    public void switchDataSource(JoinPoint joinPoint) {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        //获取方法上的注解
        MongoDataSource annotationClass = method.getAnnotation(MongoDataSource.class);
        if(annotationClass == null){
            //获取类上面的注解
            annotationClass = joinPoint.getTarget().getClass().getAnnotation(MongoDataSource.class);
            if(annotationClass == null) {
                return;
            }
        }
        //获取注解上的数据源的值的信息
        String dataSourceKey = annotationClass.value();
        if (!DynamicDataSourceContextHolder.containDataSourceKey(dataSourceKey)) {
            log.trace("MongoDB数据源 [{}] 不存在, 使用默认数据源 " + dataSourceKey);
        } else {
            // 切换数据源
            DynamicDataSourceContextHolder.setDataSourceKey(dataSourceKey);
            log.trace("MongoDB切换数据源到 [" + DynamicDataSourceContextHolder.getDataSourceKey()
                    + "] ，在方法 [" + joinPoint.getSignature() + "] 中");
        }
    }

    /**
     * 重置数据源
     * @param point
     */
    @After("pointcut()")
    public void restoreDataSource(JoinPoint point) {
        // 将数据源置为默认数据源
        DynamicDataSourceContextHolder.clearDataSourceKey();
        log.trace("MongoDB重置数据源到 [" + DynamicDataSourceContextHolder.getDataSourceKey()
                + "] ，在方法 [" + point.getSignature() + "] 中");
    }
}