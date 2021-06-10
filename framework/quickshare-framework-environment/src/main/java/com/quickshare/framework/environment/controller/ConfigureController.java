package com.quickshare.framework.environment.controller;

import com.quickshare.framework.core.global.GlobalConsts;
import com.quickshare.framework.environment.dto.ProfileInfo;
import com.quickshare.framework.environment.service.ConfigureService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

/**
 * @author liu_ke
 */
@RestController
@RequestMapping(GlobalConsts.FRAMEWORK_CONTROLLER_PREF_PATH + "/configmanager")
public class ConfigureController {

    private final ConfigureService configureService;

    public ConfigureController(ConfigureService configureService){
        this.configureService = configureService;
    }

    @GetMapping("apps")
    public Object lstApps() throws Exception {
        return configureService.lstApps();
    }

    @GetMapping("profile")
    public Object lstProfiles(String app) throws Exception {
        return configureService.lstProfiles(app);
    }

    @PostMapping("profile")
    public Object addProfile(@RequestBody ProfileInfo profile) throws Exception {
        return configureService.addProfile(profile);
    }

    @DeleteMapping("profile")
    public Object deleteProfile(String app,String id,String name) throws Exception {
        return configureService.deleteProfile(app,id,name);
    }

    @PostMapping("profile/clone")
    public Object cloneProfile(@RequestBody ProfileInfo profile) throws Exception {
        return configureService.cloneProfile(profile);
    }

    @GetMapping("properties")
    public Object lstConfig(String app,String profile) throws Exception {
        return configureService.lstConfiguration(app,profile);
    }
}
