package com.spring.cloud.user.controller;

import com.spring.cloud.common.util.ThreadUtil;
import com.spring.cloud.common.vo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return new User(id, "name@" + id, "note@" + id);
    }

    @GetMapping("/users/{timeout}")
    public List<User> getUsers(@PathVariable("timeout") long timeout) {
        List<User> users = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            User user = new User((long)i, "name@" + i, "note@" + i);
            users.add(user);
        }
        ThreadUtil.sleep(timeout);
        return users;
    }
}
