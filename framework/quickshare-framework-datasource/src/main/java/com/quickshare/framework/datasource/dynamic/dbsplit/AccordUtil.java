package com.quickshare.framework.datasource.dynamic.dbsplit;

import com.quickshare.framework.core.utils.SpringBeanUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 分库依据工具
 * @author liu_ke
 */
public class AccordUtil {

    private final static Map<String, Method> methodInfo = new HashMap<>();

    public static String generateKey(String classname,String fieldname){
        String key = String.format("%s.%s",classname,fieldname);
        return key;
    }

    public static void addField(String classname,String methodname) throws Exception{
        try{
            Class c = Class.forName(classname);
            Method m = c.getMethod(methodname);
            methodInfo.put(generateKey(classname,methodname),m);
        }
        catch (ClassNotFoundException e){
            throw new Exception(String.format("类型[%s]不存在",classname));
        }
        catch (NoSuchMethodException e){
            throw new Exception(String.format("类型[%s]的方法[%s]不存在",classname,methodname));
        }
    }

    public static String getValue(String key){
        Method m = methodInfo.get(key);
        String classname = key.substring(0,key.lastIndexOf("."));
        Object bean = SpringBeanUtil.getApplicationContext().getBean(classname);
        try{
            Object value = m.invoke(bean);
            if(value!=null){
                return value.toString();
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            return null;
        }
    }
}
