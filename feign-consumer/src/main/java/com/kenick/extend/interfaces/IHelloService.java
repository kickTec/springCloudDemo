package com.kenick.extend.interfaces;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// @FeignClient(name = "hello-service",fallback = IHelloServiceFallback.class)
@FeignClient(name = "hello-service")
public interface IHelloService {

    @RequestMapping("/hello")
    String hello(@RequestParam("name") String name);

    @RequestMapping(value ="/addUser")
    String addUser(@RequestParam("userId") String userId,@RequestParam("name") String name,@RequestParam("age") Integer age);
}
