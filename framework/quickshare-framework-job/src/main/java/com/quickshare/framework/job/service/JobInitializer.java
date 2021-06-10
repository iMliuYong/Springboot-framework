package com.quickshare.framework.job.service;

import com.quickshare.common.json.JsonConvert;
import com.quickshare.framework.job.JobMetadata;
import com.quickshare.framework.job.definition.JobDefinition;
import com.quickshare.framework.job.mapper.QuartzJobMapper;
import com.quickshare.framework.job.mapper.QuartzTableMapper;
import com.quickshare.framework.log.QLoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liu_ke
 */
@Service
@ConditionalOnProperty(name = "spring.datasource.url")
public class JobInitializer {

    private final Logger log = QLoggerFactory.getLogger("default");

    private final QuartzJobMapper quartzJobMapper;
    private final QuartzTableMapper quartzTableMapper;
    private final JobManagerService jobManagerService;

    public JobInitializer(QuartzJobMapper quartzJobMapper,
                          QuartzTableMapper quartzTableMapper,
                          JobManagerService jobManagerService){
        this.quartzJobMapper = quartzJobMapper;
        this.quartzTableMapper = quartzTableMapper;
        this.jobManagerService = jobManagerService;
    }

    public void initTable(){
        quartzTableMapper.initTable();
    }

    public void initJobs(){
        List<JobMetadata> jobMetadataList = jobManagerService.lstAllJobMetadata();
        List<String> ids = new ArrayList<>();
        for (JobMetadata jobMetadata:jobMetadataList) {
            ids.add(jobMetadata.jobId());
            quartzJobMapper.addJob(jobMetadata.jobId(),jobMetadata.jobName(),jobMetadata.jobGroup(),null,0);
        }
        if(ids.isEmpty()){
            quartzJobMapper.disableJobs(null);
        }
        else{
            quartzJobMapper.disableJobs(ids);
        }
    }

    public void loadJobs(){
        List<JobDefinition> jobs = quartzJobMapper.lstJob(null);
        for (JobDefinition jobDefinition : jobs) {
            if(jobDefinition.isEnabled() == false){
                continue;
            }
            try{
                jobManagerService.addJob(jobDefinition);
            }
            catch(Exception ex){
                log.error(String.format("初始化作业[%s]失败", jobDefinition.getId()));
            }
        }
    }
}
