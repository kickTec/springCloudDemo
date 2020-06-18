package com.kenick.extend.interfaces;

import com.kenick.pojo.User;
import org.springframework.stereotype.Component;

@Component
public class IHelloServiceFallback implements IHelloService {

    @Override
    public String hello(String name) {
        return "hello error";
    }

    @Override
    public String postUserSubmit(User user) {
        return "post user error";
    }

    @Override
    public String addUser(String userId, String name, Integer age) {
        return "helloAddUser error";
    }

}
