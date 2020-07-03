package com.kenick.contoller;

import com.alibaba.fastjson.JSONObject;
import com.kenick.pojo.User;
import com.kenick.user.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HelloController {
    private final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Resource
    private DiscoveryClient client;

    @Autowired
    private IUserService userService;

    @RequestMapping("/hello")
    public String hello(@RequestParam String name){
        return "Hello " + name;
    }

    @RequestMapping("/getUserById")
    public User getUserById(Long id){
        User user = new User();
        user.setId(id);
        user.setAge(id.intValue());
        user.setName("name"+id);
        return user;
    }

    @RequestMapping(value ="/postUserSubmit",method = RequestMethod.POST)
    public String postUserSubmit(@RequestBody User user){
        System.out.println("提交的user为:" + user.toString());
        return user.toString()+"已提交!";
    }

    @RequestMapping("/addUser")
    public String addUser(@RequestParam("userId") String userId,@RequestParam("name") String name,@RequestParam("age") Integer age){
        logger.debug("HelloController.addUser in,userId:{}", userId);
        JSONObject retJson = new JSONObject();
        retJson.put("userId", userId);
        try{
            int saveRet = userService.saveUser(userId, name, age);
            retJson.put("ret", saveRet);
            retJson.put("success", true);
        }catch (Exception e){
            retJson.put("success", false);
            retJson.put("error", e.getMessage());
        }
        return retJson.toJSONString();
    }
}
