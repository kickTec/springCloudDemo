package com.kenick.service;

import com.kenick.pojo.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hello-service",fallback = HelloServiceFallback.class)
public interface HelloService {

    @RequestMapping("/hello")
    String hello(@RequestParam("name") String name);

    @RequestMapping(value ="/postUserSubmit")
    String postUserSubmit(@RequestBody User user);
}
