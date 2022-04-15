package com.vansn.stock.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author vansn
 * @Date 2022/1/22 下午8:54
 * @Version 1.0
 * @Description
 */
@RestController
@RequestMapping("stock")
public class StockController {

    @Value("${server.port}")
    String port;

    @RequestMapping("reduct")
    public String reduct(){
        System.out.println("扣减库存");
        return "扣减成功"+port;

    }

}
