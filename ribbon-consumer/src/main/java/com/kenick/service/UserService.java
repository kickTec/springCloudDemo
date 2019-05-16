package com.kenick.service;


import com.kenick.cmd.UserGetCommand;
import com.kenick.cmd.UserPostCommand;
import com.kenick.pojo.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(commandKey = "getUserById",groupKey = "UserGroup",threadPoolKey = "getUserByIdThread")
    public User getUserById(Long id){
        // 同步执行
        return restTemplate.getForObject("http://HELLO-SERVICE/getUserById?id={1}",User.class,id);
    }

    @HystrixCommand(commandKey = "getUserByIdAsync",groupKey = "UserGroup",threadPoolKey = "getUserByIdThread")
    public Future<User> getUserByIdAsync(Long id){
        // 异步执行
        return new AsyncResult<User>() {
            @Override
            public User invoke() {
                return restTemplate.getForObject("http://HELLO-SERVICE/getUserById?id={1}",User.class,id);
            }
        };
    }

    public User getUserByCommand(Long id){
        UserGetCommand userGetCommand = new UserGetCommand(restTemplate, id);
        return userGetCommand.execute();
    }

    public User getUserByCommandAsync(Long id){
        User user = null;
        try {
            Future<User> userFuture = new UserGetCommand(restTemplate, id).queue();
            user = userFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @HystrixCommand(commandKey = "postUserSubmit",groupKey = "UserGroup",threadPoolKey = "postUser")
    public String postUserSubmit(User user){
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://HELLO-SERVICE/postUserSubmit", user, String.class);
        return responseEntity.getBody();
    }

    public String postUserCommand(User user){
        UserPostCommand userPostCommand = new UserPostCommand(restTemplate,user);
        return userPostCommand.execute().toString();
    }
}
