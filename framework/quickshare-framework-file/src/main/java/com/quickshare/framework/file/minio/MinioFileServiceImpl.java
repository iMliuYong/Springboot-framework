package com.quickshare.framework.file.minio;

import com.quickshare.framework.file.ConditionalOnFileStorage;
import com.quickshare.framework.file.FileService;
import com.quickshare.framework.file.FileStorageType;
import io.minio.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * MinIO文件服务实现
 * @author liu_ke
 */
@Service
@ConditionalOnFileStorage(type = FileStorageType.MinIO)
public class MinioFileServiceImpl implements FileService {

    private final MinioClient minioClient;

    public MinioFileServiceImpl(MinioClient minioClient){
        this.minioClient = minioClient;
    }

    @Override
    public boolean resourceExists(String name) {
        name = handleName(name);
        try {
            boolean flag = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name)
                    .build());
            if (flag) {
                return true;
            }
            return false;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public boolean createResource(String name) {
        name = handleName(name);
        try {
            boolean flag = resourceExists(name);
            if (!flag) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(name)
                                .build());
                return true;
            } else {
                return true;
            }
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public InputStream getObject(String resourceName, String objectName) {
        resourceName = handleName(resourceName);
        try {
            boolean flag = resourceExists(resourceName);

            if (flag) {
                InputStream stream = minioClient.getObject(GetObjectArgs.builder().bucket(resourceName).object(objectName).build());
                return stream;
            }
            return null;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public boolean removeObject(String resourceName, String objectName) {
        resourceName = handleName(resourceName);
        try {
            boolean flag = resourceExists(resourceName);
            if (flag) {
                minioClient.removeObject(RemoveObjectArgs.builder()
                        .bucket(resourceName)
                        .object(objectName)
                        .build());
                return true;
            }
            return false;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public boolean putObject(String resourceName, String objectName, InputStream stream) {
        return putObject(resourceName,objectName,stream,true);
    }

    @Override
    public String putObject(String resourceName, InputStream stream) {
        resourceName = handleName(resourceName);
        boolean flag = createResource(resourceName);
        if(flag == false){
            return null;
        }

        try{
            byte[] byt = new byte[stream.available()];
            stream.read(byt);
            String objectName = DigestUtils.md5Hex(byt);
            stream.close();
            stream = new ByteArrayInputStream(byt);
            if(putObject(resourceName,objectName,stream,false)){
                return objectName;
            }
        }
        catch (Exception e){
            return null;
        }

        return null;
    }

    @Override
    public Map<String, String> putObjects(String resourceName, Map<String, InputStream> streamMap) {
        Map<String, String> result = new HashMap<>(streamMap.size());
        for (Map.Entry<String,InputStream> streamEntry: streamMap.entrySet()) {
            String objectName = putObject(resourceName,streamEntry.getValue());
            result.put(streamEntry.getKey(),objectName);
        }
        return result;
    }

    private boolean putObject(String resourceName, String objectName, InputStream stream, boolean createResource) {
        try {
            boolean flag = true;
            if(createResource){
                flag = createResource(resourceName);
            }
            if(flag) {
                minioClient.putObject(PutObjectArgs.builder().bucket(resourceName).object(objectName).stream(
                        stream, stream.available(), -1)
                        //.contentType(contentType)
                        .build());
                return true;
            }
            return false;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 获取对象信息
     * @param resourceName
     * @param objectName
     * @return
     */
    private ObjectStat statObject(String resourceName, String objectName){
        resourceName = handleName(resourceName);
        try {
            boolean flag = resourceExists(resourceName);
            if (flag) {
                ObjectStat statObject = minioClient.statObject(StatObjectArgs.builder()
                        .bucket(resourceName)
                        .object(objectName).build());
                return statObject;
            }
            return null;
        }
        catch (Exception e){
            return null;
        }
    }

    private String handleName(String bucketName){
        return bucketName.toLowerCase().replace("_","-");
    }
}
