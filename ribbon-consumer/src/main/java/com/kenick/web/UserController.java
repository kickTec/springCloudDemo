package com.kenick.web;

import com.kenick.pojo.User;
import com.kenick.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

@RestController
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "/getUserById",method = RequestMethod.GET)
    public String getUserById(Long id){
        return userService.getUserById(id).toString();
    }

    @RequestMapping(value = "/getUserByIdAsync",method = RequestMethod.GET)
    public String getUserByIdAsync(Long id){
        String ret = "";
        try {
            Future<User> userFuture = userService.getUserByIdAsync(id);
            ret = userFuture.get().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @RequestMapping(value = "/getUserByCommand",method = RequestMethod.GET)
    public String getUserByCommand(Long id){
        return userService.getUserByCommand(id).toString();
    }

    @RequestMapping(value = "/getUserByCommandAsync",method = RequestMethod.GET)
    public String getUserByCommandAsync(Long id){
        return userService.getUserByCommandAsync(id).toString();
    }

    @RequestMapping(value = "/postUserSubmit",method = RequestMethod.GET)
    public String postUserSubmit(Long id,String name){
        User user = new User();
        user.setId(id);
        user.setName(name);
        return userService.postUserSubmit(user);
    }

    @RequestMapping(value = "/postUserCommand",method = RequestMethod.GET)
    public String postUserCommand(Long id,String name){
        User user = new User();
        user.setId(id);
        user.setName(name);
        String ret = userService.postUserCommand(user);
        return ret;
    }
}
