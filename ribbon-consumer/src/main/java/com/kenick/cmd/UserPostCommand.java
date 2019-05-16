package com.kenick.cmd;

import com.kenick.pojo.User;
import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class UserPostCommand extends HystrixCommand<User> {
    private static final HystrixCommandKey GETTER_KEY = HystrixCommandKey.Factory.asKey("PostCommandKey");
    private RestTemplate restTemplate;
    private User user;

    public UserPostCommand(RestTemplate restTemplate, User user){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("PostCommandGroupKey"))
                .andCommandKey(GETTER_KEY)
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("UserPostThreadKey")));
        this.restTemplate = restTemplate;
        this.user = user;
    }

    @Override
    protected User run() throws Exception {
        // 写操作
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://HELLO-SERVICE/postUserSubmit", user, String.class);
        try {
            // 刷新缓存，清理缓存中失效的User
            UserGetCommand.flushCache(this.user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.user.setName(responseEntity.getBody());
        return user;
    }

    @Override
    protected User getFallback() {
        return new User();
    }

}
