package org.cloudfoundry.samples.music.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DieController {
    @ResponseBody
    @RequestMapping(value = "/die")
    public void die() {
        System.exit(1);
    }
}