package com.katzendorn.client.controller;

import com.katzendorn.client.entity.Role;
import com.katzendorn.client.entity.User;
import com.katzendorn.client.service.UserServiceRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserServiceRest userService;

    @GetMapping(value = "list")
    public String listPage(ModelMap model) {
//        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();    //ни магу скастовать ин меморю к юзеру!! арря!

        //create fake user rom debug begin
        Role role = new Role(1L, "ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User principal = new User(null, "vasya", "123", "v@email.ru", 124, roles);
//        principal.setRoles(roles);
        model.addAttribute("you", principal);
        //end create fake user

        return "list";
    }
}
