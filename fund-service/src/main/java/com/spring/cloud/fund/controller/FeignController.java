package com.spring.cloud.fund.controller;

import com.spring.cloud.common.util.ThreadUtil;
import com.spring.cloud.common.vo.User;
import com.spring.cloud.fund.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/feign")
public class FeignController {

    @Autowired
    private UserFacade userFacade;

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userFacade.getUser(id);
    }

    @GetMapping("/users/{timeout}")
    public List<User> getUsers(@PathVariable("timeout") Long timeout) {
        long start = System.currentTimeMillis();
        try {
            return userFacade.getUsers(timeout);
        } finally {
            System.out.println("cost: " + (System.currentTimeMillis() - start) / 1000);
        }
    }

    @GetMapping("/all-users/{timeout}")
    public List<User> getAllUsers(@PathVariable("timeout") Long timeout) {
        long start = System.currentTimeMillis();
        try {
            Future<List<User>> users = userFacade.getAllUsers(timeout).queue();
            ThreadUtil.sleep(1000);
            try {
                return users.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } finally {
            System.out.println("cost: " + (System.currentTimeMillis() - start) / 1000);
        }
    }
}
