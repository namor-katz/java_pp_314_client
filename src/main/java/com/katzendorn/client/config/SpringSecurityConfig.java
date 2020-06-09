package com.katzendorn.client.config;

import com.katzendorn.client.entity.Role;
import com.katzendorn.client.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.katzendorn.client.config.handler.LoginSuccessHandler;

import java.util.Set;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
//                .antMatchers("/registration").not().fullyAuthenticated()
                .antMatchers("/api/v1/**").permitAll()  //!! сменить на только для одминов!
                .antMatchers("/", "/login", "/static/**/*", "/css/**", "/js/**").permitAll()  //dвот зе фак?
                .antMatchers("/admin/**", "/registration").permitAll()             //hasRole("ADMIN")
                .antMatchers("/user").hasRole("USER")
                //use authentification
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(new LoginSuccessHandler())
                .permitAll();

//        httpSecurity.ignoring().antMatchers("/resources/**/*", "/resources/**", "/resources/css/**");



        http.logout()
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .and().csrf().disable();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**/*","/webjars/**", "/static/css/*");
    }

/*
    public void registerGlobal(AuthenticationManagerBuilder auth) throws Exception {
        User admin = new User(1L, "admin", "123", "a@mail.ru", 125, (Set<Role>) new Role(1L, "ADMIN"));
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER").and()
                .withUser("admin").password("password").roles("USER","ADMIN", "ADMIN_ROLES").and()
        .withUser((admin.getUsername()).password(admin.getPassword()).roles(admin.getRoles()));
    }
*/
/*
    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }

 */
}   //end class
