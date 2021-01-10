package com.spring.cloud.product.facade.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.spring.cloud.common.vo.ResultMessage;
import com.spring.cloud.product.facade.HystrixFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

@Service
public class HystrixFacadeImpl implements HystrixFacade {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @HystrixCommand(fallbackMethod = "timeoutFallback")
    public ResultMessage timeout() {
        String url = "http://user-service/hystrix/timeout";
        return restTemplate.getForObject(url, ResultMessage.class);
    }

    public ResultMessage timeoutFallback() {
        return new ResultMessage(false, "超时了");
    }

    @Override
    @HystrixCommand(fallbackMethod = "expFallback")
    public ResultMessage exp(String msg) {
        String url = "http://user-service/hystrix/exp/{msg}";
        return restTemplate.getForObject(url, ResultMessage.class, msg);
    }

    public ResultMessage expFallback(String msg) {
        return new ResultMessage(false, "调用产生异常了，参数：" + msg);
    }

    @Override
    @HystrixCommand(threadPoolKey = "pool-user-1", fallbackMethod = "sleepFallback")
    public ResultMessage sleep(long timeout) {
        String url = "http://user-service/hystrix/sleep/{timeout}";
        return restTemplate.getForObject(url, ResultMessage.class, timeout);
    }



    public ResultMessage sleepFallback(long timeout) {
        return new ResultMessage(false, "降级了，参数：" + timeout);
    }

    /**
     * hystrix.command.async
     * hystrix.threadpool.pool-user-2
     * @param timeout
     * @return
     */
    @Override
    @HystrixCommand(threadPoolKey = "pool-user-2", fallbackMethod = "asyncFallback")
    public Future<ResultMessage> async(long timeout) {
        return new AsyncResult<ResultMessage>() {
            @Override
            public ResultMessage invoke() {
                String url = "http://user-service/hystrix/sleep/{timeout}";
                return restTemplate.getForObject(url, ResultMessage.class, timeout);
            }
        };
    }

    public ResultMessage asyncFallback(long timeout) {
        return new ResultMessage(false, "async降级了，参数：" + timeout);
    }
}
