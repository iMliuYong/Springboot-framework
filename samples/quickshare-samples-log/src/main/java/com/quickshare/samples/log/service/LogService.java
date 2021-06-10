package com.quickshare.samples.log.service;

import com.quickshare.framework.log.QLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author liu_ke
 */
@Service
public class LogService {

    private static Logger logger = QLoggerFactory.getLogger("service","service/sub");

    public void log(String param){
        logger.info(param);
        logger.warn(param);
        logger.error(param);
    }
}
