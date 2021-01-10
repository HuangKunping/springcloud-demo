package com.spring.cloud.product.facade;

import com.spring.cloud.common.vo.ResultMessage;

import java.util.concurrent.Future;

public interface HystrixFacade {

    ResultMessage timeout();

    ResultMessage exp(String msg);

    ResultMessage sleep(long timeout);

    Future<ResultMessage> async(long timeout);
}
