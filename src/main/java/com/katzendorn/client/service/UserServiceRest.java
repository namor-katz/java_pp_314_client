package com.katzendorn.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.katzendorn.client.entity.User;
import com.katzendorn.client.entity.Role;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
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

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceRest implements UserDetailsService {

    private final RestTemplate restTemplate;
    private final String serverUrl;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    //try base authentication in headers
    HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }

    //новая фишка. берется переменная из настроечного файла.
    public UserServiceRest(RestTemplate restTemplate, @Value("${application.server.url}") String serverUrl) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
    }

    public List<User> allUsers() {
        //try base authentication from rest api. Require support in server.
        HttpHeaders hh = createHeaders("roman", "logrys7");
        HttpEntity<String> request = new HttpEntity<>(hh);

       return restTemplate.exchange(
               serverUrl + "/api/v1/users",
               HttpMethod.GET,
               request,
               new ParameterizedTypeReference<List<User>>() {
               }
       ).getBody();
    }

    public User findUserById(Long id) {
        /*
        String plainCreds = "roman:logrys7";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
         */
        HttpHeaders hh = createHeaders("roman", "logrys7");
        HttpEntity<String> request = new HttpEntity<>(hh);

        User user = null;
        RestTemplate restTemplate = new RestTemplate();
        String urlFromGetUserById = serverUrl + "/api/v1/user/info/" + id;
        try {
//            user = restTemplate.getForObject(urlFromGetUserById, User.class);
            User u = restTemplate.exchange(urlFromGetUserById, HttpMethod.GET, request, User.class).getBody();
            return u;
        } catch (Exception e) {
            System.out.println("Don't find user!");
            return user;
        }
    }

    public User getUserByName(String name) {
        HttpHeaders hh = createHeaders("roman", "logrys7");
        HttpEntity<String> request = new HttpEntity<>(hh);

        RestTemplate restTemplate = new RestTemplate();
        String serverUrlByGetByName = serverUrl + "/api/v1/user/info2/" + name;
        User user = null;
        try {
            //user = restTemplate.getForObject(serverUrlByGetByName, User.class);
            user = restTemplate.exchange(serverUrlByGetByName, HttpMethod.GET, request, User.class).getBody();
        } catch (Exception e ) {
            System.out.println("данный пользователь не обнаружен!");
        }
        return user;
    }

    public boolean saveUser(User user) {
        HttpHeaders hh = createHeaders("roman", "logrys7");
        hh.setContentType(MediaType.APPLICATION_JSON);

        String json = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            System.out.println("не удалось конвертировать объект в json");
        }

        HttpEntity<String> request = new HttpEntity<>(json, hh);

        User userFormDB = getUserByName(user.getUsername());

        if(userFormDB != null) {
            return false;
        }
        String saveUrl = serverUrl + "/api/v1/user/new";
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.exchange(saveUrl, HttpMethod.POST, request, User.class);
            return true;
        } catch (Exception e) {
            System.out.println("увы, сохранение не удалось!");
            e.printStackTrace();
            return false;
        }
    }


    public void updateUser(User user) {
        HttpHeaders hh = createHeaders("roman", "logrys7");
        hh.setContentType(MediaType.APPLICATION_JSON);
        String json = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            System.out.println("юзер апдейт, преобразование юзера в json failed");
        }

        HttpEntity<String> request = new HttpEntity<>(json, hh);

        String putUrl = serverUrl + "/api/v1/user/update/" + user.getId();
        RestTemplate restTemplate = new RestTemplate();
        try {
            //restTemplate.put(putUrl, user, User.class);
            restTemplate.exchange(putUrl, HttpMethod.PUT, request, User.class);
        } catch (Exception e) {
            System.out.println("что то пошло не так, сохранить изменени в пользователе не удалось!");
        }
    }

    public boolean deleteUser(Long id) {
        String urlFromUserDelete = serverUrl + "/api/v1/user/delete/" + id;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders hh = createHeaders("roman", "logrys7");
        HttpEntity<String> request = new HttpEntity<>(hh);
        try {
            restTemplate.exchange(urlFromUserDelete, HttpMethod.DELETE, request, User.class);
            return true;
        } catch ( Exception e) {
            System.out.println("удаление не удалось.");
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
