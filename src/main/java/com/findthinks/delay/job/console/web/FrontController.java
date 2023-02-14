package com.findthinks.delay.job.console.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontController {

    @RequestMapping(value = {
            "/login",
            "/dashboard/**",
            "/dashboard/**",
            "/scheduler/**",
            "/shard/**",
            "/job/**",
            "/about/**",
            "/"})
    public String front() {
        return "forward:/index.html";
    }
}
