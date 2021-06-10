package com.quickshare.framework.job.service;

import com.quickshare.common.bean.web.BaseResponse;
import com.quickshare.common.json.JsonConvert;
import com.quickshare.framework.job.JobMetadata;
import com.quickshare.framework.job.definition.JobDefinition;
import com.quickshare.framework.job.definition.TriggerDefinition;
import com.quickshare.framework.job.mapper.QuartzJobMapper;
import com.quickshare.framework.log.QLoggerFactory;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author liu_ke
 */
@Service
@ConditionalOnProperty(name = "spring.datasource.url")
public class JobService {

    private final Logger log = QLoggerFactory.getLogger("default");

    private final QuartzJobMapper quartzJobMapper;
    private final JobManagerService jobManagerService;

    public JobService(QuartzJobMapper quartzJobMapper,
                      JobManagerService jobManagerService){
        this.quartzJobMapper = quartzJobMapper;
        this.jobManagerService = jobManagerService;
    }

    public Object lstJob(){
        List<JobDefinition> jobDefinitions = quartzJobMapper.lstJob(null);
        return new BaseResponse<>(0,"查询成功。",jobDefinitions);
    }

    @Transactional(rollbackFor = Exception.class)
    public Object updateJobState(String id,int state) throws Exception{
        Optional<JobDefinition> jobDefinition = quartzJobMapper.lstJob(id).stream().findAny();
        if(!jobDefinition.isPresent()){
            return new BaseResponse<>(0,"当前任务不存在在触发器，无法启用",false);
        }
        if(!jobDefinition.get().getTriggers().stream().filter(p->p.isEnabled()).findAny().isPresent()){
            return new BaseResponse<>(0,"当前任务不存在启用的触发器，无法开启任务。",false);
        }
        quartzJobMapper.updateJobState(id,state);
        String hint = "处理";
        if(state == 0){
            jobManagerService.deleteJob(id);
            hint = "禁用";
        }
        else if(state == 1){
            jobManagerService.updateJob(jobDefinition.get());
            hint = "启用";
        }
        return new BaseResponse<>(1,String.format("任务%s成功",hint),true);
    }

    public Object addTrigger(TriggerDefinition triggerDefinition){
        String id = UUID.randomUUID().toString();
        triggerDefinition.setId(id);
        quartzJobMapper.addTrigger(triggerDefinition);
        return new BaseResponse<>(1,"添加触发器成功",id);
    }

    @Transactional(rollbackFor = Exception.class)
    public Object updateTriggerState(String id, int state) throws Exception {
        quartzJobMapper.updateTriggerState(id,state);
        String hint = "处理";
        if(state == 0){
            jobManagerService.deleteTrigger(id);
            hint = "禁用";
        }
        else if(state == 1){
            List<TriggerDefinition> triggerDefinitions = quartzJobMapper.lstTrigger(id,null);
            if(triggerDefinitions.isEmpty()){
                throw new Exception("找不到对应的触发器。");
            }
            TriggerDefinition triggerDefinition = triggerDefinitions.get(0);
            String jobId = triggerDefinition.getJobId();
            Boolean jobState = quartzJobMapper.getJobState(jobId);
            jobManagerService.enableTriger(jobId,triggerDefinition,jobState.booleanValue());
            hint = "启用";
        }
        return new BaseResponse<>(1,String.format("触发器%s成功",hint),true);
    }

    public boolean applyJob(String id) throws Exception {
        JobDefinition jobDefinition = quartzJobMapper.lstJob(id).get(0);
        jobManagerService.updateJob(jobDefinition);
        return true;
    }
}
