package com.kenick.contoller;

import com.alibaba.fastjson.JSONObject;
import com.kenick.pojo.User;
import com.kenick.user.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

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
        ServiceInstance instance = client.getLocalServiceInstance();
        try {
            Double randomTime = Math.random() * 3000;
            Thread.sleep(randomTime.longValue());
            logger.info("hello sleep time:"+randomTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("/hello,host:{},service_id:{},port:{}", instance.getHost(), instance.getServiceId(),instance.getPort());
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
            // int saveRet = userService.saveUser(userId, name, age);
            int saveRet = userService.saveUserByMsgTx(userId, name, age);
            retJson.put("ret", saveRet);
            retJson.put("success", true);
        }catch (Exception e){
            retJson.put("success", false);
            retJson.put("error", e.getMessage());
        }
        return retJson.toJSONString();
    }
}
