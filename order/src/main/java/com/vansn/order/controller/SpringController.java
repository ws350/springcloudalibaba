package com.vansn.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author vansn
 * @Date 2022/1/26 上午8:57
 * @Version 1.0
 * @Description
 */
@RestController
@RequestMapping("spring")
public class SpringController {
    @Autowired
    Environment e;

    @Value("${user.age}")
    String age;

    @RequestMapping("env")
    public void wired(){
        System.out.println(e.getProperty("user.name")+e.getProperty("user.age") );
        System.out.println("age = "+age);
    }


}
