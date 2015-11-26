package com.mycompany.myproject.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
public class HelloWorldController {

    protected Log logger = LogFactory.getLog(getClass());

    @RequestMapping(value="/world", produces = "text/html; charset=utf-8")
    public @ResponseBody String world(HttpServletRequest request) {
    	
    	logger.debug("world");
    	logger.debug("world");
    	logger.debug("world");
    	
        return "hello world";
    }
}