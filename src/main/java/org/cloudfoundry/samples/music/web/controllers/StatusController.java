package org.cloudfoundry.samples.music.web.controllers;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Controller
public class StatusController {
    @RequestMapping(value = "/properties")
    @ResponseBody
    public Map<String, String> properties() throws Throwable {
        Properties props = System.getProperties();
        Map<String, String> kvs = new HashMap<String, String>();
        for (String k : props.stringPropertyNames())
            kvs.put(k, props.getProperty(k));
        return kvs;
    }

    @RequestMapping(value = "/env")
    @ResponseBody
    public Map<String, String> env() throws Throwable {
        return System.getenv();
    }

    @ResponseBody
    @RequestMapping(value = "/status")
    public CloudEnvironment showCloudEnvironment() {
        CloudEnvironment cloudEnvironment = new CloudEnvironment();
        if (cloudEnvironment.isCloudFoundry())
            return cloudEnvironment;
        else
            return null;
    }
}