package com.quickshare.framework.job.config;

import com.quickshare.framework.job.service.JobManagerService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.datasource.url")
public class JobConfiguration {

    @Bean(initMethod = "initMethod")
    public JobManagerService jobManagerService(){
        return new JobManagerService();
    }
}
