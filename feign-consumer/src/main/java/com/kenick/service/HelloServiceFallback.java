package com.kenick.service;

import com.kenick.pojo.User;
import org.springframework.stereotype.Component;

@Component
public class HelloServiceFallback implements HelloService{

    @Override
    public String hello(String name) {
        return "hello error";
    }

    @Override
    public String postUserSubmit(User user) {
        return "post user error";
    }
}
