package com.katzendorn.client.service;

import com.katzendorn.client.entity.User;
import com.katzendorn.client.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;
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
        User user = null;
        RestTemplate restTemplate = new RestTemplate();
        String urlFromGetUserById = serverUrl + "/api/v1/user/info/" + id;
        System.out.println(urlFromGetUserById + " тут дёргаю данные по id!");
        try {
            user = restTemplate.getForObject(urlFromGetUserById, User.class);
            return user;
        } catch (Exception e) {
            System.out.println("Don't find user!");
            return user;
        }
    }

    public User getUserByName(String name) {
        RestTemplate restTemplate = new RestTemplate();
        String serverUrlByGetByName = serverUrl + "/api/v1/user/info2/" + name;
        User user = null;
        try {
            user = restTemplate.getForObject(serverUrlByGetByName, User.class);
        } catch (Exception e ) {
            System.out.println("данный пользователь не обнаружен!");
        }
        return user;
    }

    public boolean saveUser(User user) {
        User userFormDB = getUserByName(user.getUsername());    //аналог стр 56

        if(userFormDB != null) {
            return false;
        }
        String postUrl = serverUrl + "/api/v1/user/new";
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        RestTemplate restTemplate = new RestTemplate();
        User addedUser = restTemplate.postForObject(postUrl, user, User.class);

        return true;
    }

    public boolean deleteUser(Long id) {
        System.out.println("пробую удалить юзера с id " + id);
        String urlFromUserDelete = serverUrl + "/api/v1/user/delete/" + id;
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.delete(urlFromUserDelete);
            return true;
        } catch ( Exception e) {
            return false;
        }
    }

    @Override   //!! edit! add method!
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = restTemplate.exchange(
//                serverUrl + "/api/v1/users/info"
//        )
        return null;
    }
}
