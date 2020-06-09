package com.katzendorn.client.controller;

import com.katzendorn.client.entity.User;
import com.katzendorn.client.service.UserServiceRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class UserRestController {

    @Autowired
    private UserServiceRest userServiceRest;


    @CrossOrigin(origins = "*", maxAge = 3600)  //debug
    @GetMapping(value = "users")
    public List<User> getAllUsers() {
        return userServiceRest.allUsers().stream().map(User::UserRest)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "user/info/{id}")
    public User getUserInfo(@PathVariable String id) {
        Long id0 = Long.parseLong(id);
        User user = userServiceRest.findUserById(id0);
        user.setPassword(null);
        return user;
    }
}
