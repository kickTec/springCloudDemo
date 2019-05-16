package com.kenick.web;

import com.kenick.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    HelloService helloService;

    @RequestMapping(value = "/helloFallBack",method = RequestMethod.GET)
    public String helloFallBack(String name){
        String ret = helloService.hello(name);
        System.out.println("hello返回值:"+ret);
        return ret;
    }
}
