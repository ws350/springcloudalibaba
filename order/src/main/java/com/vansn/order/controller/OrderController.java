package com.vansn.order.controller;


import com.vansn.order.feign.StockFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    StockFeignService stockFeignService;


    @RequestMapping("add")
    public String add(){
        System.out.println("增加成功");

        //String msg = restTemplate.getForObject("http://localhost:8011/stock/reduct", String.class);
        //使用服务名来调用
        String msg = restTemplate.getForObject("http://stock-service/stock/reduct", String.class);
        return "Hello Spring Cloud Alibaba"+msg;
    }

    @RequestMapping("addFeign")
    public String addByFeign(){
        System.out.println("下单成功");
        String msg = stockFeignService.reduct();
        return "Feignadd" +msg;
    }

}
