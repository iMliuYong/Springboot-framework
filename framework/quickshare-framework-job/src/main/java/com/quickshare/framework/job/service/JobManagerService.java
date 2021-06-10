package com.quickshare.framework.job.service;

import com.quickshare.common.json.JsonConvert;
import com.quickshare.common.reflect.ClassScanner;
import com.quickshare.framework.core.global.GlobalConsts;
import com.quickshare.framework.job.JobMetadata;
import com.quickshare.framework.job.QSJob;
import com.quickshare.framework.job.definition.JobDefinition;
import com.quickshare.framework.job.definition.TriggerDefinition;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liu_ke
 */
public class JobManagerService {

    private static Logger log = LoggerFactory.getLogger("default");

    private Map<String, JobMetadata> jobMetadataMap = new HashMap<>();
    private Map<String, Class<? extends QSJob>> jobClassMap = new HashMap<>();

    @Autowired
    @Qualifier("scheduler")
    private Scheduler scheduler;

    public void initMethod() {
        try {
            Set<Class<?>> set = (new ClassScanner()).getAnnotationClasses(GlobalConsts.APP_BASE_PACKAGE,JobMetadata.class);
            for (Class<?> clazz:set) {
                if(QSJob.class.isAssignableFrom(clazz)){
                    JobMetadata jobMetadata = clazz.getAnnotation(JobMetadata.class);
                    jobClassMap.put(jobMetadata.jobId(),(Class<? extends QSJob>) clazz);
                    jobMetadataMap.put(jobMetadata.jobId(),jobMetadata);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JobMetadata getJobMetadata(String id){
        return jobMetadataMap.getOrDefault(id,null);
    }

    public List<JobMetadata> lstAllJobMetadata(){
        return jobMetadataMap.values().stream().collect(Collectors.toList());
    }

    public void addJob(JobDefinition jobDefinition) throws Exception{
        if(!jobDefinition.getTriggers().stream().filter(p->p.isEnabled()).findAny().isPresent()){
            return;
        }
        String jobId = jobDefinition.getId();
        Class<? extends QSJob> jobClass = jobClassMap.getOrDefault(jobId,null);
        if(jobClass != null){
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobId).build();
            Set<Trigger> triggers = new HashSet<>();
            for (TriggerDefinition triggerDefinition: jobDefinition.getTriggers()) {
                String triggerId = triggerDefinition.getId();
                String cron = triggerDefinition.getCron();
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                TriggerBuilder<CronTrigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity(triggerId).withSchedule(cronScheduleBuilder);
                if(triggerDefinition.getDataMap() != null){
                    triggerBuilder = triggerBuilder.usingJobData(new JobDataMap(triggerDefinition.getDataMap()));
                }
                Trigger trigger = triggerBuilder.build();
                triggers.add(trigger);
            }
            scheduler.scheduleJob(jobDetail, triggers, true);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        }
    }

    public void enableTriger(String jobId,TriggerDefinition triggerDefinition,boolean autoCreateJob) throws SchedulerException {
        JobDetail jobDetail = getJobDetail(jobId);
        // 当任务不存在时，不处理。
        if(jobDetail == null && autoCreateJob == false){
            return;
        }
        Trigger oldTrigger = null;
        if(jobDetail == null){
            Class<? extends QSJob> jobClass = jobClassMap.getOrDefault(jobId,null);
            jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobId).build();
        }
        else {
            oldTrigger = getTrigger(triggerDefinition.getId());
        }
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(triggerDefinition.getCron());
        Trigger newTrigger = TriggerBuilder.newTrigger().withIdentity(triggerDefinition.getId())
                .usingJobData(new JobDataMap(triggerDefinition.getDataMap()))
                .withSchedule(cronScheduleBuilder).build();
        if(oldTrigger != null){
            scheduler.rescheduleJob(TriggerKey.triggerKey(jobId),newTrigger);
            return;
        }
        scheduler.scheduleJob(jobDetail,newTrigger);
    }

    public void pauseJob(String jobId) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobId);
        scheduler.pauseJob(jobKey);
    }

    public void resumeJob(String jobId) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobId);
        scheduler.resumeJob(jobKey);
    }

    public void deleteJob(String jobId) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobId);
        scheduler.deleteJob(jobKey);
    }

    public void pauseTrigger(String triggerId) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerId);
        scheduler.pauseTrigger(triggerKey);
    }

    public void resumeTrigger(String triggerId) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerId);
        scheduler.resumeTrigger(triggerKey);
    }

    public void deleteTrigger(String triggerId) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerId);
        scheduler.unscheduleJob(triggerKey);
    }

    public void runJobNow(String jobName, String jobGroupName) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        scheduler.triggerJob(jobKey);
    }

    public void updateJobCron(String jobName, String jobGroupName, String cron) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    public void updateJob(JobDefinition jobDefinition) throws Exception{
        String jobId = jobDefinition.getId();
        JobKey jobKey = JobKey.jobKey(jobId);
        if(scheduler.checkExists(jobKey)){
            scheduler.deleteJob(jobKey);
        }
        addJob(jobDefinition);
    }

    private JobDetail getJobDetail(String jobId){
        JobKey jobKey = JobKey.jobKey(jobId, jobId);
        try {
            return scheduler.getJobDetail(jobKey);
        }
        catch (SchedulerException e){
            return null;
        }
    }

    private Trigger getTrigger(String triggerId){
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerId, triggerId);
        try {
            return scheduler.getTrigger(triggerKey);
        }
        catch (SchedulerException e){
            return null;
        }
    }


}