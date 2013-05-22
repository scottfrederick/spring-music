package org.cloudfoundry.samples.music.web.controllers;

import org.cloudfoundry.samples.music.domain.ApplicationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InfoController {
    private final String[] profiles;

    @Autowired
    public InfoController(Environment environment) {
        this.profiles = environment.getActiveProfiles();
    }

    @ResponseBody
    @RequestMapping(value = "/info")
    public ApplicationInfo info() {
        return new ApplicationInfo(profiles);
    }
}