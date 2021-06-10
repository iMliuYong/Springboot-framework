package com.quickshare.framework.file;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author liu_ke
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(FileStorageCondition.class)
public @interface ConditionalOnFileStorage {

    FileStorageType type() default FileStorageType.FileSystem;
}
