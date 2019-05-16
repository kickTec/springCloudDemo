package com.kenick.cmd;

import com.kenick.pojo.User;
import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.springframework.web.client.RestTemplate;

public class UserGetCommand extends HystrixCommand<User> {
    private static final HystrixCommandKey GETTER_KEY = HystrixCommandKey.Factory.asKey("CommandKey");
    private RestTemplate restTemplate;
    private Long id;

    public UserGetCommand(RestTemplate restTemplate, Long id){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CommandGroupKey"))
                .andCommandKey(GETTER_KEY)
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("UserGetThreadKey")));
        this.restTemplate = restTemplate;
        this.id = id;
    }

    @Override
    protected User run(){
        return restTemplate.getForObject("http://HELLO-SERVICE/getUserById?id={1}",User.class,id);
    }

    @Override
    protected User getFallback() {
        return new User();
    }

    @Override
    protected String getCacheKey() {
        return String.valueOf(id);
    }

    // 刷新缓存 根据id清理
    public static void flushCache(Long id){
        HystrixRequestCache instance = HystrixRequestCache.getInstance(GETTER_KEY, HystrixConcurrencyStrategyDefault.getInstance());
        if(instance != null){
            instance.clear(String.valueOf(id));
        }
    }
}
