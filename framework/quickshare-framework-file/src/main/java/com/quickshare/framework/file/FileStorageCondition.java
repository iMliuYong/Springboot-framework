package com.quickshare.framework.file;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 文件存储选择条件
 * @author liu_ke
 */
public class FileStorageCondition implements ConfigurationCondition {

    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.PARSE_CONFIGURATION;
    }

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String fileStorageType = conditionContext.getEnvironment().getProperty("file.storage.type","FileSystem");
        FileStorageType typeOnProperty = (FileStorageType) annotatedTypeMetadata.getAnnotationAttributes(ConditionalOnFileStorage.class.getName()).get("type");
        return typeOnProperty.toString().equals(fileStorageType);
    }
}
