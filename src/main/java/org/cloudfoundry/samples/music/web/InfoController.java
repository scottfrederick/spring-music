package org.cloudfoundry.samples.music.web;

import io.pivotal.cfenv.core.CfEnv;
import io.pivotal.cfenv.core.CfService;
import org.cloudfoundry.samples.music.domain.ApplicationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class InfoController {
    private final CfEnv cfEnv;

    private Environment springEnvironment;

    @Autowired
    public InfoController(Environment springEnvironment) {
        this.springEnvironment = springEnvironment;
        this.cfEnv = new CfEnv();
    }

    @RequestMapping(value = "/appinfo")
    public ApplicationInfo info() {
        return new ApplicationInfo(springEnvironment.getActiveProfiles(), getServiceNames());
    }

    @RequestMapping(value = "/service")
    public List<CfService> showServiceInfo() {
        return cfEnv.findAllServices();
    }

    private String[] getServiceNames() {
        List<CfService> services = cfEnv.findAllServices();

        List<String> names = new ArrayList<>();
        for (CfService service : services) {
            names.add(service.getName());
        }
        return names.toArray(new String[0]);
    }
}