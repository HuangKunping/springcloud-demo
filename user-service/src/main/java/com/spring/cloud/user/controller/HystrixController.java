package com.spring.cloud.user.controller;

import com.spring.cloud.common.util.ThreadUtil;
import com.spring.cloud.common.vo.ResultMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hystrix")
public class HystrixController {
    private static long  MAX_SLEEP_TIME = 5000L;

    @GetMapping("/timeout")
    public ResultMessage timeout() {
        long sleepTime = (long) (MAX_SLEEP_TIME * Math.random());
        ThreadUtil.sleep(sleepTime);
        return new ResultMessage(true, "执行时间：" + sleepTime);
    }

    @GetMapping("/exp/{msg}")
    public ResultMessage exp(@PathVariable("msg") String msg) {
       if("spring".equals(msg)) {
           return new ResultMessage(true, msg);
       }
       throw new RuntimeException("出现了异常，请检查参数msg是否为spring");
    }

    @GetMapping("/sleep/{timeout}")
    public ResultMessage sleep(@PathVariable("timeout") long timeout) {
        ThreadUtil.sleep(timeout);
        return new ResultMessage(true, "执行时间：" + timeout);
    }
}
