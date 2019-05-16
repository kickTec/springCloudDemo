package com.kenick.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {

    @Autowired
    private RestTemplate restTemplate;

    @CacheResult
    @HystrixCommand(fallbackMethod = "helloFallback1")
    public String hello(String name){
        long startTime = System.currentTimeMillis();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://HELLO-SERVICE/hello?name={1}", String.class, name);
        long endTime = System.currentTimeMillis();
        System.out.println("hello花费时间:"+(endTime-startTime));
        return responseEntity.getBody();
    }

    public String getHelloCacheKey(String name){
        return name;
    }

    @HystrixCommand(fallbackMethod = "helloFallback2")
    public String helloFallback1(String name){
        System.out.println("调用远程服务超时，开始第1次服务降级!");
        try {
            Double randomTime = Math.random() * 3000;
            Thread.sleep(randomTime.longValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "helloFallback1";
    }

    @HystrixCommand(fallbackMethod = "helloFallback2")
    public String helloFallback2(String name){
        System.out.println("调用远程服务超时，开始第2次服务降级!");
        return "helloFallback2";
    }
}
