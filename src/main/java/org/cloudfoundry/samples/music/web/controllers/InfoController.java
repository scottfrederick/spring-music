package org.cloudfoundry.samples.music.web.controllers;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.samples.music.domain.ApplicationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class InfoController {
    private Environment springEnvironment;

    @Autowired
    public InfoController(Environment springEnvironment) {
        this.springEnvironment = springEnvironment;
    }

    @ResponseBody
    @RequestMapping(value = "/info")
    public ApplicationInfo info() {
        return new ApplicationInfo(springEnvironment.getActiveProfiles(), getServiceNames());
    }

    @RequestMapping(value = "/env")
    @ResponseBody
    public Map<String, String> showEnvironment() {
        return System.getenv();
    }

    @RequestMapping(value = "/service")
    @ResponseBody
    public List<Map<String, Object>> showServiceInfo() {
        CloudEnvironment cloudEnvironment = new CloudEnvironment();
        return cloudEnvironment.getServices();
    }


    private String[] getServiceNames() {
        CloudEnvironment cloudEnvironment = new CloudEnvironment();

        if (cloudEnvironment.isCloudFoundry()) {
            List<Map<String, Object>> services = cloudEnvironment.getServices();

            List<String> names = new ArrayList<String>();
            for (Map<String, Object> service : services) {
                names.add(service.get("name").toString());
            }
            return names.toArray(new String[names.size()]);
        } else {
            return new String[]{};
        }
    }
}