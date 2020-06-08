package com.katzendorn.client.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
public class UserServiceRest implements UserService {

    private final RestTemplate restTemplate;
    private final String serverUrl;

    //новая фишка. берется переменная из настроечного файла.
    public UserServiceRest(RestTemplate restTemplate, @Value("${application.server.url}") String serverUrl) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
    }

    @Override
    public List<User> allUsers() {
//        System.out.println("мое ёбаный адрес есть " + serverUrl);
       return restTemplate.exchange(
               serverUrl + "/api/v1/users",
               HttpMethod.GET,
               null,
               new ParameterizedTypeReference<List<User>>() {
               }
       ).getBody();
    }
}
