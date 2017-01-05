package com.izbicki.jakub.Security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Properties;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public final static String ROLE_ADMIN = "ADMIN";
    public final static String ROLE_USER = "USER";


    /**
     * Determines the resource access for different account types
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
//                    .antMatchers("/actors/**").permitAll()
//                    .antMatchers("/movies/**").hasRole(ROLE_ADMIN)
//                    .antMatchers("/movies").hasRole(ROLE_USER)
                    .anyRequest().authenticated()
                .and()
                    .httpBasic();
    }


//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth.inMemoryAuthentication()
//                .withUser("admin").password("pass").roles(ROLE_ADMIN).and()
//                .withUser("user").password("pass").roles(ROLE_USER);
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsManager());
    }

    /**
     * Initially fills Spring Security with default accounts
     */
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {

        final Properties users = new Properties();
        users.put("user","pass,ROLE_USER,enabled");
        users.put("admin","pass,ROLE_ADMIN,enabled");
        return new InMemoryUserDetailsManager(users);
    }
}
