package com.quickshare.framework.file;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

/**
 * @author liu_ke
 */
@Service
public interface FileService {

    /**
     * 资源是否存在
     * @param name 资源名称
     * @return
     */
    boolean resourceExists(String name);

    /**
     * 创建资源
     * @param name 资源名称
     * @return
     */
    boolean createResource(String name);

    /**
     * 获取对象
     * @param resourceName 资源名称
     * @param objectName 对象名称
     * @return
     */
    InputStream getObject(String resourceName,String objectName);

    /**
     * 移除对象
     * @param resourceName 资源名称
     * @param objectName 对象名称
     * @return
     */
    boolean removeObject(String resourceName,String objectName);

    /**
     * 添加对象
     * @param resourceName
     * @param stream
     * @return
     */
    String putObject(String resourceName,InputStream stream);

    /**
     * 添加多个对象
     * @param resourceName
     * @param streamMap
     * @return
     */
    Map<String,String> putObjects(String resourceName,Map<String,InputStream> streamMap);

    /**
     * 添加对象
     * @param resourceName 资源名称
     * @param objectName 对象名称
     * @param stream 对象流
     * @return
     */
    boolean putObject(String resourceName,String objectName,InputStream stream);
}
