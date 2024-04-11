package com.nick.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class DemoApplication {
    @Autowired
    private RedisTemplate redisTemplate;


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
