package com.quickshare.framework.job.mapper;

import com.quickshare.framework.job.definition.JobDefinition;
import com.quickshare.framework.job.definition.TriggerDefinition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author liu_ke
 */
@Mapper
public interface QuartzJobMapper {

    /**
     * 查询Job列表
     * @param id
     * @return
     */
    List<JobDefinition> lstJob(@Param("id")String id);

    /**
     * 查询触发器列表
     * @param id
     * @param jobId
     * @return
     */
    List<TriggerDefinition> lstTrigger(@Param("id")String id,@Param("jobId")String jobId);

    /**
     * 禁用Job,JLZT=0
     * @param ids
     * @return
     */
    int disableJobs(@Param("ids")List<String> ids);

    /**
     * 更新Job的状态
     * @param id JOB_ID,Job主键
     * @param state Job状态
     * @return
     */
    @Update("<script>update SYS_JOB set IS_ENABLED=#{state} where JOB_ID=#{id}</script>")
    int updateJobState(@Param("id")String id,@Param("state")int state);

    /**
     * 更新Job的状态
     * @param id JOB_ID,Job主键
     * @param state Job状态
     * @return
     */
    @Update("<script>update SYS_JOB_TRIGGERS set IS_ENABLED=#{state} where TRIGGER_ID=#{id}</script>")
    int updateTriggerState(@Param("id")String id,@Param("state")int state);

    int addJob(@Param("id")String id,
               @Param("name")String name,
               @Param("group")String group,
               @Param("desc")String desc,
               @Param("state")int state);

    /**
     * 添加触发器
     * @param triggerDefinition
     * @return
     */
    int addTrigger(TriggerDefinition triggerDefinition);

    /**
     * 获取Job状态
     * @param id
     * @return
     */
    @Select("<script>select IS_ENABLED from SYS_JOB where JOB_ID=#{id}</script>")
    Boolean getJobState(@Param("id")String id);

    int editJob(JobDefinition job);
}