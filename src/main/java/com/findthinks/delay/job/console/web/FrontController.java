package com.findthinks.delay.job.console.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author YuBo
 */
@Controller
public class FrontController {
    @RequestMapping(value = {
            "/login",
            "/"})
    public String front() {
        return "forward:/index.html";
    }
}
