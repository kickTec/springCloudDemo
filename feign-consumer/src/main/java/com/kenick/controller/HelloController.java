package com.kenick.controller;

import com.kenick.extend.interfaces.IHelloService;
import com.kenick.pojo.User;
import com.kenick.user.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private IHelloService helloService;

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String hello(String name){
        return helloService.hello(name);
    }

    @RequestMapping(value = "/submitUser",method = RequestMethod.GET)
    public String postUserSubmit(Long id,String name){
        User user = new User();
        user.setId(id);
        user.setName(name);
        return helloService.postUserSubmit(user);
    }

    @RequestMapping("/addUser")
    public String addUser(@RequestParam("userId") String userId, @RequestParam("name") String name, @RequestParam("age") Integer age){
        return "ret: " + userService.saveUser(userId,name,age);
    }

}
