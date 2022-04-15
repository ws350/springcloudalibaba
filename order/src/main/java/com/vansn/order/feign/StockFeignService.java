package com.vansn.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author vansn
 * @Date 2022/1/24 下午3:58
 * @Version 1.0
 * @Description
 */
@FeignClient(name="stock-service",path="/stock")
public interface StockFeignService {

    @RequestMapping("/reduct")
    public String reduct();

}
