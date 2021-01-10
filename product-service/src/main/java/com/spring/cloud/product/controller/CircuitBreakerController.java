package com.spring.cloud.product.controller;

import com.spring.cloud.common.util.ThreadUtil;
import com.spring.cloud.common.vo.ResultMessage;
import com.spring.cloud.product.facade.HystrixFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class CircuitBreakerController {

    @Autowired
    private HystrixFacade hystrixFacade;

    @GetMapping("/cr/timeout")
    public ResultMessage timeout() {
        return hystrixFacade.timeout();
    }

    @GetMapping("/cr/exp/{msg}")
    public ResultMessage exp(@PathVariable("msg") String msg) {
        return hystrixFacade.exp(msg);
    }

    @GetMapping("/cr/sleep/{timeout}")
    public ResultMessage sleep(@PathVariable("timeout") long timeout) {
        long start = System.currentTimeMillis();
        try {
            return hystrixFacade.sleep(timeout);
        } finally {
            System.out.println("cost: " + (System.currentTimeMillis() - start) / 1000);
        }
    }

    @GetMapping("/cr/async/{timeout}")
    public ResultMessage async(@PathVariable("timeout") long timeout) {
        long start = System.currentTimeMillis();
        try {
            Future<ResultMessage> future = hystrixFacade.async(timeout);
            ThreadUtil.sleep(1000);
            try {
                return future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ResultMessage(false, "系统未知异常");
        } finally {
            System.out.println("cost: " + (System.currentTimeMillis() - start) / 1000);
        }
    }
}
