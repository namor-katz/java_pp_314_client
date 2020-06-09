package com.katzendorn.client.controller;

import com.katzendorn.client.entity.Role;
import com.katzendorn.client.entity.User;
import com.katzendorn.client.service.UserServiceRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class RegistrationController {

    @Autowired
    private UserServiceRest userService;

    @GetMapping("/registration")
    public String registration(Model model) {
//        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //begin fake user
        Role role = new Role(1L, "ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User principal = new User(null, "vasya", "123", "v@email.ru", 124, roles);
        model.addAttribute("you", principal);
        //end fake user

        return "registration";
    }
}
