package com.quickshare.framework.job.config;

import com.quickshare.framework.datasource.DataSourceUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

/**
 * @author liu_ke
 */
@Configuration
public class ScheduledConfiguration {

    @Value("${spring.datasource.url:}")
    private String url;
    @Value("${spring.datasource.username:}")
    private String username;
    @Value("${spring.datasource.password:}")
    private String password;
    @Value("${spring.datasource.driver-class-name:}")
    private String driverClassName;

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * 调度工厂bean
     *
     * @param jobFactory
     * @return
     * @throws IOException
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // this allows to update triggers in DB when updating settings in config
        // file:
        // 用于quartz集群,QuartzScheduler
        // 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        // 用于quartz集群,加载quartz数据源
        // factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        // QuartzScheduler 延时启动，应用启动完20秒后 QuartzScheduler 再启动
        factory.setStartupDelay(2);
        factory.setAutoStartup(isAutoStartup());

        return factory;
    }

    @Bean(name="scheduler")
    public Scheduler scheduler(JobFactory jobFactory) throws IOException {
        return schedulerFactoryBean(jobFactory).getScheduler();
    }

    private boolean isAutoStartup(){
        if(StringUtils.isEmpty(url)){
            return false;
        }
        DataSource ds = DataSourceUtils.createDataSource(url,username,password,driverClassName);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        String sql = "select 1 from SYS_JOB where JLZT='1' and IS_ENABLED=1";
        try {
            List<Integer> rows = jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<>(Integer.class));
            if(rows.isEmpty()){
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
}