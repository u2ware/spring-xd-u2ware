package io.github.u2ware.xd.ibs.helloworld;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloworldController {

    protected Log logger = LogFactory.getLog(getClass());

    @RequestMapping(value="/helloworld", produces = "text/html; charset=utf-8")
    public @ResponseBody String world(HttpServletRequest request) {
    	
    	logger.debug("sample1");
    	logger.debug("sample1");
    	logger.debug("sample1");
    	
        return "hello world";
    }
}