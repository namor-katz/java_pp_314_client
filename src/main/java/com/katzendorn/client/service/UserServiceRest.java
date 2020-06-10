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

    public User getUserByName(String name) {
        RestTemplate restTemplate = new RestTemplate();
        String serverUrlByGetByName = serverUrl + "/api/v1/user/info2/" + name;
        User user = restTemplate.getForObject(serverUrlByGetByName, User.class);
        return user;
    }

    public boolean saveUser(User user) {
        System.out.println("я прямиком из UserServiceRest.saveUser, моё имя " + user.getUsername());
        String serverUrlGetInfo2 = serverUrl + "/api/v1/user/info2/" + user.getUsername();

        User userFormDB = null;
        try {
            userFormDB = getUserByName(user.getUsername());    //аналог стр 56
            System.out.println("ОХООХ! я получил имя юзера!");
        } catch (Exception e) {
            System.out.println("ЧтОто пошлО не ТаК");
            e.printStackTrace();
        }
        System.out.println("я есть полученный по имени пользователь! " + userFormDB);

        if(userFormDB != null) {
            System.out.println("С вами снова saveUser, и если я тут, то ты повторился с именем, такой юзер давно есть!");
            return false;   //такой юзер уже есть, НЕ добавляем.
        }
        String postUrl = serverUrl + "/api/v1/user/new";
        System.out.println("щас пошлю пост запрос на адрес "+ postUrl);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        RestTemplate restTemplate = new RestTemplate();
        User addedUser = restTemplate.postForObject(postUrl, user, User.class);
        //отправить постом в апи нею

        return true;
    }

    @Override   //!! edit! add method!
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = restTemplate.exchange(
//                serverUrl + "/api/v1/users/info"
//        )
        return null;
    }
}
