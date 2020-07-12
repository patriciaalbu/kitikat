package com.ciutz.kitikat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ResourcesController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }
}
