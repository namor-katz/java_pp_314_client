package com.katzendorn.client.controller;

import com.katzendorn.client.entity.User;
import com.katzendorn.client.service.UserServiceRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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

    @ApiOperation(value = "create new User", code = 201, response = User.class)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Succesfully created user")})
    @PostMapping(value = "user/new", consumes = {"application/json"})
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        System.out.println("this UserRestController, а я и есть юзер " + user.getUsername());
        user.setId(null);
        userServiceRest.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @ApiOperation(value = "Delete user by id", code = 204)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Delete completed")})
    @DeleteMapping(value = "user/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        User user = userServiceRest.findUserById(id);
        if(user.getUsername() == null) {
            System.out.println("don't find user");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userServiceRest.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
