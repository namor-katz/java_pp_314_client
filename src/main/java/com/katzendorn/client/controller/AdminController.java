package com.katzendorn.client.controller;

import com.katzendorn.client.service.User;
import com.katzendorn.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "list")
    public String listPage(ModelMap model) {
        System.out.println("Зашёл в админ контроллер!");
//        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();    //ни магу скастовать ин меморю к юзеру!! арря!
        User principal = new User("Vasy", "e@vas", "ADMIN_role");
        model.addAttribute("you", principal);
        return "list";
    }
}
