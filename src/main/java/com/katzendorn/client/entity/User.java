package com.katzendorn.client.entity;

import java.util.Set;

public class User {

    private Long id;

    private String username;

    private String password;

    private String email;

    private int maxweight;

    private Set<Role> roles; //??!!


    //constructor

    public User() {}

    public User(Set<Role> roles) {
        this.roles = roles;
    }


    //this constructor required from rest controller, from create user without password
    public User(Long id, String username, String email, Set<Role> roles, int maxweight) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.maxweight = maxweight;
    }

    //full constructor
    public User(Long id, String username, String password, String email, int maxweigth, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.maxweight = maxweigth;
        this.roles = roles;
    }

//getters and setters

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getMaxweight() {
        return maxweight;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMaxweight(int maxweight) {
        this.maxweight = maxweight;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    //create user without password
    public static User UserRest(User user) {
        return new User(user.getId(), user.getUsername(), user.getEmail(), user.getRoles(), user.getMaxweight());
    }
}
