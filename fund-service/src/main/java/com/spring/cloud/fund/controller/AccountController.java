package com.spring.cloud.fund.controller;

import com.spring.cloud.common.util.ThreadUtil;
import com.spring.cloud.common.vo.ResultMessage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AccountController {

    @PostMapping("/account/balance/{userId}/{amount}")
    public ResultMessage deductingBalance(
            @PathVariable("userId") Long userId,
            @PathVariable("amount") Double amount,
            HttpServletRequest request) {
        ThreadUtil.sleep(120 * 1000);
        String message = "端口：【" + request.getServerPort() + "】扣减成功";
        return new ResultMessage(true, message);
    }
}
