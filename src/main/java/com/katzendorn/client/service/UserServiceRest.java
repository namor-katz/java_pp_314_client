package com.katzendorn.client.service;

import com.katzendorn.client.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
public class UserServiceRest implements UserDetailsService {

    private final RestTemplate restTemplate;
    private final String serverUrl;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    //новая фишка. берется переменная из настроечного файла.
    public UserServiceRest(RestTemplate restTemplate, @Value("${application.server.url}") String serverUrl) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
    }

    public List<User> allUsers() {
        System.out.println("run allUsers ");
       return restTemplate.exchange(
               serverUrl + "/api/v1/users",
               HttpMethod.GET,
               null,
               new ParameterizedTypeReference<List<User>>() {
               }
       ).getBody();
    }

    public User findUserById(Long id) {
        System.out.println("пробую получить данные с 8081 ");
        User user = restTemplate.exchange(
                serverUrl + "/api/v1/user/info/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<User>() {
                }
        ).getBody();
        System.out.println("Я юзер из юзер сервисе! моё имя " + user.getUsername());
        return user;
    }

    @Override   //!! edit! add method!
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = restTemplate.exchange(
//                serverUrl + "/api/v1/users/info"
//        )
        return null;
    }
}
