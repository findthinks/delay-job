package com.findthinks.delay.job.console.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author YuBo
 */
@Controller
public class FrontController {
    @RequestMapping(value = {
            "/dashboard/monitor",
            "/analyse",
            "/service",
            "/router",
            "/access/app",
            "/access/quota",
            "/access/white_list",
            "/login",
            "faq",
            "/"})
    public String front() {
        return "forward:/index.html";
    }
}
