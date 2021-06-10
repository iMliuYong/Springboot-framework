package com.quickshare.framework.job.controller;

import com.quickshare.common.bean.web.BaseResponse;
import com.quickshare.framework.core.global.GlobalConsts;
import com.quickshare.framework.job.definition.TriggerDefinition;
import com.quickshare.framework.job.service.JobService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * @author liu_ke
 */
@RestController
@RequestMapping(GlobalConsts.FRAMEWORK_CONTROLLER_PREF_PATH + "/jobmanager")
@ConditionalOnProperty(name = "spring.datasource.url")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService){
        this.jobService = jobService;
    }

    @PostMapping("/job/enable")
    public Object enableJob(String id) throws Exception {
        if(StringUtils.isEmpty(id)){
            return new BaseResponse<>(0,"id不能为空。",false);
        }
        return jobService.updateJobState(id,1);
    }

    @GetMapping("/job")
    public Object lstJob(){
        return jobService.lstJob();
    }

    @PostMapping("/trigger")
    public Object addTrigger(@RequestBody TriggerDefinition triggerDefinition){
        return jobService.addTrigger(triggerDefinition);
    }

    @PostMapping("/trigger/enable")
    public Object enableTrigger(String id) throws Exception {
        if(StringUtils.isEmpty(id)){
            return new BaseResponse<>(0,"id不能为空。",false);
        }
        return jobService.updateTriggerState(id,1);
    }

    @PostMapping("/trigger/disable")
    public Object disableTrigger(String id) throws Exception {
        if(StringUtils.isEmpty(id)){
            return new BaseResponse<>(0,"id不能为空。",false);
        }
        return jobService.updateTriggerState(id,0);
    }
}
