package com.dominicjmarshall.springbootday1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.dominicjmarshall.springbootday1.util.constants.Privileges;
import com.dominicjmarshall.springbootday1.util.constants.Roles;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class WebSecurity {
    private static final String[] WHITELIST = {
        "/",
        "/login",
        "/register",
        "/db-console/**",
        "/css/**",
        "/fonts/**",
        "/images/**",
        "/posts/**",
        "/js/**",
    };

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
        .authorizeRequests()
        .requestMatchers(WHITELIST).permitAll()
        .requestMatchers("/profile/**").authenticated()
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .requestMatchers("/editor/**").hasAnyRole("ADMIN", "EDITOR")
        .requestMatchers("/test").hasAuthority(Privileges.ACCESS_ADMIN_PANEL.getName())
        .and()
        .formLogin()
        .loginPage("/login")
        .loginProcessingUrl("/login")
        .usernameParameter("email")
        .passwordParameter("password")
        .defaultSuccessUrl("/", true)
        .failureUrl("/login?error")
        .permitAll()
        .and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/")
        .and()
        .rememberMe().rememberMeParameter("remember-me")
        .and()
        .httpBasic();

        //TODO: remove these after upgrading the DB from H2
        http.csrf().disable();
        http.headers().frameOptions().disable();

        return http.build();
    }
    
}
