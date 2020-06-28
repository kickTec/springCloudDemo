package com.kenick.controller;

import com.kenick.extend.interfaces.IHelloService;
import com.kenick.user.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HelloController {
    private final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Resource
    private IHelloService helloService;

    @Resource
    private IUserService userService;

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String hello(String name){
        return helloService.hello(name);
    }

    @RequestMapping("/addUser")
    public String addUser(@RequestParam("userId") String userId, @RequestParam("name") String name, @RequestParam("age") Integer age){
        logger.debug("HelloController.addUser in,userId:{}", userId);
        return "ret: " + userService.saveUser(userId,name,age);
    }

}
