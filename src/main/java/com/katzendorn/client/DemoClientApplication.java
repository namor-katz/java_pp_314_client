package com.katzendorn.client;

import com.katzendorn.client.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.ApplicationContext;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@EnableCaching
@EnableHystrix
@SpringBootApplication
public class DemoClientApplication {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = SpringApplication.run(DemoClientApplication.class, args);
        UserService userService = ctx.getBean(UserService.class);

        System.out.println("юзера, блядь ");
        userService.allUsers().forEach(user -> System.out.println(user.getUsername()));
    }
}
