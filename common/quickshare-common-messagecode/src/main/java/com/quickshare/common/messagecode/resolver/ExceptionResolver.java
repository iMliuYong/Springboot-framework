package com.quickshare.common.messagecode.resolver;

/**
 * 错误解析器
 * @author liu_ke
 */
public class ExceptionResolver {

    public static String getExceptionMessage(Exception ex){
        String message = ex.getMessage();
        if(ex instanceof org.springframework.dao.DataIntegrityViolationException){
            message = ex.getCause().getMessage();
        }
        return message;
    }
}
