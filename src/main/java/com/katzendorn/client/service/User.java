package com.katzendorn.client.service;

import java.util.Set;

public class User {

    private Long id;

    private String username;

    private String password;

    private String email;

    private int maxweigth;

    private String roles; //??!!


    //constructor

    public User(String username, String email, String roles) {
        this.username = username;
//        this.password = password;
        this.email = email;
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

    public int getMaxweigth() {
        return maxweigth;
    }

    public String getRoles() {
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

    public void setMaxweigth(int maxweigth) {
        this.maxweigth = maxweigth;
    }
}
