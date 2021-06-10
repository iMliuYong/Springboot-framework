package com.quickshare.samples.log.controller;

import com.quickshare.framework.log.QLoggerFactory;
import com.quickshare.samples.log.service.LogService;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liu_ke
 */
@RestController
@RequestMapping("log")
public class LogController {

    private final Logger logger = QLoggerFactory.getLogger("Controllers");
    private final LogService logService;

    public LogController(LogService logService){
        this.logService = logService;
    }

    @GetMapping
    public String test(@RequestParam(name = "param")String param){

        logger.info(param);
        logger.warn(param);
        logger.error(param);
        logger.error("测试个error",new Exception("抛错试试"));

        logService.log(param);
        return param;
    }
}
