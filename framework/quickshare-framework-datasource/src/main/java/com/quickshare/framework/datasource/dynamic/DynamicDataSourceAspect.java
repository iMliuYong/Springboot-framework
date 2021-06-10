package com.quickshare.framework.datasource.dynamic;

import com.quickshare.framework.datasource.DataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger log = LoggerFactory.getLogger("default");

    @Pointcut("@within(com.quickshare.framework.datasource.DataSource)||@annotation(com.quickshare.framework.datasource.DataSource)")
    public void pointcut() {}

    /**
     * 切换数据源
     * @param joinPoint
     */
    @Before("pointcut()")
    public void switchDataSource(JoinPoint joinPoint) {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        //获取方法上的注解
        DataSource annotationClass = method.getAnnotation(DataSource.class);
        if(annotationClass == null){
            //获取类上面的注解
            annotationClass = joinPoint.getTarget().getClass().getAnnotation(DataSource.class);
            if(annotationClass == null) {
                return;
            }
        }
        //获取注解上的数据源的值的信息
        String dataSourceKey = annotationClass.value();
        if (!DynamicDataSourceContextHolder.containDataSourceKey(dataSourceKey)) {
            log.trace("数据源 [{}] 不存在, 使用默认数据源 " + dataSourceKey);
        } else {
            // 切换数据源
            DynamicDataSourceContextHolder.setDataSourceKey(dataSourceKey);
            log.trace("切换数据源到 [" + DynamicDataSourceContextHolder.getDataSourceKey()
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
        log.trace("重置数据源到 [" + DynamicDataSourceContextHolder.getDataSourceKey()
                + "] ，在方法 [" + point.getSignature() + "] 中");
    }
}
