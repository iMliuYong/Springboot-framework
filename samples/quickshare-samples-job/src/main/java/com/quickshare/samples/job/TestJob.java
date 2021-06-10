package com.quickshare.samples.job;

import com.quickshare.framework.job.JobMetadata;
import com.quickshare.framework.job.QSJob;
import com.quickshare.framework.log.QLoggerFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

/**
 * @author liu_ke
 */
@JobMetadata(jobId = "b9be9674-e314-4951-a555-4e09f6e35346",jobName = "一个测试任务")
public class TestJob implements QSJob {

    private final Logger logger = QLoggerFactory.getLogger("test");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("test");
    }
}
