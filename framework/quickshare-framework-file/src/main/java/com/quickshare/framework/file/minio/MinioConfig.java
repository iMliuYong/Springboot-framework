package com.quickshare.framework.file.minio;

import com.quickshare.framework.file.ConditionalOnFileStorage;
import com.quickshare.framework.file.FileStorageType;
import io.minio.MinioClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO配置
 * @author liu_ke
 */
@Configuration
@ConditionalOnFileStorage(type = FileStorageType.MinIO)
@ConfigurationProperties(prefix = "file.minio")
public class MinioConfig {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    @Bean
    public MinioClient getMinioClient(){
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey,secretKey)
                .build();
        return minioClient;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
