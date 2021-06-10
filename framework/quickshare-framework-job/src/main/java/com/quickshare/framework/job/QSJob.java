package com.quickshare.framework.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;

/**
 * @author wangy
 */
@DisallowConcurrentExecution
public interface QSJob extends Job {


}
