package com.quickshare.framework.job.config;

import com.quickshare.framework.job.service.JobInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author liu_ke
 */
@Component
@ConditionalOnProperty(name = "spring.datasource.url")
public class QuartzInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger log = LoggerFactory.getLogger("default");

    private final JobInitializer jobInitializer;

    public QuartzInitializer(JobInitializer jobInitializer){
        this.jobInitializer = jobInitializer;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        try{
            jobInitializer.initTable();
            jobInitializer.initJobs();
            jobInitializer.loadJobs();
        }
        catch(Exception ex){
            log.error(ex.getMessage(), ex);
        }
    }


}
