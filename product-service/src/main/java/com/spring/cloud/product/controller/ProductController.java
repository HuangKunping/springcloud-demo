package com.spring.cloud.product.controller;

import com.spring.cloud.common.vo.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/purchase/{userId}/{productId}/{amount}")
    public ResultMessage purchaseProduct(
            @PathVariable("userId") Long userId,
            @PathVariable("productId") Long productId,
            @PathVariable("amount") Double amount,
            HttpServletRequest request) {
        long start = System.currentTimeMillis();
        try {
            System.out.println("扣减产品余额。");
            String url = "http://fund-service/account/balance/{userId}/{amount}";
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("amount", amount);
            ResultMessage rm = restTemplate.postForObject(url, null, ResultMessage.class,
                    params);
            System.out.println(rm.getMessage());
            System.out.println("记录交易信息");
            return new ResultMessage(true, "交易成功");
        } catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
            return new ResultMessage(false, "客户端请求异常");
        } finally {
            System.out.println("cost: " + (System.currentTimeMillis() - start)/1000);
        }
    }
}
