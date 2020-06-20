package com.kenick.extend.interfaces;

import org.springframework.stereotype.Component;

@Component
public class IHelloServiceFallback implements IHelloService {

    @Override
    public String hello(String name) {
        return "hello error";
    }

    @Override
    public String addUser(String userId, String name, Integer age) {
        return "helloAddUser error";
    }

}
