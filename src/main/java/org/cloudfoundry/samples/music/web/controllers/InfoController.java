package org.cloudfoundry.samples.music.web.controllers;

import org.cloudfoundry.samples.music.cloud.CloudInfo;
import org.cloudfoundry.samples.music.domain.ApplicationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class InfoController {
    private Environment springEnvironment;
    private CloudInfo cloudInfo;

    @Autowired
    public InfoController(Environment springEnvironment) {
        this.springEnvironment = springEnvironment;
        this.cloudInfo = new CloudInfo();
    }

    @ResponseBody
    @RequestMapping(value = "/info")
    public ApplicationInfo info() {
        return new ApplicationInfo(springEnvironment.getActiveProfiles(), cloudInfo.getServiceNames());
    }

    @RequestMapping(value = "/env")
    @ResponseBody
    public Map<String, String> showEnvironment() {
        return System.getenv();
    }

    @RequestMapping(value = "/service")
    @ResponseBody
    public Map<String, Object> showServiceInfo() {
        for (String profile : springEnvironment.getActiveProfiles()) {
            try {
                return cloudInfo.getServiceInfoForType(profile);
            } catch(Exception e) {
                // try the next profile, or fall through if none found
            }
        }

        return null;
    }
}